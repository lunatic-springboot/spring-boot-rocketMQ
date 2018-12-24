package cn.lunatic.spring.boot.rocketmq.producer;

import cn.lunatic.spring.boot.rocketmq.Application;
import cn.lunatic.spring.boot.rocketmq.bean.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MQProducerTest {
    @Resource
    private MQProducer producer;

    @Test
    public void send() {
        for ( int i = 0; i < 5; i++ ) {
            UserInfo userInfo = UserInfo.builder().id(i).username("Test-UserName" + i).build();
            producer.send(userInfo, "topic-test", "topic-test-user");
        }
    }
}