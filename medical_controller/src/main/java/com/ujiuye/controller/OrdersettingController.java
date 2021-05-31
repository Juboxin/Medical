package com.ujiuye.controller;


import com.ujiuye.pojo.Caldate;
import com.ujiuye.pojo.Ordersetting;
import com.ujiuye.service.IOrdersettingService;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.MyFileUtils;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单设置 前端控制器
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Value("${ordersettingUploadPath}")
    private String ordersettingUploadPath;
    @Reference
    private IOrdersettingService iOrdersettingService;

    @RequestMapping("/uploadTempleate")
    public Result uploda(@RequestParam("excelFile") MultipartFile multipartFile) {
        try {
            File excelFile = MyFileUtils.upload(multipartFile, ordersettingUploadPath);
            if (excelFile != null) {
                List<Ordersetting> ordersettingList = excelToList(excelFile);
                iOrdersettingService.saveList(ordersettingList);
                return new Result(true, MessageConstant.UPLOAD_SUCCESS);
            }
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    //把Excel文件读取为一个list集合
    private List<Ordersetting> excelToList(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        ArrayList<Ordersetting> list = new ArrayList<>();
        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            Date date = row.getCell(0).getDateCellValue();
            int number = (int) row.getCell(1).getNumericCellValue();
            //将date类型转换为localdate
            //给Ordersetting实体类添加生成构造函数的注解
            Ordersetting order = new Ordersetting(0, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), number, 0);
            list.add(order);
        }
        return list;
    }

    @RequestMapping("/listCaldate")
    public Result list(String date) {
        try {
            List<Caldate> caldates = iOrdersettingService.listOrdersetting(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, caldates);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL, null);
        }
    }

    @RequestMapping("/update")
    public Result update(String date, Integer number) {
        try {
            iOrdersettingService.update(date, number);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}

