package com.ujiuye.controller;


import com.ujiuye.service.IOrderService;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/submit")
    public Result order(@RequestBody Map<String, String> map) {
        try {
            return iOrderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }
}

