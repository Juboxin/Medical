package com.ujiuye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ujiuye.pojo.User;

/**
 * <p>
 * 员工表 服务类
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
public interface IUserService extends IService<User> {
    User getByUserName(String username);
}
