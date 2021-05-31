package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.Permission;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface IPermissionService extends IService<Permission> {
    List<Permission> listByUserId(int user_id);
}
