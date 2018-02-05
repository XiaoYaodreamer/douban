package Tools;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelMaker {

   private ExcelMaker(){

   }

    //工作簿
    private static HSSFWorkbook workBook;

    //表一
    private static HSSFSheet sheet;



    //初始化表
    private static void initHSSFWorkbook(String sheetName) {
        workBook = new HSSFWorkbook();
        sheet = workBook.createSheet(sheetName);
    }

    //创建标题行
    private static void createTitleRow(Map<String, String> titleMap, String sheetName) {
        //0行
        //0列到7列
        CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, titleMap.size() - 1);
        sheet.addMergedRegion(titleRange);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(sheetName);
    }






     //创建表头
     //1行
     //0列到7列
     //序号、书名等
    private static void createHeadRow(Map<String, String> titleMap) {
        HSSFRow headRow = sheet.createRow(1);
        int i = 0;
        for (String entry : titleMap.keySet()) {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(titleMap.get(entry));
            i++;
        }
    }

    //导入数据
    //反射
    private static void createContentRow(List<?> dataList, Map<String, String> titleMap) {
        try {
            int i = 0;
            for (Object obj : dataList) {
                HSSFRow textRow = sheet.createRow(2 + i);
                int j = 0;
                for (String entry : titleMap.keySet()) {
                    String method = "get" + entry.substring(0, 1).toUpperCase() + entry.substring(1);
                    Method m = obj.getClass().getMethod(method, null);
                    String value = m.invoke(obj, null).toString();
                    HSSFCell textCell = textRow.createCell(j);
                    textCell.setCellValue(value);
                    j++;
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   //建表
    public static void export(List<?> dataList, Map<String, String> titleMap, String sheetName) {
        System.out.println("the number of the book");
        System.out.println(dataList.size());
        initHSSFWorkbook(sheetName);
        createTitleRow(titleMap, sheetName);
        createHeadRow(titleMap);
        createContentRow(dataList, titleMap);

        try {

            String fileName = "douban" + ".xls";
            //导出文件
            OutputStream out = new FileOutputStream(fileName);
            workBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
