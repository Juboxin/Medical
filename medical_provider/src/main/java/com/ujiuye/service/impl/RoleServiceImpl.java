package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.RoleMapper;
import com.ujiuye.pojo.Role;
import com.ujiuye.service.IRoleService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> listByUserId(int user_id) {
        return roleMapper.listByUserId(user_id);
    }
}
