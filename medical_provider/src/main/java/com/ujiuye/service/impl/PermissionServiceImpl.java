package com.ujiuye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ujiuye.mapper.PermissionMapper;
import com.ujiuye.pojo.Permission;
import com.ujiuye.service.IPermissionService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> listByUserId(int user_id) {
        return permissionMapper.listByUserId(user_id);
    }
}
