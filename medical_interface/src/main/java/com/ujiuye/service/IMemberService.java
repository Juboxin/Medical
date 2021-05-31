package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.Member;
import com.ujiuye.utils.util.PageResult;
import com.ujiuye.utils.util.QueryPageBean;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */

public interface IMemberService extends IService<Member> {

    PageResult listPage(QueryPageBean queryPageBean);

    Date getRegList();

    Map<String, Object> memberEcharts();

    Integer todayNewMember() throws Exception;

    Integer thisWeekNewMember();

    Integer thisMonthNewMember();

    Integer totalMember();

}
