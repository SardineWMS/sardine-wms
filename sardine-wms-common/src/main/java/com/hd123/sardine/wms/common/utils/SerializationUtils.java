/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	SerializationUtils.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-24 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.exception.SerializeException;

/**
 * 对象的序列化和反序列化
 * <p>
 * 当调用不符合方法定义时，抛出{@link IllegalArgumentException}。
 * 
 * @author zhangsai
 * 
 */
public class SerializationUtils {
    private static ObjectMapper mapper = new ObjectMapper()
            .configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

    /**
     * 序列化对象
     * 
     * @param object
     *            要序列化的对象，not null。
     * @return 对象序列化后生成的json字符串
     * @throws SerializeException
     * @throws IllegalArgumentException
     */
    public static String serialize(Object object) {
        if (object == null)
            throw new IllegalArgumentException("序列化对象不能为空");
        try {
            String jsondata = mapper.writeValueAsString(object);
            return jsondata;
        } catch (Exception e) {
            throw new SerializeException(e, "对象(" + object.getClass().getName() + ")序列化过程出错。");
        }
    }

    /**
     * 反序列化对象。
     * 
     * @param <T>
     *            类型
     * @param source
     *            json字符串，not null
     * @param targetClass
     *            对象class，not null。
     * @return 返回反序列化后的对象
     * @throws SerializeException
     * @throws IllegalArgumentException
     */
    public static <T> T deserialize(String source, Class<T> targetClass) {
        if (StringUtil.isNullOrEmpty(source))
            throw new IllegalArgumentException("json字符串不能为空");
        if (targetClass == null)
            throw new IllegalArgumentException("targetClass不能为空");
        try {
            return mapper.readValue(source, targetClass);
        } catch (Exception e) {
            throw new SerializeException(e, "类(" + targetClass.getName() + "反序列化出错。");
        }
    }

    /**
     * 反序列化集合对象。
     * 
     * @param <T>
     *            集合类型，not null。
     * @param source
     *            反序列化的json字符串，not null。
     * @param ref
     *            类型
     * @return 集合对象
     * @throws SerializeException
     *             反序列化出错时抛出异常
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String source, TypeReference<T> ref) {
        if (StringUtil.isNullOrEmpty(source))
            throw new IllegalArgumentException("json字符串不能为空");
        if (ref == null)
            throw new IllegalArgumentException("TypeReference不能为空");

        try {
            return (T) mapper.readValue(source, ref);
        } catch (Exception e) {
            throw new SerializeException(e, "类(" + ref.getType().getClass().getName() + "反序列化出错。");
        }
    }
}
