/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	wechange-commons
 * 文件名：	POJORandomInitionlizer.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月1日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java Bean 随机初始化器
 * <p>
 * 主要用于单元测试Java Bean赋值，减少无意义测试代码。
 * 
 * @author zhangsai
 *
 */
public class POJORandomInitionlizer {
    private static final Logger logger = LoggerFactory.getLogger(POJORandomInitionlizer.class);

    @SuppressWarnings("rawtypes")
    private static List<Class> SIMPLE_CLASSES = new ArrayList<Class>();
    static {
        SIMPLE_CLASSES.add(boolean.class);
        SIMPLE_CLASSES.add(int.class);
        SIMPLE_CLASSES.add(short.class);
        SIMPLE_CLASSES.add(long.class);
        SIMPLE_CLASSES.add(float.class);
        SIMPLE_CLASSES.add(double.class);
        SIMPLE_CLASSES.add(char.class);
        SIMPLE_CLASSES.add(byte.class);
        SIMPLE_CLASSES.add(Date.class);
        SIMPLE_CLASSES.add(Time.class);
        SIMPLE_CLASSES.add(String.class);
        SIMPLE_CLASSES.add(BigDecimal.class);
    }

    /** 随机初始化对象 */
    @SuppressWarnings("rawtypes")
    public static void randomInit(Object o) {
        if (o == null) {
            return;
        }
        Class c = o.getClass();
        if (isSimpleElement(c)) {
            return;
        }
        randomInit(o, c);
    }

    @SuppressWarnings("rawtypes")
    private static void randomInit(Object o, Class c) {
        assert o != null;
        if (c == null || c.equals(Object.class)) {
            return;
        }

        try {
            for (Field f : c.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
                    continue;
                }
                f.setAccessible(true);
                Class fieldClass = f.getType();

                if (f.getType().isPrimitive()) {
                    initPrimity(o, f);
                    continue;
                }

                if (fieldClass.isInterface()) {
                    continue;
                }

                if (f.get(o) != null) {
                    continue;
                }

                if (fieldClass.isEnum()) {
                    initEnum(o, f);
                    continue;
                }

                if (isSimpleElement(fieldClass)) {
                    initSimpleElementExceptionEnumAndPrimitive(o, f);
                    continue;
                }

                Object property = null;
                property = new Integer(1);
                if (fieldClass.equals(Integer.class)) {
                    property = new Integer(2);
                } else if (fieldClass.equals(Long.class)) {
                    property = new Long(10);
                } else if (fieldClass.equals(Boolean.class)) {
                    property = new Boolean(true);
                } else {
                    property = fieldClass.newInstance();
                }
                f.set(o, property);
                randomInit(property);
            }

            if (c.getSuperclass().equals(Object.class) == false) {
                randomInit(o, c.getSuperclass());
            }
        } catch (IllegalArgumentException e) {
            logger.debug(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.debug(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.debug(e.getMessage(), e);
        } catch (InstantiationException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    @SuppressWarnings("rawtypes")
    private static boolean isSimpleElement(Class c) {
        assert c != null;
        return SIMPLE_CLASSES.contains(c);
    }

    @SuppressWarnings("rawtypes")
    private static void initSimpleElementExceptionEnumAndPrimitive(Object o, Field f)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assert o != null;
        assert f != null;

        Class c = f.getType();
        Object value = null;
        if (c.equals(String.class)) {
            value = RandomStringUtils.randomAlphabetic(10);
        }
        if (c.equals(Date.class)) {
            value = new Date();
        }
        if (c.equals(Time.class)) {
            value = new Time(new Date().getTime());
        }
        if (c.equals(BigDecimal.class)) {
            value = new BigDecimal(randomPositiveInt());
        }
        if (value != null) {
            f.set(o, value);
        }
    }

    @SuppressWarnings("rawtypes")
    private static void initEnum(Object o, Field f)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assert o != null;
        assert f != null;

        Class c = f.getType();
        Object value = null;
        if (c.isEnum()) {
            value = c.getEnumConstants() == null ? null : c.getEnumConstants()[0];
            f.set(o, value);
        }
    }

    @SuppressWarnings("rawtypes")
    private static void initPrimity(Object o, Field f)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        assert o != null;
        assert f != null;

        Class c = f.getType();
        Object value = null;
        int i = randomPositiveInt();

        if (c.equals(byte.class)) {
            value = Byte.MAX_VALUE;
        }
        if (c.equals(int.class)) {
            value = i;
        }
        if (c.equals(short.class)) {
            value = i;
        }
        if (c.equals(long.class)) {
            value = i;
        }
        if (c.equals(float.class)) {
            value = i;
        }
        if (c.equals(double.class)) {
            value = i;
        }
        if (c.equals(char.class)) {
            value = CharUtils.toChar(RandomStringUtils.randomAlphabetic(1));
        }
        if (c.equals(boolean.class)) {
            value = true;
        }
        if (value != null) {
            f.set(o, value);
        }
    }

    private static int randomPositiveInt() {
        Random r = new Random();
        int i = r.nextInt();
        i = i > 0 ? i : -i;
        i = i == 0 ? 1 : i;
        return i;
    }
}
