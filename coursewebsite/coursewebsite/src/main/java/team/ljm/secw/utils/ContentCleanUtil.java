package team.ljm.secw.utils;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 内容清洗
 *
 * @date 2021/06/13
 */
public class ContentCleanUtil {
    private static final File WORD_FILE = new File("./static/word.txt");
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private static final char LEGAL_CHARACTER = '*';
    private static Map<Character, Object> states = null;

    static {
        init();
    }

    /**
     * 清洗内容
     *
     * @param content 原文
     * @return 清洗后内容
     */
    public static String clean(String content) {
        if ((content == null) || ("".equals(content)) || (content.length() < 1)) {
            return content;
        }
        return XSSClean(String.valueOf(sensitiveClean(content.toCharArray())));
    }

    private static String XSSClean(String contents){
        // TODO: XSS过滤
        return contents;
    }

    private static char[] sensitiveClean(char[] contents) {
        int begin = -1;
        int i = 0;
        Map<Character, Object> nowMap = states;
        while (i < contents.length) {
            Character c = contents[i];
            if (nowMap.containsKey(c)) {
                if (begin == -1) {
                    begin = i;
                }
                nowMap = (Map<Character, Object>) nowMap.get(c);
                if ("1".equals(nowMap.get('~'))) {
                    for (int j = begin;j <= i;j++) {
                        contents[j] = LEGAL_CHARACTER;
                    }
                    begin = -1;
                    nowMap = states;
                }
            } else {
                if (begin != -1) {
                    i = begin;
                }
                begin = -1;
                nowMap = states;
            }
            ++i;
        }
        return contents;
    }

    private static void init() {
        String[] words = readMMAP().split("[\r\n, ]+");
        states = new HashMap<>(words.length << 2);
        Map<Character, Object> nowMap;
        for (String word : words) {
            nowMap = states;
            for (int i = 0;i < word.length();i++) {
                Character c = word.charAt(i);
                Object objectMap = nowMap.get(c);

                if (objectMap != null) {
                    nowMap = (Map<Character, Object>) objectMap;
                } else {
                    Map<Character, Object> newMap = new HashMap<>();
                    newMap.put('~', "0");
                    nowMap.put(c, newMap);
                    nowMap = newMap;
                }

                if (i == word.length() - 1) {
                    nowMap.put('~', "1");
                }
            }
        }
    }

    private static String readMMAP(){
        RandomAccessFile raf = null;
        MappedByteBuffer mbb = null;
        try {
            raf = new RandomAccessFile(ContentCleanUtil.WORD_FILE, "r");
            mbb = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, ContentCleanUtil.WORD_FILE.length());
            if (mbb != null){
                return ENCODING.decode(mbb).toString();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mbb != null){
                Cleaner var1 = ((DirectBuffer)mbb).cleaner();
                if (var1 != null) {
                    var1.clean();
                }
            }
            if (raf != null){
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
