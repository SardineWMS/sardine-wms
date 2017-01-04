/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	EntityInvalidException.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.sardine.wms.common.utils.EntityUtil;

/**
 * 意味着实体不存在或状态不可用。
 * 
 * @author zhangsai
 * 
 */
public class EntityInvalidException extends DataAccessException {

    private static final long serialVersionUID = -2531246065792357789L;

    /**
     * @param entityName
     *            实体名。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
     */
    public static EntityInvalidException create(String entityName, Object... keyValues) {
        return create2(entityName, null, keyValues);
    }

    /**
     * @param entityName
     *            实体名。
     * @param caught
     *            捕捉的异常。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
     */
    public static EntityInvalidException create2(String entityName, Throwable caught,
            Object... keyValues) throws IllegalArgumentException {
        EntityInvalidException exception = new EntityInvalidException(
                buildMessage(entityName, keyValues), caught);
        exception.inject(entityName, keyValues);
        return exception;
    }

    public EntityInvalidException(String message) {
        super(message);
    }

    public EntityInvalidException(String message, Throwable caught) {
        super(message, caught);
    }

    private String entityName;
    private List<Pair<Object, Object>> keyValues;
    private List<Pair<Object, Object>> roKeyValues;

    /**
     * 实体名，用于显示的文本，例如“用户”。
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 返回键-值对列表，表示所指定的实体。返回的列表对象只读。
     */
    public List<Pair<Object, Object>> getKeyValues() {
        return roKeyValues;
    }

    @SuppressWarnings("unchecked")
    protected void inject(String entityName, Object... keyValues) {
        this.entityName = entityName;
        this.keyValues = EntityUtil.toKeyValuesList(keyValues);
        this.roKeyValues = ListUtils.unmodifiableList(this.keyValues);
    }

    private static String buildMessage(String entityName, Object... keyValues) {
        String entity = EntityUtil.toEntityString(entityName, keyValues);
        return MessageFormat.format(R.R.entityInvalid(), entity);
    }

    public static interface R {
        public static final R R = Resources.create(R.class);

        @DefaultStringValue("指定实体“{0}”不存在或状态不可用。")
        String entityInvalid();
    }

}
