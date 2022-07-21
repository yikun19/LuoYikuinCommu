package com.Luoyikun.community.controller;

import com.Luoyikun.community.entity.User;
import com.Luoyikun.community.service.LikeService;
import com.Luoyikun.community.util.CommunityUtil;
import com.Luoyikun.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId) {
        User user = hostHolder.getUser();
        //点赞
        likeService.like(user.getId(),entityType,entityId);
        //点赞数
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        //点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus",likeStatus);

        return CommunityUtil.getJSONString(0,null, map);
    }
}