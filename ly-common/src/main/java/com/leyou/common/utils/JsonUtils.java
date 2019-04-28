package com.leyou.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: JiangGuangming
 * 实现实体类与Json的转换
 **/
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     *把一个对象序列化为String类型，包含1个参数
     * @param obj  原始java对象
     * @return  json串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    /**
     * 将json串反序列化为实体类
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 把一个json反序列化为List类型，需要指定集合中元素类型，包含两个参数
     * @param json  要反序列化的json字符串
     * @param eClass  集合中元素类型
     * @return List类型
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 把一个json反序列化为Map类型，需要指定集合中key和value类型，包含三个参数
     * @param json  要反序列化的json字符串
     * @param kClass  集合中key的类型
     * @param vClass  集合中value的类型
     * @param <K>
     * @param <V>
     * @return   Map集合
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 把json字符串反序列化，当反序列化的结果比较复杂时，通过这个方法转换
     * @param json  要反序列化的json字符串
     * @param type  在传参时，需要传递TypeReference的匿名内部类，
     *              把要返回的类型写在TypeReference的泛型中，则返回的就是泛型中类型
     * @param <T>
     * @return
     */
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }
}
