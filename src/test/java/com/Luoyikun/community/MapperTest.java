package com.Luoyikun.community;

import com.Luoyikun.community.dao.DiscussPostMapper;
import com.Luoyikun.community.dao.UserMapper;
import com.Luoyikun.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

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
}
