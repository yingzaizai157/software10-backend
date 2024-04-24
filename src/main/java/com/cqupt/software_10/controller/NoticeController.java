package com.cqupt.software_10.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.cqupt.software_10.common.Result;
import com.cqupt.software_10.dao.NoticeMapper;
import com.cqupt.software_10.entity.InsertNoticeVo;
import com.cqupt.software_10.entity.Notification;
import com.cqupt.software_10.entity.user.User;
import com.cqupt.software_10.mapper.user.UserMapper;
import com.cqupt.software_10.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/allNotices")
    public PageInfo<Notification> allNotices(@RequestParam Integer pageNum , @RequestParam Integer pageSize){
        return noticeService.allNotices(pageNum, pageSize);
    }

    @GetMapping("/queryNotices")
    public List<Notification> queryNotices(){
        return noticeService.queryNotices();
    }




    @PostMapping("/updateNotice")
    public Result updateNotice(@RequestBody Notification notification, @RequestParam String curUid){

        notification.setUpdateTime(new Date());
        noticeService.saveOrUpdate(notification);

        return Result.success(200, "成功", null);
    }


    @PostMapping("delNotice")
    public Result delNotice(@RequestBody Notification notification, @RequestParam String curUid){
        noticeService.removeById(notification.getInfoId());
        return Result.success(200, "成功", null);
    }

    @PostMapping("insertNotice")
    public Result insertNotice(@RequestBody InsertNoticeVo notification, @RequestParam String curUid){
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
        return Result.success(200 , "成功", null);
    }


}
