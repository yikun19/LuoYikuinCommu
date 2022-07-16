package com.Luoyikun.community;

import com.Luoyikun.community.dao.DiscussPostMapper;
import com.Luoyikun.community.dao.LoginTicketMapper;
import com.Luoyikun.community.dao.UserMapper;
import com.Luoyikun.community.entity.LoginTicket;
import com.Luoyikun.community.entity.User;
import com.Luoyikun.community.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(1111);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User newUser = new User();
        newUser.setUsername("罗怡坤");
        newUser.setPassword("luo739663202");
        newUser.setSalt("abc");
        newUser.setEmail("739663202@qq.com");
        newUser.setHeaderUrl("http://www.nowcoder.com/101.png");

        int rows = userMapper.insertUser(newUser);

        System.out.println(rows);
        System.out.println(newUser.getId());
    }

    @Test
    public void testLoginTicketInsert() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setId(11);
        loginTicket.setUserId(101);
        loginTicket.setTicket("asdasd");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testLoginTicketUpdate() {
        loginTicketMapper.updateStatus("asdasd", 1);
    }

    @Test
    public void testLoginTicketSelect() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("asdasd");
        System.out.println(loginTicket);
    }

    @Test
    public void PasswordGenerate() {
        User user = userMapper.selectByName("luoyikun");
        String salt = user.getSalt();
        String password = CommunityUtil.md5("321" + salt);
        System.out.println(password);
    }
}
