/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BaseServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.internal.PropertySupport;
import org.junit.Before;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.utils.BillNumberGenerator;
import com.hd123.sardine.wms.common.utils.FlowCodeGenerator;

/**
 * @author zhangsai
 *
 */
@SuppressWarnings({
        "rawtypes", "unchecked" })
public class BaseServiceTest {

    private static List<String> CLASSES = new ArrayList<String>();
    private static List<Class> CCS = new ArrayList<Class>();
    static {
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.EntityNotNullValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.Length100Validator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.Length255Validator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.Length30Validator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.NotNullValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.NullValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.OperateContextValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.StringLengthValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.routines.VersionValidator");
        CLASSES.add("com.hd123.sardine.wms.common.validator.OperateContextValidateHandler");
        CLASSES.add("com.hd123.sardine.wms.common.validator.GenericValidateHandler");

        for (String className : CLASSES) {
            try {
                Class cc = Class.forName(className);
                CCS.add(cc);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        BillNumberGenerator billNumberGenerator = when(
                mock(BillNumberGenerator.class).allocate(anyString(), anyString()))
                        .thenReturn(RandomStringUtils.randomNumeric(10)).getMock();
        ReflectionTestUtils.setField(BillNumberGenerator.class, "instance", billNumberGenerator);

        FlowCodeGenerator flowCodeGenerator = when(
                mock(FlowCodeGenerator.class).allocate(anyString()))
                        .thenReturn(RandomStringUtils.randomNumeric(6)).getMock();
        ReflectionTestUtils.setField(FlowCodeGenerator.class, "instance", flowCodeGenerator);
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(InjectMocks.class) != null) {
                setField(field.get(this));
            }
        }
    }

    private static void setField(Object service) throws Exception {
        assert service != null;

        Field[] fields = service.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ((field.getAnnotation(Autowired.class) != null
                    || field.getAnnotation(Resource.class) != null)
                    && (field.getName().endsWith("ValidateHandler")
                            || field.getName().endsWith("Validator"))) {
                Object o = null;
                if (field.getType().isInterface()) {
                    List<Class> classes = getAllClassByInterface(field.getType());
                    for (Class c : classes) {
                        if (c.getSimpleName().toUpperCase().equals(field.getName().toUpperCase()))
                            o = c.newInstance();
                    }
                } else
                    o = field.getType().newInstance();
                ReflectionTestUtils.setField(service, field.getName(), o);
                setField(o);
            }
        }
    }

    public static List<Class> getAllClassByInterface(Class c) throws Exception {
        List returnClassList = new ArrayList<Class>();
        // 判断是不是接口,不是接口不作处理
        if (c.isInterface()) {
            List<Class> allClass = getClasses("com.hd123.sardine.wms.service");// 获得当前包以及子包下的所有类
            allClass.addAll(CCS);
            // 判断是否是一个接口
            for (int i = 0; i < allClass.size(); i++) {
                if (c.isAssignableFrom(allClass.get(i))) {
                    if (!c.equals(allClass.get(i))) {
                        returnClassList.add(allClass.get(i));
                    }
                }
            }
        }
        return returnClassList;
    }

    /**
     * 
     * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
     * @param pageName
     *            包名
     * @return List<Class> 包下所有类
     */
    private static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClass(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClass(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "."
                        + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    protected static OperateContext defaultOperCtx() {
        Operator operator = new Operator("uuid", "loginId", "name");
        OperateContext context = new OperateContext();
        context.setOperator(operator);
        context.setTime(new Date());
        return context;
    }

    /***
     * 支持以"属性名"-"实际值"对方式,判断列表中是否存在某个元素包含特定属性值。
     * 
     * @param actual
     *            "属性名"-"实际值"对
     * @param clazz
     *            验证数据类型。
     * 
     */
    protected <T> List<T> refEqL(Class<T> clazz, final Object... actual) {
        if (actual == null || actual.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        return argThat(new ArgumentMatcher<List<T>>() {
            @Override
            public boolean matches(Object argument) {
                if (argument == null) {
                    return false;
                }
                List<T> list = (List) argument;
                if (list.isEmpty()) {
                    return false;
                }

                for (T t : list) {
                    if (refEq(t, actual)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /***
     * 支持以"属性名"-"实际值"对方式对对象属性进行验证.
     * 
     * @param actual
     *            "属性名"-"实际值"对
     * @param clazz
     *            验证数据类型。
     * 
     */
    protected <T> T refEq(Class<T> clazz, final Object... actual) {
        if (actual == null || actual.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        return argThat(new ArgumentMatcher<T>() {
            @Override
            public boolean matches(Object argument) {
                if (argument == null) {
                    return false;
                }
                return refEq(argument, actual);
            }
        });
    }

    private boolean refEq(Object argument, final Object... actual) {
        int count = actual.length / 2;
        List actualValues = new ArrayList();
        for (int i = 0; i < count; i++) {
            actualValues.add(actual[i * 2 + 1]);
        }

        String[] fields = new String[count];
        for (int i = 0; i < count; i++) {
            fields[i] = actual[i * 2].toString();
        }
        return actualValues.equals(Arrays.asList(extractProperties(argument, fields)));
    }

    /** 获取对象属性值，支持属性嵌入表达 */
    protected Object[] extractProperties(Object javaBean, String... fields) {
        if (javaBean == null || fields == null) {
            return new Object[0];
        }

        PropertySupport propertySupport = PropertySupport.instance();
        List results = new ArrayList();
        for (String propertyName : fields) {
            results.add(propertySupport.propertyValueOf(propertyName, Object.class, javaBean));
        }

        return results.toArray();
    }
}
