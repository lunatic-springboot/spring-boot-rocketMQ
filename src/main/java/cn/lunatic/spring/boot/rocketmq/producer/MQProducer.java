package cn.lunatic.spring.boot.rocketmq.producer;

import cn.lunatic.common.utils.SerializeUtil;
import cn.lunatic.spring.boot.rocketmq.config.RocketMQProperties;
import cn.lunatic.spring.boot.rocketmq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

/**
 * MQ Producer
 *
 * @author ganlunatic
 * @date 2018/12/22
 */
@Slf4j
@Component
public class MQProducer {
    /**
     * RocketMQProperties
     */
    private RocketMQProperties rocketMQProperties;
    /**
     * Producer
     */
    private DefaultMQProducer defaultProducer;

    public MQProducer(RocketMQProperties rocketMQProperties) {
        log.info("[MQ Producer]Init{}", rocketMQProperties);
        this.rocketMQProperties = rocketMQProperties;
        defaultProducer = new DefaultMQProducer(rocketMQProperties.getProducerGroupName());
        defaultProducer.setNamesrvAddr(rocketMQProperties.getNamesrvAddr());
        defaultProducer.setInstanceName(rocketMQProperties.getProducerInstanceName());
        defaultProducer.setVipChannelEnabled(false);
        defaultProducer.setRetryTimesWhenSendAsyncFailed(10);
        try {
            defaultProducer.start();
        } catch (MQClientException e) {
            log.error("[MQ Producer] Start Exception", e);
            throw new RuntimeException("[MQ Producer] Start Exception", e);
        }
        log.warn("[MQ Producer] Start ... ...");
    }


    public void send(Object message, String topic, String tags) {
        Message msg = new Message(topic, tags, SerializeUtil.serialize(message, SerializeUtil.SerializeTypeEnum.parse(rocketMQProperties.getSerializeType())));
        try {
            SendResult result = defaultProducer.send(msg);
            log.info("[MQ Producer] Send Message {}", result);
        } catch (Exception e) {
            throw new MQException("[MQ Producer] Send Exception", e);
        }
    }

    public void send(Object message, String topic) {
        Message msg = new Message(topic, SerializeUtil.serialize(message, SerializeUtil.SerializeTypeEnum.parse(rocketMQProperties.getSerializeType())));
        try {
            SendResult result = defaultProducer.send(msg);
            log.info("[MQ Producer] Send Message {}", result);
        } catch (Exception e) {
            throw new MQException("[MQ Producer] Send Exception", e);
        }
    }
}
