package com.cqupt.software_10.controller.user;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.software_10.common.R;
import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.entity.InsertUserVo;
import com.cqupt.software_10.entity.UpdateStatusVo;
import com.cqupt.software_10.entity.UserPwd;
import com.cqupt.software_10.entity.VerifyUserQ;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.entity.user.UserLog;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.user.UserLogService;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Autowired
    LogService logService;


    @Autowired
    private UserLogService userLogService;



    @GetMapping("/querUserNameExist")
    public Result querUserNameExist(@RequestParam String userName){
        User existUser = userService.getUserByName(userName);
        if (existUser != null){
            System.out.println(Result.fail(500,"用户已经存在",null).toString());
            return Result.fail(500,"用户已经存在",null);
        }
        System.out.println(Result.success(200, "用户名可用" , null).toString());
        return Result.success(200, "用户名可用" , null);
    }

    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user, @RequestParam String curUid) throws ParseException {

        System.out.println(user);
        // 检查用户名是否已经存在
        user.setUid("0");
        User existUser = userService.getUserByName(user.getUsername());
        if (existUser != null){
            return Result.success(500, "用户已经存在", null);
        }
        String pwd = user.getPassword();
        // 对密码进行加密处理
        String password = SecurityUtil.hashDataSHA256(pwd);
        user.setPassword(password);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        user.setCreateTime(date);
        user.setUpdateTime(null);
        user.setRole(0);
        user.setUid(new Random().nextInt() + "");
        user.setUploadSize(200);
        userService.saveUser(user);
        User user_log = userService.getUserById(curUid);
        logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "账户注册成功");

        //  操作日志记录
//       UserLog userLog = new UserLog();
//       User one = userService.getUserByName(user.getUsername());
//       Integer uid = one.getUid();
////       userLog.setId(new Random().nextInt());
//       userLog.setUid(uid);
//       userLog.setOpTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//       userLog.setOpType("用户注册");
//       userLogService.save(userLog);
       return Result.success(200, "成功", null);
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request){

        // 判断验证编码
        String code = request.getSession().getAttribute("code").toString();
        if(code==null) return Result.fail(500,"验证码已过期！");
        if(user.getCode()==null || !user.getCode().equals(code)) {
            return Result.fail(500, "验证码错误!");
        }

        String userName = user.getUsername();
        User getUser = userService.getUserByName(userName);
        String password = getUser.getPassword();


        if (getUser != null){
            // 用户状态校验
            // 判断用户是否激活
            System.out.println("getUser:"+  getUser+ "\n" + getUser.getUserStatus() );
            if (getUser.getUserStatus().equals("0")){
                logService.insertLog(getUser.getUid(), getUser.getRole(), getUser.getUsername() + "账户未激活");
                return Result.fail("该账户未激活");
            }
            if (getUser.getUserStatus().equals("2")){
                logService.insertLog(getUser.getUid(), getUser.getRole(), getUser.getUsername() + "该账户已经被禁用");
                return Result.fail("该账户已经被禁用");
            }

            String userStatus = getUser.getUserStatus();
            if(userStatus.equals("0")){ // 待激活
                return Result.fail(500,"账户未激活！");
            }else if(userStatus.equals("2")){
                return Result.fail(500,"用户已被禁用!");
            }

            // 进行验证密码
            String pwd = user.getPassword();
            String sha256 = SecurityUtil.hashDataSHA256(pwd);
            if (sha256.equals(password)){
                // 验证成功
//                UserLog userLog = new UserLog();
//                userLog.setUid(getUser.getUid());
//                userLog.setOpTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                userLog.setOpType("登录系统");
//                userLog.setUsername(userName);
//                System.out.println("userlog:"+userLog);
//                userLogService.save(userLog);
                // session认证
                HttpSession session = request.getSession();
//                request.setR
                session.setAttribute("username",user.getUsername());
                session.setAttribute("userId",getUser.getUid());

//                logService.insertLog(getUser.getUid(), user.getRole(), "用户登录");
                logService.insertLog(getUser.getUid(), getUser.getRole(), getUser.getUsername() + "登录成功");

                String token = SecurityUtil.generateToken(getUser.getUid());
                String uid = SecurityUtil.getUserIdFromToken(token);
                System.out.println("user.getUid()=" + user.getUid() + "uid=" + uid);

                return Result.success(200, token, getUser);
            }else {
                logService.insertLog(getUser.getUid(), getUser.getRole(), getUser.getUsername() + "登录成功");
                return Result.success("500","密码错误请重新输入");
            }
        }else {
            return Result.success(500,"用户不存在",null);
        }
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request,HttpServletResponse response){

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        logService.insertLog(user.getUid(), user.getRole(), "用户退出");

        session.invalidate();
        Cookie cookie = new Cookie("userId", userId.toString());
        cookie.setMaxAge(0); // 设置过期时间为0，表示立即过期
        cookie.setPath("/"); // 设置Cookie的作用路径，保持与之前设置Cookie时的路径一致
        response.addCookie(cookie); // 添加到HTTP响应中
        return Result.success(200,"退出成功",null);
    }


    /**
     * 管理员中心查看得所有用户信息
     * @return
     */
    @GetMapping("/allUser")
    public Map<String, Object> allUser(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize
                                       ){

        Map<String, Object> allUsers = userService.getUserPage(pageNum, pageSize);
         return allUsers;

    }



    @GetMapping("/querUser")
    public List<User> querUser(){
        return userService.querUser();
    }

    /**
     *
     *  管理员修改用户状态
     * @return
     */
    @PostMapping("updateStatus")
    public Result  updateStatus(@RequestBody UpdateStatusVo updateStatusVo, @RequestParam String curUid){
        // 根据 id  修改用户状态   角色
        boolean b = userService.updateStatusById(updateStatusVo.getUid() ,updateStatusVo.getRole(),updateStatusVo.getUploadSize(), updateStatusVo.getStatus());
        User user = userService.getUserById(curUid);
        if (b) {
            logService.insertLog(user.getUid(), user.getRole(), "成功，修改用户状态");
            return  Result.success(200 , "修改用户状态成功");
        }
        logService.insertLog(user.getUid(), user.getRole(), "失败，修改用户状态");
        return  Result.fail("修改失败");
    }


    @PostMapping("delUser")
    public Result delUser(@RequestBody UpdateStatusVo updateStatusVo, @RequestParam String curUid){
        Integer uid = updateStatusVo.getUid();
        User removedUser = userService.getUserById(uid.toString());
        boolean b = userService.removeUserById(uid);
        User user = userService.getUserById(curUid);
        if (b) {
            logService.insertLog(user.getUid(), user.getRole(), "成功，删除用户：" + removedUser.getUsername() + "，被删用户id：" + removedUser.getUid());
            return Result.success(200 , "删除成功");
        }
        logService.insertLog(user.getUid(), user.getRole(), "失败，删除用户：" + removedUser.getUsername() + "，被删用户id：" + removedUser.getUid());
        return Result.fail(200 , "删除失败");
    }

    // TODO 目前不需要，还未加入日志
    @PostMapping("insertUser")
    public Result insertUser(@RequestBody InsertUserVo user) throws ParseException {
        boolean b = userService.insertUser(user);
        if (b) return Result.success(200 , "删除成功");
        return Result.fail(200 , "删除失败");
    }


    // 忘记密码功能
    @GetMapping("/queryQuestions")
    public Result  forgotPwd(@RequestParam String username){
        User user = userService.getUserByName(username);
        String answer1 = user.getAnswer1().split(":")[0];
        String answer2 = user.getAnswer2().split(":")[0];
        String answer3 = user.getAnswer3().split(":")[0];
        List<String> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        System.out.println(Result.success(200, "查询用户密保问题成功",answers ).toString());
        return Result.success(200, "查询用户密保问题成功",answers );
    }


    // 验证问题
    @PostMapping("/verify")
    public Result verify(@RequestBody VerifyUserQ verifyUserQ){
        // 用户名   密保问题 和 答案
        QueryWrapper queryWrapper = new QueryWrapper<>()
                .eq("username",verifyUserQ.getUsername())
                .eq("answer_1" , verifyUserQ.getQ1()).eq("answer_2" , verifyUserQ.getQ2()).eq("answer_3" , verifyUserQ.getQ3());
        User user = userService.getOne(queryWrapper);


        if (user == null){
            return Result.fail("验证失败");
        }else {
             return Result.success(200 ," 验证成功，请重置密码");
        }

    }

    @PostMapping("updatePwd")
    public Result  updatePwd(@RequestBody UserPwd user){
        User updatedUser = userService.getUserByName(user.getUsername());
        String password = user.getPassword();
        String sha256 = SecurityUtil.hashDataSHA256(password);
        user.setPassword(sha256);
        System.out.println(user);
        userService.updatePwd(user);
        return Result.success(200 , "修改密码成功");
    }




    @GetMapping("/getUserList")
    public Result getUserList() {
        return Result.success(200,"读取成功",userService.list());
    }

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String searchUser
    ){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like(StringUtils.isNotBlank(searchUser),"username",searchUser);
        PageInfo<User> pageInfo = userService.findByPageService(pageNum, pageSize,queryWrapper);

        return Result.success(pageInfo);
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody Map<String,String> user, @RequestParam String curUid) {

        QueryWrapper queryWrapper = new QueryWrapper();
        String username = user.get("username");
        queryWrapper.eq("username",username);

        User existUser = userService.getOne(queryWrapper);

        if (existUser != null){
            return Result.fail(500,"用户名已存在");
        }
        String pwd = user.get("password");
        // 对密码进行加密处理
        String password = SecurityUtil.hashDataSHA256(pwd);
        Date tempDate= new Date();
        User tempUser = new User();
        tempUser.setUsername(username);
        tempUser.setPassword(password);
        tempUser.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tempDate));
        tempUser.setUpdateTime(null);
        tempUser.setRole(1);
        userService.save(tempUser);

        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加一个用户。添加用户的用户名：" + username);
        return Result.success(200,"新增用户成功！");

    }

    @GetMapping("/delete/{uid}")
    public Result deleteUser(@PathVariable int uid, @RequestParam String curUid) {
        User user = userService.getUserById(String.valueOf(uid));

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        userService.remove(queryWrapper);

        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除一个用户。被删用户的用户名：" + user.getUsername());
        return Result.success(200,"删除用户成功！");
    }

    @GetMapping("/getInfo/{uid}")
    public Result getUserInfo(@PathVariable int uid, @RequestParam String curUid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        User tempuser =  userService.getOne(queryWrapper);


        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，查询一个用户。被查询用户的用户名：" + tempuser.getUsername());
        return Result.success(200,"获取用户信息成功！",tempuser);
    }

    @PostMapping("/edit")
    public Result getUserInfo(@RequestBody User user, @RequestParam String curUid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",user.getUid());
        user.setPassword(SecurityUtil.hashDataSHA256(user.getPassword()));
        userService.update(user,queryWrapper);


        User curUser = userService.getUserById(curUid);
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，修改密码。被修改用户的用户名：" + user.getUsername());
        return Result.success(200,"更新用户信息成功！");
    }

}
