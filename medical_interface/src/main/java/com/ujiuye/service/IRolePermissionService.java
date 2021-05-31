package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.RolePermission;

/**
 * <p>
 * 角色表与权限表的中间表,一个角色有多个权限 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface IRolePermissionService extends IService<RolePermission> {

}
