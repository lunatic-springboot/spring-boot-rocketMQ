package cn.lunatic.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 序列化工具类
 *
 * @author ganluantic
 * @date 2018/12/22
 */
@Slf4j
public class SerializeUtil {
    /**
     * 序列化对象
     *
     * @param obj           对象
     * @param serializeType 序列化类型
     * @return
     */
    public static byte[] serialize(Object obj, SerializeTypeEnum serializeType) {
        if (SerializeTypeEnum.JSON == serializeType) {
            return jsonSerialize(obj);
        } else if (SerializeTypeEnum.JAVA_SERIALIZE == serializeType) {
            return javaSerialize(obj);
        } else {
            log.error("不支持的序列化方式：{}", serializeType);
            return null;
        }
    }

    public static <T> T deserialize(byte[] bytes, SerializeTypeEnum serializeType, Class<T> clazz) {
        if (SerializeTypeEnum.JSON == serializeType) {
            return jsonDeserialize(bytes, clazz);
        } else if (SerializeTypeEnum.JAVA_SERIALIZE == serializeType) {
            return javaDeserialize(bytes, clazz);
        } else {
            log.error("不支持的序列化方式：{}", serializeType);
            return null;
        }
    }

    private static byte[] javaSerialize(Object obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("java序列化对象异常,{}", obj, e);
            return null;
        }
    }

    private static <T> T javaDeserialize(byte[] bytes, Class<T> clazz) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new java.io.ByteArrayInputStream(bytes)));
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            log.error("java反序列化对象异常,clazz={}", clazz, e);
            return null;
        }
    }


    private static byte[] jsonSerialize(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("Json序列化对象异常,{}", obj, e);
            return null;
        }
    }

    private static <T> T jsonDeserialize(byte[] bytes, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("json反序列化对象异常,clazz={}", clazz, e);
            return null;
        }
    }


    @Getter
    public enum SerializeTypeEnum {
        JSON(1, "Json序列化"), JAVA_SERIALIZE(2, "Java原生序列化");
        private int codeId;
        private String codeName;

        SerializeTypeEnum(int codeId, String codeName) {
            this.codeId = codeId;
            this.codeName = codeName;
        }

        public static SerializeTypeEnum parse(int serializeType) {
            for ( SerializeTypeEnum typeEnum : values() ) {
                if (typeEnum.codeId == serializeType) {
                    return typeEnum;
                }
            }
            return JAVA_SERIALIZE;
        }
    }
}
