package com.ujiuye.controller;


import com.ujiuye.pojo.Member;
import com.ujiuye.service.IMemberService;
import com.ujiuye.utils.util.*;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private IMemberService iMemberService;

    @RequestMapping("/listPage")
    public PageResult listPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            return iMemberService.listPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(0L, null);
        }
    }

    @RequestMapping("/save")
    public Result save(@RequestBody Member member) {
        try {
            boolean save = iMemberService.save(member);
            return new Result(true, MessageConstant.ADD_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
    }

    @RequestMapping("/remove")
    public Result remove(Integer id) {
        try {
            iMemberService.removeById(id);
            return new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }
    }

    @RequestMapping("/getById")
    public Result getById(Integer id) {
        try {
            Member member = iMemberService.getById(id);
            if (member != null) {
                return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS, member);
            }
            return new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Member member) {
        try {
            iMemberService.updateById(member);
            return new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(QueryPageBean queryPageBean) {
        //查询当前页数据
        PageResult pageResult = iMemberService.listPage(queryPageBean);
        //把数据写入一个Excel
        File file = listToExcel(pageResult.getRows(), queryPageBean.getCurrentPage());
        //将Excel从服务器下载到客户端
        System.out.println(file.getPath());
        return MyFileUtils.download(file.getName(), "D:\\fileupload\\Excel\\");
    }

    private File listToExcel(List<Member> list, int pageNum) {
        File file = new File("D:\\fileupload\\Excel\\第" + pageNum + "页会员数据.xlsx");
        //创建表(在内存中)
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workbook.createSheet("会员信息");
        //设置表头
        XSSFRow rowHead = sheet.createRow(0);
        rowHead.createCell(0).setCellValue("id");
        rowHead.createCell(1).setCellValue("姓名");
        rowHead.createCell(2).setCellValue("性别");
        rowHead.createCell(3).setCellValue("身份证号码");
        rowHead.createCell(4).setCellValue("电话号码");
        rowHead.createCell(5).setCellValue("注册时间");
        rowHead.createCell(6).setCellValue("邮箱");
        rowHead.createCell(7).setCellValue("生日");
        //遍历数据(int i 从1开始遍历,是为了忽略表头信息,避免因为数据类型不同导致的报错)
        for (int i = 1; i <= list.size(); i++) {
            Member member = list.get(i - 1);
            XSSFRow rowBody = sheet.createRow(i);
            rowBody.createCell(0).setCellValue(member.getId());
            rowBody.createCell(1).setCellValue(member.getName());
            rowBody.createCell(2).setCellValue(member.getSex());
            rowBody.createCell(3).setCellValue(member.getIdcard());
            rowBody.createCell(4).setCellValue(member.getPhonenumber());
            rowBody.createCell(5).setCellValue(member.getRegtime().toString());
            rowBody.createCell(6).setCellValue(member.getEmail());
            rowBody.createCell(7).setCellValue(member.getBirthday().toString());
        }
        try {
            workbook.write(new FileOutputStream(file));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @RequestMapping("/getRegList")
    public List getRegList() {
        int year1 = new Date().getYear() + 1900;
        int year2 = iMemberService.getRegList().getYear() + 1900;
        List list = new ArrayList();
        for (int i = year2; i <= year1; i++) {
            list.add(i);
        }
        return list;
    }

    @RequestMapping("/memberEcharts")
    public Result memberEcharts() {
        try {
            Map<String, Object> map = iMemberService.memberEcharts();
            return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

}

