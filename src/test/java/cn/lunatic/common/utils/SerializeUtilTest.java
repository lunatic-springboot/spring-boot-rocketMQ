package cn.lunatic.common.utils;

import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SerializeUtilTest {

    @Test
    public void serialize() {
        User user = new User();
        user.setId(1);
        user.setName("Xmnishi上你号是啊花生豆啊到时间段欧艾斯讲好多");

        Map<String, String> m = new TreeMap<>();
        m.put("你是说", "大傻逼");
        List<Map<String, String>> list = new ArrayList<>();
        list.add(m);
        user.setList(list);

        long l = System.currentTimeMillis();
        byte[] bytes = null;
        for ( int i = 0; i < 10; i++ ) {
            bytes = SerializeUtil.serialize(user, SerializeUtil.SerializeTypeEnum.JSON);
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - l) + ",,,Size:" + bytes.length);

        long l2 = System.currentTimeMillis();
        User p = SerializeUtil.deserialize(bytes, SerializeUtil.SerializeTypeEnum.JSON, User.class);
        System.out.println("耗时:" + (System.currentTimeMillis() - l2));

        l = System.currentTimeMillis();
        for ( int i = 0; i < 10; i++ ) {
            bytes = SerializeUtil.serialize(user, SerializeUtil.SerializeTypeEnum.JAVA_SERIALIZE);
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - l) + ",,,Size:" + bytes.length);
        l2 = System.currentTimeMillis();
        p = SerializeUtil.deserialize(bytes, SerializeUtil.SerializeTypeEnum.JAVA_SERIALIZE, User.class);
        System.out.println("耗时:" + (System.currentTimeMillis() - l2));
    }
}

@Data
class User implements Serializable {
    private int id;
    private String name;
    private List<Map<String, String>> list;
}