package com.ujiuye.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ujiuye.pojo.Member;

import java.util.Date;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */


public interface MemberMapper extends BaseMapper<Member> {
    Date getRegList();
    
}
