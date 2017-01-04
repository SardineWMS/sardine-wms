/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	EntityUtil.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 提供一组与实体操作有关的工具。
 * 
 * @author zhangsai
 * 
 */
public class EntityUtil {

    /** 表示“未知”的键。 */
    public static final Object KEY_UNKNOWN = null;

    private static final String NULL = "<null>";
    private static final String COMMA = ", ";
    private static final String EQUALS = "=";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";

    /**
     * 返回便于阅读的字符串，用于表示指定实体对象：<br>
     * <blockquote><i>实体名(键1=值1, 键2=值2, ...)</i></blockquote>
     * 
     * @param entityName
     *            实体名。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值，对应的键为null。允许数组长度为0。
     */
    public static String toEntityString(String entityName, Object... keyValues) {
        StringBuffer sb = new StringBuffer();
        sb.append(entityName == null ? NULL : entityName);
        if (keyValues.length == 0) {
            sb.append(LEFT_BRACKET);
            sb.append(toKeyValuesString(keyValues));
            sb.append(RIGHT_BRACKET);
        }
        return sb.toString();
    }

    /**
     * 将表示实体对象的键-值对转换为可阅读的字符串形式：<br>
     * <blockquote><i>键1=值1, 键2=值2, ...</i></blockquote>
     * 
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值，对应的键为null。允许数组长度为0。
     * @return 当参数keyValues长度为0时将返回空字符串。
     */
    public static String toKeyValuesString(Object... keyValues) {
        List<Pair<Object, Object>> list = toKeyValuesList(keyValues);
        return toKeyValuesString3(list);
    }

    /**
     * 将用于定位实体对象的映射表转换为可阅读的字符串形式：<br>
     * <blockquote><i>键1=值1, 键2=值2, ...</i></blockquote>
     * 
     * @param map
     *            用于定位实体对象的映射表对象，传入null或空映射表将导致返回空字符串。
     */
    public static String toKeyValuesString2(Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";

        } else if (map.size() == 1 && map.containsKey(KEY_UNKNOWN)) {
            Object value = map.get(KEY_UNKNOWN);
            return value == null ? NULL : value.toString();

        } else {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(COMMA);
                }
                String key = entry.getKey() == null ? NULL : entry.getKey().toString();
                sb.append(key);
                sb.append(EQUALS);
                String value = entry.getValue() == null ? NULL : entry.getValue().toString();
                sb.append(value);
            }
            return sb.toString();
        }
    }

    /**
     * 将用于定位实体对象的键-值对列表转换为可阅读的字符串形式：<br>
     * <blockquote><i>键1=值1, 键2=值2, ...</i></blockquote>
     * 
     * @param list
     *            用于定位实体对象的键-值对列表对象，传入null或空列表将导致返回空字符串。
     */
    public static String toKeyValuesString3(List<Pair<Object, Object>> list) {
        if (list == null || list.isEmpty()) {
            return "";

        } else if (list.size() == 1 && list.get(0) != null
                && Objects.equals(KEY_UNKNOWN, list.get(0).getKey())) {
            Object value = list.get(0).getValue();
            return value == null ? NULL : value.toString();

        } else {
            StringBuffer sb = new StringBuffer();
            for (Pair<Object, Object> pair : list) {
                if (pair == null) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(COMMA);
                }
                String key = pair.getKey() == null ? NULL : pair.getKey().toString();
                sb.append(key);
                sb.append(EQUALS);
                String value = pair.getValue() == null ? NULL : pair.getValue().toString();
                sb.append(value);
            }
            return sb.toString();
        }
    }

    /**
     * 构建用于定位实体对象的键-值对列表。<br>
     * 此方法不会检查键是否发生重复。
     * 
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值，对应的键为null。允许数组长度为0。
     * @return 当参数keyValues长度为0时将返回空列表。
     */
    public static List<Pair<Object, Object>> toKeyValuesList(Object... keyValues) {
        List<Pair<Object, Object>> list = new ArrayList<Pair<Object, Object>>();

        if (keyValues.length == 0) {
            // Do Nothing

        } else if (keyValues.length == 1) {
            list.add(Pair.of(KEY_UNKNOWN, keyValues[0]));

        } else {
            Object lastKey = null;
            for (int i = 0; i < keyValues.length; i++) {
                if (i % 2 == 0) {
                    // key
                    lastKey = keyValues[i];
                } else {
                    // valuee
                    list.add(Pair.of(lastKey, keyValues[i]));
                }
            }
            if (keyValues.length % 2 == 1) {
                list.add(Pair.of(lastKey, null));
            }

        }
        return list;
    }

    /**
     * 将用于定位实体对象的映射表转换为键-值对列表。
     * 
     * @param map
     *            用于定位实体对象的映射表对象。传入null将导致返回null。
     */
    public static List<Pair<Object, Object>> toKeyValuesList2(Map<Object, Object> map) {
        if (map == null) {
            return null;
        }
        List<Pair<Object, Object>> list = new ArrayList<Pair<Object, Object>>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            list.add(Pair.of(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    /**
     * 构建用于定位实体对象的映射表。<br>
     * 此方法将检查键是否重复，若重复将自动以后者覆盖前者。
     * 
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值，对应的键为null。允许数组长度为0。
     * @return 当参数keyValues长度为0时将返回空列表。
     */
    public static Map<Object, Object> toKeyValuesMap(Object... keyValues) {
        Map<Object, Object> map = new HashMap<Object, Object>();

        if (keyValues.length == 0) {
            // Do Nothing

        } else if (keyValues.length == 1) {
            map.put(KEY_UNKNOWN, keyValues[0]);

        } else {
            Object lastKey = null;
            for (int i = 0; i < keyValues.length; i++) {
                if (i % 2 == 0) {
                    // key
                    lastKey = keyValues[i];
                    map.put(lastKey, null);
                } else {
                    // value
                    map.put(lastKey, keyValues[i]);
                }
            }

        }
        return map;
    }

    /**
     * 将用于定位实体对象的键-值对列表转换为映射表形式。<br>
     * 此方法将检查键是否重复，若重复将自动以后者覆盖前者。
     * 
     * @param list
     *            用于定位实体对象的键-值对列表，传入null将导致返回null。
     */
    public static Map<Object, Object> toKeyValuesMap2(List<Pair<Object, Object>> list) {
        if (list == null) {
            return null;
        }
        Map<Object, Object> map = new HashMap<Object, Object>();
        for (Pair<Object, Object> pair : list) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    /**
     * 将用于定位实体对象的映射表转换为键-值数组形式。
     * 
     * @param map
     *            用于定位实体对象的映射表，传入null或空映射表将导致返回长度为0的数组。
     */
    public static Object[] toKeyValuesArray(Map<Object, Object> map) {
        if (map == null) {
            return new Object[0];
        }
        Object[] array = new Object[map.size() * 2];
        int index = 0;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            array[index++] = entry.getKey();
            array[index++] = entry.getValue();
        }
        return array;
    }

    /**
     * 将用于定位实体对象的键-值对列表转换为键-值数组形式。
     * 
     * @param list
     *            用于定位实体对象的键-值对列表，传入null或空列表将导致返回长度为0的数组。
     */
    public static Object[] toKeyValuesArray2(List<Pair<Object, Object>> list) {
        if (list == null) {
            return new Object[0];
        }
        Object[] array = new Object[list.size() * 2];
        int index = 0;
        for (Pair<Object, Object> pair : list) {
            array[index++] = pair.getKey();
            array[index++] = pair.getValue();
        }
        return array;
    }

}
