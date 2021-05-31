package com.ujiuye.service;

import com.ujiuye.pojo.Permission;
import com.ujiuye.pojo.Role;
import com.ujiuye.pojo.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JuBoxin
 * @date 2021/5/28 - 14:30
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private IUserService iUserService;
    @Reference
    private IRoleService iRoleService;
    @Reference
    private IPermissionService iPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名判断用户是否存在
        User user = iUserService.getByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不正确");
        }
        //创建一个集合用于保存用户的角色和权限
        List<GrantedAuthority> authorityList = new ArrayList<>();

        //查询用户对应的角色
        List<Role> roleList = iRoleService.listByUserId(user.getId());
        for (Role role : roleList) {
            String keyword = role.getKeyword();
            authorityList.add(new SimpleGrantedAuthority(keyword));
        }

        //查询用户对应的权限
        List<Permission> permissionList = iPermissionService.listByUserId(user.getId());
        for (Permission permission : permissionList) {
            String keyword = permission.getKeyword();
            authorityList.add(new SimpleGrantedAuthority(keyword));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorityList);
    }
}
