package com.cqupt.software_10.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.tool.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 *
 * 用户管理模块
 *
 * 用户注册
 * 用户登录
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;



    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user) {

        // 检查用户名是否已经存在
//        user.setUid(0);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user.getUsername());

        User existUser = userService.getOne(queryWrapper);

        if (existUser != null){
            return Result.fail(500,"用户名已存在");
        }
        String pwd = user.getPassword();
        // 对密码进行加密处理
        String password = SecurityUtil.hashDataSHA256(pwd);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setUpdateTime(null);
        System.out.println(user);
        userService.save(user);
        return Result.success(200,"注册成功");

    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request){

        String userName = user.getUsername();

        QueryWrapper queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username",user.getUsername());

        User getUser = userService.getOne(queryWrapper);


        if (getUser != null){
            String password = getUser.getPassword();
            // 进行验证密码
            String pwd = user.getPassword();
            String sha256 = SecurityUtil.hashDataSHA256(pwd);
            if (sha256.equals(password)){
                // session认证
                HttpSession session = request.getSession();
                session.setAttribute("username",user.getUsername());
                session.setAttribute("userId",getUser.getUid());

                String uid = getUser.getUid().toString();
                Cookie cookie = new Cookie("userId",uid );
                response.addCookie(cookie);

                //封装user对象返回
                User user1 = new User();
                user1.setUid(getUser.getUid());
                user1.setUsername(getUser.getUsername());
                user1.setRole(getUser.getRole());

                return Result.success(200,"登录成功",user1);
            }else {
                return Result.fail(500,"密码错误请重新输入",null);
            }

        }else {
            return Result.fail(500,"用户不存在",null);
        }
    }


    @PostMapping("/logout")
    public Result logout(HttpServletRequest request,HttpServletResponse response){

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        session.invalidate();
        Cookie cookie = new Cookie("userId", userId.toString());
        cookie.setMaxAge(0); // 设置过期时间为0，表示立即过期
        cookie.setPath("/"); // 设置Cookie的作用路径，保持与之前设置Cookie时的路径一致
        response.addCookie(cookie); // 添加到HTTP响应中
        return Result.success(200,"退出成功",null);
    }


    @GetMapping("/queryByUername/{username}")
    public Result queryByUername(@PathVariable String username) throws JsonProcessingException {
        User user = userService.queryByUername(username);

        return Result.success(200, "操作成功", user);
    }

}
