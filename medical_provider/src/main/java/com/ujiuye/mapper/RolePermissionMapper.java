package com.ujiuye.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ujiuye.pojo.RolePermission;

/**
 * <p>
 * 角色表与权限表的中间表,一个角色有多个权限 Mapper 接口
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
