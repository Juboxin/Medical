package com.ujiuye.test;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author JuBoxin
 * @date 2021/5/21 - 19:35
 */
//从java中导出数据到Excel
public class TestPoi1 {
    public static void main(String[] args) throws IOException {
        //新建表格 (在内存中创建,最后还需要将内存中的内容写入file文件)
        XSSFWorkbook workbook = new XSSFWorkbook();
        //新建工作表
        XSSFSheet sheet = workbook.createSheet("teacher");
        //创建表头
        XSSFRow rowHead = sheet.createRow(0);
        //在表头行中创建两个单元格
        rowHead.createCell(0).setCellValue("name");
        rowHead.createCell(1).setCellValue("age");
        //循环创建多行数据
        for (int i = 1; i <= 10; i++) {
            XSSFRow rowBody = sheet.createRow(i);
            //在标题行中创建两个单元格
            rowBody.createCell(0).setCellValue("name" + i);
            rowBody.createCell(1).setCellValue(i);
        }
        //创建一个文件
        File file = new File("D:\\fileupload\\Excel\\teacher.xlsx");
        //把内存中创建的表格的内容全部写入到file文件
        workbook.write(new FileOutputStream(file));
        workbook.close();
        System.out.println(sheet.getSheetName() + ".xlsx文件导出成功");
    }

}
