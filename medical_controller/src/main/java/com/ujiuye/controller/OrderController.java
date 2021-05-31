package com.ujiuye.controller;


import com.ujiuye.service.IMemberService;
import com.ujiuye.service.IOrderService;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.MyFileUtils;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private IOrderService iOrderService;
    @Reference
    private IMemberService iMemberService;

    @RequestMapping("/getBusiness")
    public Result getBusiness() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("todayNewMember", iMemberService.todayNewMember());
            map.put("thisWeekNewMember", iMemberService.thisWeekNewMember());
            map.put("thisMonthNewMember", iMemberService.thisMonthNewMember());
            map.put("totalMember", iMemberService.totalMember());


            map.put("todayOrderNumber", iOrderService.todayOrderNumber());
            map.put("todayVisitsNumber", iOrderService.todayVisitsNumber());
            map.put("thisWeekOrderNumber", iOrderService.thisWeekOrderNumber());
            map.put("thisWeekVisitsNumber", iOrderService.thisWeekVisitsNumber());
            map.put("thisMonthOrderNumber", iOrderService.thisMonthOrderNumber());
            map.put("thisMonthVisitsNumber", iOrderService.thisMonthVisitsNumber());

            map.put("hotSetmeal", iOrderService.hotSetmeal());
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download() {
        try {
            File file = new File("D:\\fileupload\\Excel\\demo.xlsx");
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.getRow(1).getCell(1).setCellValue(Double.parseDouble(iMemberService.todayNewMember().toString()));
            sheet.getRow(1).getCell(3).setCellValue(Double.parseDouble(iMemberService.totalMember().toString()));
            sheet.getRow(2).getCell(1).setCellValue(Double.parseDouble(iMemberService.thisWeekNewMember().toString()));
            sheet.getRow(2).getCell(3).setCellValue(Double.parseDouble(iMemberService.thisMonthNewMember().toString()));
            sheet.getRow(4).getCell(1).setCellValue(Double.parseDouble(iOrderService.todayOrderNumber().toString()));
            sheet.getRow(4).getCell(3).setCellValue(Double.parseDouble(iOrderService.todayVisitsNumber().toString()));
            sheet.getRow(5).getCell(1).setCellValue(Double.parseDouble(iOrderService.thisWeekOrderNumber().toString()));
            sheet.getRow(5).getCell(3).setCellValue(Double.parseDouble(iOrderService.thisWeekVisitsNumber().toString()));
            sheet.getRow(6).getCell(1).setCellValue(Double.parseDouble(iOrderService.thisMonthOrderNumber().toString()));
            sheet.getRow(6).getCell(3).setCellValue(Double.parseDouble(iOrderService.thisMonthVisitsNumber().toString()));
            List<Map<String, Object>> list = iOrderService.hotSetmeal();
            for (int i = 9, j = 0; i < list.size(); i++, j++) {
                Map<String, Object> map = list.get(j);
                sheet.getRow(i).getCell(0).setCellValue(map.get("name").toString());
                sheet.getRow(i).getCell(1).setCellValue(Double.parseDouble(map.get("setmeal_count").toString()));
                sheet.getRow(i).getCell(2).setCellValue(Double.parseDouble(map.get("proportion").toString()));
                sheet.getRow(i).getCell(3).setCellValue("");
            }
            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            return MyFileUtils.download(file.getName(), "D:\\fileupload\\Excel\\");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}

