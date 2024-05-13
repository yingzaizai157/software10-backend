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
import com.cqupt.software_10.mapper.user.UserMapper;
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
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户所有信息
     */
    //个人中心开始
    @GetMapping("/getmessage/{uid}")
    public Result<List<User>> getall(@PathVariable("uid") String uid){
        User user = userMapper.selectById(uid);
//        user.setPassword(null);
        System.out.println("user info"+user);
        return Result.success(200,"获取成功",user);
    }

    //修改密码，根据用户名匹配密码是否正确
    @PostMapping("/VerifyPas")
    public Result VerifyPas(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");
        String pwd = SecurityUtil.hashDataSHA256(password);
        System.out.println(username);
        User user = userMapper.getUerByUserName(username);


        if(!pwd.equals(user.getPassword())){
            return Result.success(200,"密码不匹配",false);
        }
        return Result.success(200,"密码匹配",true);
    }


    //修改密码
    @PostMapping("/updatePas")
    public Result updatePas(@RequestBody Map<String, String> requests) {
        try {
            // 假设 userMapper 是 MyBatis 的一个 Mapper 接口
            String username = requests.get("username");
            String password = requests.get("password");
            String pwd = SecurityUtil.hashDataSHA256(password);
            UserPwd user = new UserPwd(username, pwd);
            int updatedRows =  userMapper.updatePwd(user);

            //  操作日志记录
            QueryWrapper queryWrapper1  = new QueryWrapper<>();
            queryWrapper1.eq("username",username);

            User one = userService.getOne(queryWrapper1);
            String uid = one.getUid();
            User user_log = userService.getUserById(uid);
            if (updatedRows > 0) {
                logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "更新成功");
                // 更新成功，返回成功结果
                return Result.success(200, "更新成功");
            } else {
                logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "更新失败，用户不存在或密码未更改");
                // 更新失败，没有记录被更新
                return Result.success(404, "更新失败，用户不存在或密码未更改");
            }
        } catch (Exception e) {
            String username = requests.get("username");
            QueryWrapper queryWrapper1  = new QueryWrapper<>();
            queryWrapper1.eq("username",username);
            User one = userService.getOne(queryWrapper1);

            String uid = one.getUid();
            User user_log = userService.getUserById(uid);
            logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "用户修改个人信息成功");

            // 处理可能出现的任何异常，例如数据库连接失败等
            // 记录异常信息，根据实际情况决定是否需要发送错误日志
            // 这里返回一个通用的错误信息
            return Result.success(400, "更新失败，发生未知错误");
        }
    }

    //修改个人信息
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        try {
            // 假设 userMapper 是 MyBatis 的一个 Mapper 接口

            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("uid",user.getUid());

            int updatedRows = userMapper.update(user, wrapper);
            //  操作日志记录
            User user_log = userService.getUserById(user.getUid());


            if (updatedRows > 0) {
                // 更新成功，返回成功结果
                logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "用户修改个人信息成功");
                return Result.success("200", "更新成功");
            } else {
                logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "用户修改个人信息失败");
                // 更新失败，没有记录被更新
                return Result.success("404", "更新失败，用户不存在");
            }
        } catch (Exception e) {
            // 处理可能出现的任何异常，例如数据库连接失败等
            // 记录异常信息，根据实际情况决定是否需要发送错误日志
            // 这里返回一个通用的错误信息
            return Result.success("400", "更新失败，发生未知错误");
        }
    }

    /**
     *  检查用户名是否重复
     * @param username
     * @return
     */
    @GetMapping("/checkRepetition/{username}")
    public Result checkRepetition(@PathVariable("username") String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if(user != null){
            return Result.success(200, "用户名已存在");
        }else {
            return Result.success(200, "用户名可用");
        }
    }
    //个人中心结束

    @GetMapping("/querUserNameExist")
    public Result querUserNameExist(@RequestParam String userName){
        User existUser = userService.getUserByName(userName);
        if (existUser != null){
            return Result.fail(400,"用户已经存在",null);
        }else{
            return Result.success(200, "用户名可用" , null);
        }

    }

    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user) throws ParseException {

        System.out.println(user);
        // 检查用户名是否已经存在
        user.setUid("0");
        User existUser = userService.getUserByName(user.getUsername());
        if (existUser != null){
            return Result.success(400, "用户已经存在", null);
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
//        User user_log = userService.getUserById(curUid);
//        logService.insertLog(user_log.getUid(), user_log.getRole(), user_log.getUsername() + "账户注册成功");


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
        if(code==null) return Result.fail(400,"验证码已过期！");
        if(user.getCode()==null || !user.getCode().equals(code)) {
            return Result.fail(501, "验证码错误!");
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
                return Result.fail(502,"账户未激活！");
            }else if(userStatus.equals("2")){
                return Result.fail(503,"用户已被禁用!");
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
                System.out.println("================================" + token);
                return Result.success(200, token, getUser);
            }else {
                logService.insertLog(getUser.getUid(), getUser.getRole(), getUser.getUsername() + "登录失败");
                return Result.fail(504,"密码错误请重新输入",null);
            }
        }else {
            return Result.fail(505,"用户不存在",null);
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
    public Result  updateStatus(@RequestBody UpdateStatusVo updateStatusVo, HttpServletRequest request){
        // 根据 id  修改用户状态   角色
        boolean b = userService.updateStatusById(updateStatusVo.getUid() ,updateStatusVo.getRole(),updateStatusVo.getUploadSize(), updateStatusVo.getStatus());


        String token = request.getHeader("Authorization");
        String uid = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(uid);

        if (b) {
            logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，修改用户状态");
            return  Result.success(200 , "修改用户状态成功");
        }
        logService.insertLog(curUser.getUid(), curUser.getRole(), "失败，修改用户状态");
        return  Result.fail("修改失败");
    }


    @PostMapping("delUser")
    public Result delUser(@RequestBody UpdateStatusVo updateStatusVo, HttpServletRequest request){
        Integer uid = updateStatusVo.getUid();
        User removedUser = userService.getUserById(uid.toString());
        boolean b = userService.removeUserById(uid);


        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        if (b) {
            logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除用户：" + removedUser.getUsername() + "，被删用户id：" + removedUser.getUid());
            return Result.success(200 , "删除成功");
        }
        logService.insertLog(curUser.getUid(), curUser.getRole(), "失败，删除用户：" + removedUser.getUsername() + "，被删用户id：" + removedUser.getUid());
        return Result.fail(200 , "删除失败");
    }

    // TODO 目前不需要，还未加入日志
    @PostMapping("insertUser")
    public Result insertUser(@RequestBody InsertUserVo user, HttpServletRequest request) throws ParseException {
        boolean b = userService.insertUser(user);

        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        if (b) {
            logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加用户：" + user.getUsername());
            return Result.success(200 , "添加成功");
        }
        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加用户：" + user.getUsername());
        return Result.fail(200 , "添加失败");
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
    public Result  updatePwd(@RequestBody UserPwd user, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        String password = user.getPassword();
        String sha256 = SecurityUtil.hashDataSHA256(password);
        user.setPassword(sha256);
        System.out.println(user);
        userService.updatePwd(user);

//        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，修改密码：" + user.getUsername());
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
    public Result addUser(@RequestBody Map<String,String> user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);


        QueryWrapper queryWrapper = new QueryWrapper();
        String username = user.get("username");
        queryWrapper.eq("username",username);

        User existUser = userService.getOne(queryWrapper);

        if (existUser != null){
            return Result.fail(400,"用户名已存在");
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

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加一个用户。添加用户的用户名：" + username);
        return Result.success(200,"新增用户成功！");

    }

    @GetMapping("/delete/{uid}")
    public Result deleteUser(@PathVariable int uid, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        User user = userService.getUserById(String.valueOf(uid));

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        userService.remove(queryWrapper);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除一个用户。被删用户的用户名：" + user.getUsername());
        return Result.success(200,"删除用户成功！");
    }

    @GetMapping("/getInfo/{uid}")
    public Result getUserInfo(@PathVariable int uid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",uid);
        User tempuser =  userService.getOne(queryWrapper);

        return Result.success(200,"获取用户信息成功！",tempuser);
    }

    @PostMapping("/edit")
    public Result getUserInfo(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",user.getUid());
        user.setPassword(SecurityUtil.hashDataSHA256(user.getPassword()));
        userService.update(user,queryWrapper);


        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，修改用户信息。被修改用户的用户名：" + user.getUsername());
        return Result.success(200,"更新用户信息成功！");
    }

    // 新增可共享用户列表
    @GetMapping("/getTransferUserList")
    public Result getTransferUserList(@RequestParam("uid") String uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("uid", uid);
        List<User> userList = userMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (User user : userList) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", user.getUid());
            resultMap.put("label", user.getUsername());
            resultList.add(resultMap);
        }
        return  Result.success(200,"获得成功",resultList);
    }


}
