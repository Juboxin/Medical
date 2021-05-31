package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.OrdersettingMapper;
import com.ujiuye.pojo.Caldate;
import com.ujiuye.pojo.Ordersetting;
import com.ujiuye.service.IOrdersettingService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单设置 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class OrdersettingServiceImpl extends ServiceImpl<OrdersettingMapper, Ordersetting> implements IOrdersettingService {

    @Resource
    private OrdersettingMapper ordersettingMapper;

    @Override
    public void saveList(List<Ordersetting> ordersettingList) {
        for (Ordersetting ordersetting : ordersettingList) {
            QueryWrapper<Ordersetting> queryWrapper = new QueryWrapper<>();
            //判断是否存在此日期,若存在则更新信息,不存在则添加信息
            queryWrapper.eq("orderDate", ordersetting.getOrderdate());
            Ordersetting getordersetting = ordersettingMapper.selectOne(queryWrapper);
            if (getordersetting == null) {
                ordersettingMapper.insert(ordersetting);
            } else {
                getordersetting.setNumber(ordersetting.getNumber());
                ordersettingMapper.updateById(getordersetting);
            }

        }
    }

    @Override
    public List<Caldate> listOrdersetting(String date) {
        String beginTime = date + "-01";
        String endTime = date + "-31";
        QueryWrapper<Ordersetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("orderDate", beginTime, endTime);
        List<Ordersetting> ordersettings = ordersettingMapper.selectList(queryWrapper);
        ArrayList<Caldate> list = new ArrayList<>();
        //将ordersettings的数据转换为前台需要的格式
        if (ordersettings != null && ordersettings.size() > 0) {
            for (Ordersetting ordersetting : ordersettings) {
                int day = ordersetting.getOrderdate().getDayOfMonth();
                Integer number = ordersetting.getNumber();
                Integer reservations = ordersetting.getReservations();
                list.add(new Caldate(day, number, reservations));
            }
        }
        return list;
    }

    @Override
    public void update(String date, Integer number) {
        QueryWrapper<Ordersetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderDate", date);
        Ordersetting ordersetting = ordersettingMapper.selectOne(queryWrapper);
        if (ordersetting != null) {
            //表示此日期存在
            ordersetting.setNumber(number);
            ordersettingMapper.updateById(ordersetting);
        } else {
            //表示此日期不存在,需要新增一个
            DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yy-MM-dd");
            LocalDate date1 = LocalDate.parse(date, dfm);
            Ordersetting ordersetting1 = new Ordersetting(0, date1, number, 0);
            ordersettingMapper.insert(ordersetting1);

        }
    }
}
