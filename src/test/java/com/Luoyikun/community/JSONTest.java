package com.Luoyikun.community;


import com.Luoyikun.community.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class JSONTest {

    @Test
    public void test01(){
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", 1);
        map.put("likeStatus",2);
        String jsonString = CommunityUtil.getJSONString(0, null, map);
        System.out.println(jsonString);
    }
}
