package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.CheckgroupCheckitemMapper;
import com.ujiuye.mapper.CheckgroupMapper;
import com.ujiuye.pojo.Checkgroup;
import com.ujiuye.pojo.CheckgroupCheckitem;
import com.ujiuye.service.ICheckgroupService;
import com.ujiuye.utils.util.PageResult;
import com.ujiuye.utils.util.QueryPageBean;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 检查组 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class CheckgroupServiceImpl extends ServiceImpl<CheckgroupMapper, Checkgroup> implements ICheckgroupService {
    @Resource
    private CheckgroupMapper checkgroupMapper;
    @Resource
    private CheckgroupCheckitemMapper checkgroupCheckitemMapper;

    @Override
    public void save(Checkgroup checkgroup, Integer[] checkitemids) {
        //添加检查组
        checkgroupMapper.insert(checkgroup);
        //获取所添加检查组对象的ID
        Integer checkgroupId = checkgroup.getId();
        //添加检查组与检查项的中间表
        for (Integer checkitemid : checkitemids) {
            CheckgroupCheckitem checkgroupCheckitem = new CheckgroupCheckitem();
            checkgroupCheckitem.setCheckgroupId(checkgroupId);
            checkgroupCheckitem.setCheckitemId(checkitemid);
            checkgroupCheckitemMapper.insert(checkgroupCheckitem);
        }

    }

    @Override
    public PageResult listPage(QueryPageBean queryPageBean) {
//        创建分页对象Page,类型选择 com.baomidou.mybatisPluss
        Page<Checkgroup> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
//        创建查询条件
        String queryString = queryPageBean.getQueryString();
//        创建查询包装器
        QueryWrapper<Checkgroup> queryWrapper = new QueryWrapper<>();
//        判断查询条件是否为空,或者等于空字符串
        if (queryString != null && !queryString.equals("")) {
//            使用查询包装器进行模糊查询
            queryWrapper.like("code", queryString)
                    .or().like("name", queryString)
                    .or().like("helpcode", queryString);
        }
        Page<Checkgroup> checkgroupPage = checkgroupMapper.selectPage(page, queryWrapper);
//        参数为总记录数,查询的数据集合
        return new PageResult(checkgroupPage.getTotal(), checkgroupPage.getRecords());


    }

    @Override
    public void update(Checkgroup checkgroup, Integer[] checkitemids) {
//        修改检查组信息
        checkgroupMapper.updateById(checkgroup);

//        修改检查项(增加或减少)  方案:如下          并重新添加数据

//        删除检查组和检查项的中间表中本次checkgroup对应的数据
        checkgroupCheckitemMapper.delete(new QueryWrapper<CheckgroupCheckitem>().eq("checkgroup_id", checkgroup.getId()));
//        再向检查组和检查项的中间表中添加本次checkgroup对应的数据
        for (Integer checkitemid : checkitemids) {
            CheckgroupCheckitem checkgroupCheckitem = new CheckgroupCheckitem();
            checkgroupCheckitem.setCheckgroupId(checkgroup.getId());
            checkgroupCheckitem.setCheckitemId(checkitemid);
            checkgroupCheckitemMapper.insert(checkgroupCheckitem);
        }
    }
}
