package com.Luoyikun.community.controller;


import com.Luoyikun.community.entity.Message;
import com.Luoyikun.community.entity.Page;
import com.Luoyikun.community.entity.User;
import com.Luoyikun.community.service.MessageService;
import com.Luoyikun.community.service.UserService;
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
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/message/list", method = RequestMethod.GET)
    public String getMessageList(Model model, Page page) {
        User user = hostHolder.getUser();
        page.setPath("/message/list");
        page.setLimit(5);
        page.setRows(messageService.findConversationCount(user.getId()));
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        for(Message message : conversationList) {
            if(message != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation",message);
                map.put("messageCount",messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                int targetUserId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("targetUser", userService.findUserById(targetUserId));

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

        int totalUnreadMsgCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("totalUnreadMsgCount",totalUnreadMsgCount);
        return "/site/letter";
    }

    @RequestMapping(path="/message/detail/{conversationId}")
    //查询message详情
    public String getMessageDetail(@PathVariable("conversationId") String conversationId, Model model, Page page) {

        User user = hostHolder.getUser();
        page.setPath("/message/detail/" + conversationId);
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        List<Message> conversations = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messages = new ArrayList<>();
        for(Message message : conversations) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", message);
            map.put("sender", userService.findUserById(message.getFromId()));
            messages.add(map);
        }
        model.addAttribute("messages", messages);

        model.addAttribute("target", getLetterTarget(conversationId));

        List<Integer> ids = getMessageIds(conversations);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    private List<Integer> getMessageIds(List<Message> messageList) {
        List<Integer> ids = new ArrayList<>();

        if (messageList != null) {
            for (Message message : messageList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(path = "/message/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName, String content) {
        User ToUser = userService.findUserByName(toName);
        if(ToUser == null) {
            return CommunityUtil.getJSONString(1,"目标用户不存在！");
        }
        Message message = new Message();
        message.setContent(content);
        int fromId = hostHolder.getUser().getId();
        int toId = ToUser.getId();
        message.setFromId(fromId);
        message.setToId(toId);
        if(toId < fromId) {
            message.setConversationId(toId + "_" + fromId);
        } else {
            message.setConversationId(fromId + "_" + toId);
        }
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }
}
