package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.Order;
import com.ujiuye.utils.util.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface IOrderService extends IService<Order> {

    Result submit(Map<String, String> map) throws Exception;

    Integer todayOrderNumber() throws Exception;

    Integer todayVisitsNumber() throws Exception;

    Integer thisWeekOrderNumber();

    Integer thisWeekVisitsNumber();

    Integer thisMonthOrderNumber();

    Integer thisMonthVisitsNumber();

    List<Map<String, Object>> hotSetmeal();
    
}
