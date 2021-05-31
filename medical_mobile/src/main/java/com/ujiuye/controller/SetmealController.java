package com.ujiuye.controller;


import com.ujiuye.pojo.Setmeal;
import com.ujiuye.service.ISetmealService;
import com.ujiuye.utils.util.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    //    从配置文件中获取属性值
    //    fileUploadPath是配置文件中规定的文件上传位置
    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Reference
    private ISetmealService iSetmealService;

    //    上传图片  返回图片的名字
    //    MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。
    //    MyFileUtils是文件操作工具类
    @RequestMapping("/upload")
    public String upload(@RequestParam("imgFile") MultipartFile multipartFile) {
        File file = MyFileUtils.upload(multipartFile, fileUploadPath);
        if (file != null) {//文件上传成功
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_UPLOAD, file.getName());
        }
        return file.getName();
    }

    @RequestMapping("/save")
    public Result save(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            iSetmealService.save(setmeal, checkgroupIds);
            redisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB, setmeal.getImg().substring(18));
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/listPage")
    public PageResult listPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            return iSetmealService.listPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(0L, null);
        }
    }

    @RequestMapping("/getInfo/{id}")
    public Result getInfo(@PathVariable("id") Integer id) {
        try {
            Setmeal setmeal = iSetmealService.getInfo(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}

