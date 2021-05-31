package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.*;
import com.ujiuye.pojo.*;
import com.ujiuye.service.ISetmealService;
import com.ujiuye.utils.util.PageResult;
import com.ujiuye.utils.util.QueryPageBean;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {
    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealCheckgroupMapper setmealCheckgroupMapper;
    @Resource
    private CheckgroupMapper checkgroupMapper;
    @Resource
    private CheckgroupCheckitemMapper checkgroupCheckitemMapper;
    @Resource
    private CheckitemMapper checkitemMapper;

    @Override
    public void save(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealMapper.insert(setmeal);
        for (Integer checkgroupId : checkgroupIds) {
            SetmealCheckgroup setmealCheckgroup = new SetmealCheckgroup();
            setmealCheckgroup.setSetmealId(setmeal.getId());
            setmealCheckgroup.setCheckgroupId(checkgroupId);
            setmealCheckgroupMapper.insert(setmealCheckgroup);
        }
    }

    @Override
    public PageResult listPage(QueryPageBean queryPageBean) {
        Page<Setmeal> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        if (queryString != null && !queryString.equals("")) {
            queryWrapper.like("code", queryString)
                    .or().like("name", queryString)
                    .or().like("helpCode", queryString);
        }
        Page<Setmeal> setmealPage = setmealMapper.selectPage(page, queryWrapper);
        return new PageResult(setmealPage.getTotal(), setmealPage.getRecords());
    }

    @Override
    public Setmeal getInfo(Integer id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        //查询该套餐有多少个检查组
        QueryWrapper<SetmealCheckgroup> scQueryWrapper = new QueryWrapper<>();
        scQueryWrapper.eq("setmeal_id", setmeal.getId());
        List<SetmealCheckgroup> setmealCheckgroupList = setmealCheckgroupMapper.selectList(scQueryWrapper);
        if (setmealCheckgroupList == null || setmealCheckgroupList.size() == 0) {
            return setmeal;
        }
        List<Checkgroup> checkgroupList = new ArrayList<>();
        //获取每个检查组的信息
        for (SetmealCheckgroup setmealCheckgroup : setmealCheckgroupList) {
            Checkgroup checkgroup = checkgroupMapper.selectById(setmealCheckgroup.getCheckgroupId());
            //查询此检查组有什么检查项
            QueryWrapper<CheckgroupCheckitem> ccqueryWrapper = new QueryWrapper<>();
            ccqueryWrapper.eq("checkgroup_id", setmealCheckgroup.getCheckgroupId());
            List<CheckgroupCheckitem> checkgroupCheckitemList = checkgroupCheckitemMapper.selectList(ccqueryWrapper);
            if (checkgroupCheckitemList != null && checkgroupCheckitemList.size() > 0) {
                List<Checkitem> checkitemList = new ArrayList<>();
                for (CheckgroupCheckitem checkgroupCheckitem : checkgroupCheckitemList) {
                    Checkitem checkitem = checkitemMapper.selectById(checkgroupCheckitem.getCheckitemId());
                    checkitemList.add(checkitem);
                }
                checkgroup.setCheckitemList(checkitemList);
            }
            checkgroupList.add(checkgroup);
        }
        setmeal.setCheckgroupList(checkgroupList);
        return setmeal;
    }

    @Override
    public Map<String, Object> getCountSetmeal() {
        List<Map<String, Object>> countSetmeal = setmealMapper.getCountSetmeal();
        System.out.println("countSetmeal=" + countSetmeal);
        List<String> names = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : countSetmeal) {
            System.out.println("stringObjectMap=" + stringObjectMap);
            names.add(stringObjectMap.get("name").toString());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("setmealCount", countSetmeal);
        map.put("setmealNames", names);
        return map;
    }

}
