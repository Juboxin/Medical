package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.MemberMapper;
import com.ujiuye.pojo.Member;
import com.ujiuye.service.IMemberService;
import com.ujiuye.utils.util.DateUtils;
import com.ujiuye.utils.util.PageResult;
import com.ujiuye.utils.util.QueryPageBean;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    @Resource
    private MemberMapper memberMapper;

    //获取年龄
    private List<String> getDate(int smallage, int bigage) {
        Date date = new Date();
        Date olderDate = new Date(date.getYear() - (bigage + 1), date.getMonth(), date.getDate() + 1);
        Date youngDate = new Date(date.getYear() - smallage, date.getMonth(), date.getDate());
        String d1 = new SimpleDateFormat("yyyy-MM-dd").format(olderDate);
        String d2 = new SimpleDateFormat("yyyy-MM-dd").format(youngDate);
        List<String> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        return list;
    }


    @Override
    public PageResult listPage(QueryPageBean queryPageBean) {
        Page<Member> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        String queryString = queryPageBean.getQueryString();
        String sex = queryPageBean.getSex();
        String age = queryPageBean.getAge();
        Integer reg = queryPageBean.getReg();

        //筛选标签查询
        if (sex != null && !sex.equals("")) {
            if (age != null && !age.equals("")) {
                //获取年龄范围的数组 age[0]为较小年龄 age[1]为较大年龄
                String[] ages = age.split("-");
                List<String> list = getDate(Integer.parseInt(ages[0]), Integer.parseInt(ages[1]));
                if (reg != null && !reg.equals("")) {
                    queryWrapper.eq("sex", sex).between("birthday", list.get(0), list.get(1)).likeRight("regTime", reg);
                } else {
                    queryWrapper.eq("sex", sex).between("birthday", list.get(0), list.get(1));
                }
            } else {
                if (reg != null && !reg.equals("")) {
                    queryWrapper.eq("sex", sex).likeRight("regTime", reg);
                } else {
                    queryWrapper.eq("sex", sex);
                }
            }
        } else {
            if (age != null && !age.equals("")) {
                //获取年龄范围的数组 age[0]为较小年龄 age[1]为较大年龄
                String[] ages = age.split("-");
                List<String> list = getDate(Integer.parseInt(ages[0]), Integer.parseInt(ages[1]));
                if (reg != null && !reg.equals("")) {
                    queryWrapper.between("birthday", list.get(0), list.get(1)).likeRight("regTime", reg);
                } else {
                    queryWrapper.between("birthday", list.get(0), list.get(1));
                }
            } else {
                if (reg != null && !reg.equals("")) {
                    queryWrapper.likeRight("regTime", reg);
                }
            }
        }
        //搜索框模糊查询
        if (queryString != null && !queryString.equals("")) {
            queryWrapper.like("name", queryString)
                    .or().like("phoneNumber", queryString);
        }
        Page<Member> memberPage = memberMapper.selectPage(page, queryWrapper);
        return new PageResult(memberPage.getTotal(), memberPage.getRecords());
    }

    @Override
    public Date getRegList() {
        return memberMapper.getRegList();
    }

    @Override
    public Map<String, Object> memberEcharts() {
        List<String> months = new ArrayList<>();
        List<Integer> memberCount = new ArrayList<>();
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        //把日历时间往前推12个月
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i <= 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            //获取日历的时间
            Date time = calendar.getTime();
            //格式时间
            String format = new SimpleDateFormat("yyyy-MM").format(time);
            months.add(format);
        }
        //查询集合中所有月份的会员总数
        for (String month : months) {
            QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
            memberQueryWrapper.le("regTime", month + "-31");
            Integer count = memberMapper.selectCount(memberQueryWrapper);
            memberCount.add(count);
        }
        //将months和memberCount添加到map集合中并返回
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }

    @Override
    public Integer todayNewMember() throws Exception {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        String string = DateUtils.parseDate2String(DateUtils.getToday());
        queryWrapper.eq("regTime", string);
        return memberMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer thisWeekNewMember() {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("regTime", DateUtils.getFirstDay4ThisMonth(), DateUtils.getToday());
        return memberMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer thisMonthNewMember() {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("regTime", DateUtils.getThisWeekMonday(DateUtils.getToday()), DateUtils.getToday());
        return memberMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer totalMember() {
        return memberMapper.selectCount(null);
    }
}


