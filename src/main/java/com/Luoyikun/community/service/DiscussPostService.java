package com.Luoyikun.community.service;

import com.Luoyikun.community.dao.DiscussPostMapper;
import com.Luoyikun.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    //获得某个用户的帖子
    public List<DiscussPost> findDiscussPosts(int uerId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(uerId,offset,limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
