package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface IRoleService extends IService<Role> {
    List<Role> listByUserId(int user_id);
}
