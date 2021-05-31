package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.MemberMapper;
import com.ujiuye.mapper.OrderMapper;
import com.ujiuye.mapper.OrdersettingMapper;
import com.ujiuye.pojo.Member;
import com.ujiuye.pojo.Order;
import com.ujiuye.pojo.Ordersetting;
import com.ujiuye.service.IOrderService;
import com.ujiuye.utils.util.DateUtils;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrdersettingMapper ordersettingMapper;
    @Resource
    private MemberMapper memberMapper;


    @Override
    public Result submit(Map<String, String> map) throws Exception {
        //检查用户所选择的预约日期是否已经提前进行了预约设置,如果没有则无法进行预约
        QueryWrapper<Ordersetting> osQueryWrapper = new QueryWrapper<>();
        String orderDate = map.get("orderDate");
        osQueryWrapper.eq("orderDate", DateUtils.parseString2Date(orderDate));
        Ordersetting ordersetting = ordersettingMapper.selectOne(osQueryWrapper);
        if (ordersetting == null) {
            //表示该日期没有进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查用户所选择的预约日期是否已经约满,如果已经约满则无法预约
        if (ordersetting.getNumber() == ordersetting.getReservations()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //没有约满的情况下,检查该用户是否为会员
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("phoneNumber", map.get("telephone"));
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if (member == null) {
            //表示不是会员,自动注册会员,给member自动赋值
            member = new Member();
            member.setName(map.get("name"));
            member.setSex(map.get("sex"));
            member.setPhonenumber(map.get("telephone"));
            member.setIdcard(map.get("idCard"));
            member.setRegtime(LocalDate.now());
            member.setPassword("123456");
            memberMapper.insert(member);
        }

        //本身就是会员或者非会员注册成功后
        //检查用户是否重复预约(同一个用户在同一天预约了同一个套餐),如果是重复预约,则无法再次预约
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("member_id", member.getId());
        orderQueryWrapper.eq("orderDate", DateUtils.parseString2Date(orderDate));
        orderQueryWrapper.eq("setmeal_id", "setmealId");
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if (order != null) {
            return new Result(false, MessageConstant.HAS_ORDERED);
        }
        //如果没有重复预约即order=null,则创建Order对象
        order = new Order();
        order.setMemberId(member.getId());
        order.setOrdertype("网站预约");
        try {
            order.setOrderdate(LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setSetmealId(Integer.parseInt(map.get("setmealId")));
        orderMapper.insert(order);
        //预约成功后更新已预约人数
        ordersetting.setReservations(ordersetting.getReservations() + 1);
        ordersettingMapper.updateById(ordersetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS);
    }

    @Override
    public Integer todayOrderNumber() throws Exception {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        String s = DateUtils.parseDate2String(DateUtils.getToday());
        orderQueryWrapper.eq("orderDate", s);
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public Integer todayVisitsNumber() throws Exception {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        String s = DateUtils.parseDate2String(DateUtils.getToday());
        orderQueryWrapper.eq("orderDate", s);
        orderQueryWrapper.eq("orderStatus", "已到诊");
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public Integer thisWeekOrderNumber() {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.between("orderDate", DateUtils.getThisWeekMonday(DateUtils.getToday()), DateUtils.getToday());
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public Integer thisWeekVisitsNumber() {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.between("orderDate", DateUtils.getThisWeekMonday(DateUtils.getToday()), DateUtils.getToday());
        orderQueryWrapper.eq("orderStatus", "已到诊");
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public Integer thisMonthOrderNumber() {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.between("orderDate", DateUtils.getFirstDay4ThisMonth(), DateUtils.getToday());
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public Integer thisMonthVisitsNumber() {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.between("orderDate", DateUtils.getFirstDay4ThisMonth(), DateUtils.getToday());
        orderQueryWrapper.eq("orderStatus", "已到诊");
        return orderMapper.selectCount(orderQueryWrapper);
    }

    @Override
    public List<Map<String, Object>> hotSetmeal() {
        return orderMapper.hotSetmeal();
    }
}
