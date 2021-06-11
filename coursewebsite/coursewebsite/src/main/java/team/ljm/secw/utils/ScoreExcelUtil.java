package team.ljm.secw.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import team.ljm.secw.dto.ExcelDataDTO;
import team.ljm.secw.dto.ExcelHeadDTO;
import team.ljm.secw.dto.ScoreDTO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreExcelUtil {

    public static XSSFCellStyle fontStyle;

    private static int totalRows = 0;

    private static int totalCells = 0;

    private static String errorMsg;

    public static XSSFWorkbook createExcelFile(String sheetName, List<ExcelHeadDTO> headList, List<ExcelDataDTO> dataList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        createFont(workbook);
        createExcelHead(sheet, headList);
        createExcelData(sheet, workbook, dataList);
        return workbook;
    }

    public static void createFont(XSSFWorkbook workbook) {
        fontStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        fontStyle.setFont(font);
        fontStyle.setAlignment(HorizontalAlignment.CENTER);
        fontStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public static void createExcelHead(XSSFSheet sheet, List<ExcelHeadDTO> headList) {
        XSSFRow row1 = sheet.createRow(0);
        XSSFRow row2 = sheet.createRow(1);
        for (ExcelHeadDTO head : headList) {
            XSSFRow row;
            if(!head.getFirstRow().equals(head.getLastRow()) || !head.getFirstCol().equals(head.getLastCol())) {
                CellRangeAddress region = new CellRangeAddress(head.getFirstRow(), head.getLastRow(), head.getFirstCol(), head.getLastCol());
                sheet.addMergedRegion(region);
            }

            if (head.getFirstRow().equals(0)) {
                row = row1;
            } else {
                row = row2;
            }

            XSSFCell cell = row.createCell(head.getFirstCol());
            cell.setCellValue(head.getHeadText());
            cell.setCellStyle(fontStyle);
            sheet.setColumnWidth(head.getFirstCol(), head.getWidth());
        }
    }

    public static void createExcelData(XSSFSheet sheet, XSSFWorkbook workbook, List<ExcelDataDTO> dataList) {
        for (int i = 0; i < dataList.size() ; i+=3) {
            sheet.createRow(dataList.get(i).getRow());
        }

        for (ExcelDataDTO data : dataList) {
            XSSFRow row = sheet.getRow(data.getRow());
            XSSFCell cell = row.createCell(data.getCol());
            if (data.getData() != null) {
                if (data.getData() instanceof Date) {
                    cell.setCellValue((Date) data.getData());
                    XSSFCellStyle style = workbook.createCellStyle();
                    XSSFDataFormat format = workbook.createDataFormat();
                    style.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
                    cell.setCellStyle(style);
                } else if (data.getData() instanceof Double) {
                    cell.setCellValue((Double) data.getData());
                    XSSFCellStyle style = workbook.createCellStyle();
                    XSSFDataFormat format = workbook.createDataFormat();
                    style.setDataFormat(format.getFormat("0.0000"));
                    cell.setCellStyle(style);
                } else if (data.getData() instanceof String) {
                    cell.setCellValue(data.getData().toString());
                } else if (data.getData() instanceof Integer) {
                    cell.setCellValue((Integer) data.getData());
                    XSSFCellStyle style = workbook.createCellStyle();
                    XSSFDataFormat format = workbook.createDataFormat();
                    style.setDataFormat(format.getFormat("0"));
                    cell.setCellStyle(style);
                }
            }
            if (data.getFormula() != null) {
                cell.setCellFormula(data.getFormula());
            }
            cell.setCellStyle(fontStyle);
        }
    }

    public static int getTotalRows()  {
        return totalRows;
    }

    public static int getTotalCells() {
        return totalCells;
    }

    public static String getErrorInfo() {
        return errorMsg;
    }

    public static List<ScoreDTO> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();
        System.out.println("文件名"+fileName);
        if (fileName == null) {
            fileName = "";
        }
        List<ScoreDTO> scoreList = null;
        try {
            if (!validateExcel(fileName)) {
                return null;
            }
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            scoreList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreList;
    }

    public static List<ScoreDTO> createExcel(InputStream is, boolean isExcel2003) {
        List<ScoreDTO> scoreList = null;
        try{
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            scoreList = readExcelValue(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoreList;
    }

    private static List<ScoreDTO> readExcelValue(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        totalRows = sheet.getPhysicalNumberOfRows();
        System.out.println("行数======="+totalRows);

        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getLastCellNum();
            System.out.println("总列数=========="+totalCells);
        }

        List<ScoreDTO> scoreList = new ArrayList<>();

        for (int r = 2; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            ScoreDTO score = new ScoreDTO();

            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (c == 0) {
                    score.setAccount(getCellValue(cell));
                } else if (c == 13) {
                    score.setUsualScore(Integer.parseInt(getCellValue(cell)));
                } else if (c == 14) {
                    score.setWrittenScore(Integer.parseInt(getCellValue(cell)));
                } else if (c == 15) {
                    score.setTotalScore(Integer.parseInt(getCellValue(cell)));
                }
            }

            scoreList.add(score);
        }
        return scoreList;
    }

    public static String getCellValue(Cell cell) {
        String value = null;
        DecimalFormat df = new DecimalFormat("0");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df2 = new DecimalFormat("0.00");
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                    if(null!=value&&!"".equals(value.trim())){
                        String[] item = value.split("[.]");
                        if(1<item.length&&"0".equals(item[1])){
                            value=item[0];
                        }
                    }
                }
                break;
            case BOOLEAN:
                value = "" + cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            case FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {
                    value = cell.getStringCellValue();
                }
                if(null!=value&&!"".equals(value.trim())){
                    String[] item = value.split("[.]");
                    if(1<item.length&&"0".equals(item[1])){
                        value=item[0];
                    }
                }
                break;
            default:
                break;
        }
        return value;
    }

    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    public static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

}
