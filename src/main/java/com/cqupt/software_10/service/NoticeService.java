package com.cqupt.software_10.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.cqupt.software_10.entity.InsertNoticeVo;
import com.cqupt.software_10.entity.Notification;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface NoticeService extends IService<Notification> {
    PageInfo<Notification> allNotices(Integer pageNum, Integer pageSize);

    void saveNotification(InsertNoticeVo notification);

    List<Notification> queryNotices();

}
