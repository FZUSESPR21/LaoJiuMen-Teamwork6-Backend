package team.ljm.secw.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import team.ljm.secw.entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentExcelUtil {

    private static int totalRows = 0;
    private static int totalCells = 0;
    private static String errorMsg;

    public static int getTotalRows()  { return totalRows;}
    public static int getTotalCells() {  return totalCells;}
    public static String getErrorInfo() { return errorMsg; }

    public static List<Student> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();
        System.out.println("文件名"+fileName);
        if (fileName == null) {
            fileName = "";
        }
        List<Student> stuList = null;
        try {
            if (!validateExcel(fileName)) {
                return null;
            }
            boolean isExcel2003 = true;
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            stuList = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stuList;
    }


    public static List<Student> createExcel(InputStream is, boolean isExcel2003) {
        List<Student> stuList = null;
        try{
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            stuList = readExcelValue(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stuList;
    }

    private static List<Student> readExcelValue(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        totalRows = sheet.getPhysicalNumberOfRows();
        System.out.println("行数======="+totalRows);
        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println("总列数=========="+totalCells);
        }
        List<Student> studentList = new ArrayList<Student>();

        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            Student student = new Student();

            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (c == 0) {
                    student.setAccount(getValue(cell));
                } else if(c == 1) {
                    student.setStudentName(getValue(cell));
                } else if(c == 2) {
                    student.setEmail(getValue(cell));
                }
            }

            studentList.add(student);
        }
        return studentList;
    }

    private static List<Student> readScoreExcelValue(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        totalRows = sheet.getPhysicalNumberOfRows();
        System.out.println("行数======="+totalRows);

        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println("总列数=========="+totalCells);
        }

        List<Student> studentList = new ArrayList<Student>();

        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }
            Student student = new Student();

            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (c == 0) {
                    student.setAccount(getValue(cell));
                } else if(c == 1) {
                    student.setStudentName(getValue(cell));
                } else if(c == 2) {
                    student.setEmail(getValue(cell));
                }
            }

            studentList.add(student);
        }

        return studentList;
    }

    public static String getValue(Cell cell) {
        String value = "";

        if(null==cell){
            return value;
        }

        switch (cell.getCellType()) {

            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = format.format(date);;
                }else {
                    BigDecimal big= BigDecimal.valueOf(cell.getNumericCellValue());
                    value = big.toString();

                    if(null!=value&&!"".equals(value.trim())){
                        String[] item = value.split("[.]");

                        if(1<item.length&&"0".equals(item[1])){
                            value=item[0];
                        }

                    }

                }
                break;

            case STRING:
                value = cell.getRichStringCellValue().toString();
                break;

            case FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {
                    value = cell.getStringCellValue().toString();
                }
                break;

            case BOOLEAN:
                value = ""+ cell.getBooleanCellValue();
                break;

            default:
                value = cell.getStringCellValue().toString();
        }

        if("null".endsWith(value.trim())){
            value="";
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
