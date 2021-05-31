package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.CheckgroupCheckitemMapper;
import com.ujiuye.pojo.CheckgroupCheckitem;
import com.ujiuye.service.ICheckgroupCheckitemService;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 检查组与检查项的中间表,一个检查组有多个检查项 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class CheckgroupCheckitemServiceImpl extends ServiceImpl<CheckgroupCheckitemMapper, CheckgroupCheckitem> implements ICheckgroupCheckitemService {
    @Resource
    private CheckgroupCheckitemMapper checkgroupCheckitemMapper;

    @Override
    public Result listByCheckgroupId(Integer checkgroupId) {
        QueryWrapper<CheckgroupCheckitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkgroup_id", checkgroupId);
//        获取检查组和检查项的中间表集合
        List<CheckgroupCheckitem> selectList = checkgroupCheckitemMapper.selectList(queryWrapper);
//        新建checkitem_id集合,从selectlist中提取所需checkitem_id并添加到checkitem_id集合
        List<Integer> checkitemIds = new ArrayList<>();
        for (CheckgroupCheckitem checkgroupCheckitem : selectList) {
            checkitemIds.add(checkgroupCheckitem.getCheckitemId());
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUPCHECKITEM_SUCCESS, checkitemIds);
    }
}
