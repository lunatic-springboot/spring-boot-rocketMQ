package cn.lunatic.spring.boot.rocketmq.consumer;

import cn.lunatic.spring.boot.rocketmq.bean.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQConsumerTest extends MQConsumer<UserInfo> {
    @Override
    protected String getTopic() {
        return "user-topic-test";
    }

    @Override
    public boolean onMessage(UserInfo messge) {
        log.info("[Consumer Message] {}", messge);
        return false;
    }
}