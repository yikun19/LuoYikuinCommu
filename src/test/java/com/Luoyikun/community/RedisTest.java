package com.Luoyikun.community;

import com.Luoyikun.community.service.LikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private LikeService likeService;

    @Test
    public void test01() {
        int likeStatus = likeService.findEntityLikeStatus(1120, 1, 280);
        System.out.println(likeStatus);
    }
}
