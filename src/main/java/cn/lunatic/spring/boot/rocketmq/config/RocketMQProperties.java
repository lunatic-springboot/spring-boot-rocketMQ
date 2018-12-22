package cn.lunatic.spring.boot.rocketmq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 读取配置文件信息
 *
 * @author ganlunatic
 */
@ConfigurationProperties(prefix = "lunatic.rocketmq")
@Configuration
@Getter
@Setter
public class RocketMQProperties implements Serializable {

    private static final long serialVersionUID = -484821303443538121L;
    /**
     * namesrv地址
     */
    private String namesrvAddr;
    /**
     * 生产者group名称
     */
    private String producerGroupName;
    /**
     * 事务生产者group名称
     */
    private String transactionProducerGroupName;
    /**
     * 消费者group名称
     */
    private String consumerGroupName;
    /**
     * 生产者实例名称
     */
    private String producerInstanceName;
    /**
     * 消费者实例名称
     */
    private String consumerInstanceName;
    /**
     * 事务生产者实例名称
     */
    private String producerTranInstanceName;
    /**
     * 广播消费
     */
    private boolean consumerBroadcasting;
    /**
     * 消息序列化方式:1-json;2-Java原生，默认Json
     */
    private int serializeType;
}
