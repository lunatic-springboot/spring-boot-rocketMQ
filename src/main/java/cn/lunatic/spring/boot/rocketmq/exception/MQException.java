package cn.lunatic.spring.boot.rocketmq.exception;

/**
 * MQ异常定义
 *
 * @author ganlunatic
 * @date 2018/12/22
 */
public class MQException extends RuntimeException {
    public MQException(String message, Throwable cause) {
        super(message, cause);
    }
}
