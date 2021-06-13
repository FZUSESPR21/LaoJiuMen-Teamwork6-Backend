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
    private static final File WORD_FILE = new File("./src/word.txt");
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
        //TODO: 敏感词过滤
        return contents;
    }

    private static void init() {
        // TODO: 建立DFA
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
