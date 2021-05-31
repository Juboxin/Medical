package com.ujiuye.test;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author JuBoxin
 * @date 2021/5/21 - 19:35
 */
//从Excel中导出数据到java
public class TestPoi {
    public static void main(String[] args) throws IOException {
        //获取整个表格
        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream("D:\\fileupload\\Excel\\student.xlsx"));
        //获取第一个工作表
        XSSFSheet sheet = sheets.getSheetAt(0);
        //获取该表数据总行数
        int rowNum = sheet.getLastRowNum();
        //遍历rowNum来逐行读取表格
        //i=1表示跳过索引0,即从第二个索引开始,从第2行开始遍历数据,跳过表头,为了避免数据类型不一致导致的报错
        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            String name = row.getCell(0).getStringCellValue();
            //默认用double来接numeric类型数据
            double age = row.getCell(1).getNumericCellValue();
            System.out.println("name=" + name + "age=" + age);
        }
    }
}
