package com.Luoyikun.community.controller;

import com.Luoyikun.community.dao.DiscussPostMapper;
import com.Luoyikun.community.entity.Comment;
import com.Luoyikun.community.entity.DiscussPost;
import com.Luoyikun.community.entity.Page;
import com.Luoyikun.community.entity.User;
import com.Luoyikun.community.service.CommentService;
import com.Luoyikun.community.service.DiscussPostService;
import com.Luoyikun.community.service.LikeService;
import com.Luoyikun.community.service.UserService;
import com.Luoyikun.community.util.CommunityConstant;
import com.Luoyikun.community.util.CommunityUtil;
import com.Luoyikun.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if(user == null) {
            return CommunityUtil.getJSONString(403,"你还没有登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);

        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    //进入某个帖子的详情页
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {

        DiscussPost post = discussPostService.selectDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        //查询该帖子的点赞数
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount",likeCount);

        //查询当前登录用户对于某个帖子的点赞状态
        int likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        //查询该帖子的所有评论列表，List中装的是Comment对象
        List<Comment> commentList = commentService.findCommentByEntityType(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        //构造该帖子的评论列表的Vo(view object)对象
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null) {
            for(Comment comment : commentList) {
                Map<String, Object> commentVo = new HashMap<>();
                //查询该评论的点赞数
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("comment", comment);
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                commentVo.put("likeCount", likeCount);
                commentVo.put("likeStatus", likeStatus);

                //查询某个评论的回复列表
                List<Comment> replyList = commentService.findCommentByEntityType(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                //构造评论的回复列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null) {
                    for(Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        likeStatus = likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount", likeCount);
                        replyVo.put("likeStatus", likeStatus);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        return "site/discuss-detail";
    }


}
