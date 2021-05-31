package com.ujiuye.controller;


import com.ujiuye.pojo.User;
import com.ujiuye.service.IUserService;
import com.ujiuye.utils.util.MessageConstant;
import com.ujiuye.utils.util.PageResult;
import com.ujiuye.utils.util.QueryPageBean;
import com.ujiuye.utils.util.Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 员工表 前端控制器
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private IUserService iUserService;

    @PostMapping("/list")
    public PageResult list(@RequestBody QueryPageBean queryPageBean) {
        try {
            return iUserService.listQuery(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(0L, null);
        }
    }

    @PostMapping("/listAll")
    public Result listAll() {
        try {
            List<User> list = iUserService.list();
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        //若用户密码为空或者为空字符串,则设置默认密码123456
        try {
            String password = user.getPassword();
            if (password == null || password.equals("")) {
                password = "123456";
            }
            //对密码进行加密
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //设置用户密码(加密后的)
            user.setPassword(bCryptPasswordEncoder.encode(password));
            iUserService.saveUser(user);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody User User) {
        try {
            iUserService.updateUser(User);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    @GetMapping("/remove/{id}")
    public Result remove(@PathVariable("id") int id) {
        try {
            iUserService.removeById(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }

    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") int id) {
        try {
            User User = iUserService.getUser(id);
            return new Result(true, MessageConstant.GET_USER_SUCCESS, User);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USER_FAIL);
        }
    }

    @GetMapping("/getCurrentUser")
    public String getCurrentUser(HttpSession session) {
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = ((UserDetails) securityContext.getAuthentication().getPrincipal()).getUsername();
        return username;
    }

}

