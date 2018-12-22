package cn.lunatic.spring.boot.rocketmq.bean;

import lombok.*;

import java.io.Serializable;

/**
 * @Author 18011618
 * @Date 19:28 2018/7/17
 * @Function 发送消息体
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 6529434871276180234L;
    private Integer id;
    private String username;
    private String pwd;

}
