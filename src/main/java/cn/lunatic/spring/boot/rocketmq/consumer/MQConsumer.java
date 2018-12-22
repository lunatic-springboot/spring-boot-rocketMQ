package cn.lunatic.spring.boot.rocketmq.consumer;

import cn.lunatic.common.utils.SerializeUtil;
import cn.lunatic.spring.boot.rocketmq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Consumer基类
 *
 * @author ganlunatic
 * @date 2018/12/22
 */
@Slf4j
public abstract class MQConsumer<T> {
    @Autowired
    protected RocketMQProperties rocketMQProperties;
    /**
     * Topic
     *
     * @return
     */
    protected abstract String getTopic();

    /**
     * tags
     *
     * @return
     */
    protected String getTags() {
        return "*";
    }

    /**
     * 是否顺序消费
     *
     * @return
     */
    protected boolean isOrderlyFlag() {
        return false;
    }

    /**
     * 消息处理
     *
     * @param messge
     * @return
     */
    public abstract boolean onMessage(T messge);

    @PostConstruct
    public void initConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMQProperties.getConsumerGroupName());
        consumer.setNamesrvAddr(rocketMQProperties.getNamesrvAddr());
        consumer.setInstanceName(rocketMQProperties.getConsumerInstanceName());
        // 判断是否是广播模式
        if (rocketMQProperties.isConsumerBroadcasting()) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        // 设置批量消费
        consumer.setConsumeMessageBatchMaxSize(1);
        // 设置主题Topic和标签Tags
        try {
            consumer.subscribe(getTopic(), getTags());
        } catch (MQClientException e) {
            log.error("[MQ Consumer] Subscribe Exception", e);
            throw new RuntimeException("[MQ Consumer] Subscribe Exception", e);
        }
        if (isOrderlyFlag()) {
            // 顺序消费
            registerOrderlyListener(consumer);
        } else {
            // 普通并发消费
            registerNormalListener(consumer);
        }
        try {
            consumer.start();
        } catch (MQClientException e) {
            log.error("[MQ Consumer] Start Exception", e);
            throw new RuntimeException("[MQ Consumer] Start Exception", e);
        }
        log.warn("[MQ Consumer] Start ... ...");
    }

    private void registerNormalListener(MQPushConsumer consumer) {
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt messageExt = msgs.get(0);
            log.info("[MQ Consumer] messageExt={}", messageExt);
            boolean consumerFlag;
            try {
                Object message = SerializeUtil.deserialize(messageExt.getBody(), SerializeUtil.SerializeTypeEnum.parse(rocketMQProperties.getSerializeType()), Object.class);
                consumerFlag = onMessage((T) message);
            } catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            if (consumerFlag) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
    }

    private void registerOrderlyListener(MQPushConsumer consumer) {
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            context.setAutoCommit(true);
            MessageExt messageExt = msgs.get(0);
            log.info("[MQ Consumer] messageExt={}", messageExt);
            boolean consumerFlag;
            try {
                Object message = SerializeUtil.deserialize(messageExt.getBody(), SerializeUtil.SerializeTypeEnum.parse(rocketMQProperties.getSerializeType()), Object.class);
                consumerFlag = onMessage((T) message);
            } catch (Exception e) {
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
            if (consumerFlag) {
                return ConsumeOrderlyStatus.SUCCESS;
            } else {
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        });
    }
}
