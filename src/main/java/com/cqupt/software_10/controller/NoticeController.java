package com.cqupt.software_10.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.NoticeMapper;
import com.cqupt.software_10.entity.InsertNoticeVo;
import com.cqupt.software_10.entity.Notification;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.LogService;
import com.cqupt.software_10.service.NoticeService;
import com.cqupt.software_10.service.user.UserService;
import com.cqupt.software_10.util.SecurityUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;

    @GetMapping("/allNotices")
    public PageInfo<Notification> allNotices(@RequestParam Integer pageNum , @RequestParam Integer pageSize){
        return noticeService.allNotices(pageNum, pageSize);
    }

    @GetMapping("/queryNotices")
    public List<Notification> queryNotices(){
        return noticeService.queryNotices();
    }




    @PostMapping("/updateNotice")
    public Result updateNotice(@RequestBody Notification notification, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        notification.setUpdateTime(new Date());
        noticeService.saveOrUpdate(notification);

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，更新通知：" + notification);
        return Result.success(200, "成功", null);
    }


    @PostMapping("delNotice")
    public Result delNotice(@RequestBody Notification notification, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

        noticeService.removeById(notification.getInfoId());

        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，删除通知：" + notification);
        return Result.success(200, "成功", null);
    }

    @PostMapping("insertNotice")
    public Result insertNotice(@RequestBody InsertNoticeVo notification, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String curId = SecurityUtil.getUserIdFromToken(token);
        User curUser = userService.getUserById(curId);

//        private String infoId;
//        private String uid;
//        private String username;
//        private Date createTime;
//        private String title;
//        private String content;
//        private Date updateTime;
//        private String isDelete;
//        private String type;
        Notification note = new Notification();

//        noticeService.saveNotification(notification);
        User user = userMapper.selectByUid(notification.getUid()+"");
        note.setInfoId(new Random().nextInt() + "");
        note.setUid(user.getUid());
        note.setUid(user.getUsername());
        note.setCreateTime(new Date());
        note.setTitle(notification.getTitle());
        note.setContent(notification.getContent());
        note.setIsDelete("0");
        noticeMapper.insert(note);


        logService.insertLog(curUser.getUid(), curUser.getRole(), "成功，添加通知：" + notification);
        return Result.success(200 , "成功", null);
    }


}
