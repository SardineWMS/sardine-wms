/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	VersionConflictException.java
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
 * 意味着版本检查发生冲突导致操作无法继续。
 * 
 * @author zhangsai
 * 
 */
public class VersionConflictException extends DataAccessException {

    private static final long serialVersionUID = 1440619042021736393L;

    private static final long UNKNOWN_VERSION = -1;

    /**
     * @param entityName
     *            实体名。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
     */
    public static VersionConflictException create(String entityName, Object... keyValues) {
        return create3(entityName, UNKNOWN_VERSION, UNKNOWN_VERSION, null, keyValues);
    }

    /**
     * @param entityName
     *            实体名。
     * @param expectVersion
     *            期望的版本号。
     * @param actualVersion
     *            实际的版本号。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
     */
    public static VersionConflictException create2(String entityName, long expectVersion,
            long actualVersion, Object... keyValues) {
        return create3(entityName, expectVersion, actualVersion, null, keyValues);
    }

    /**
     * @param entityName
     *            实体名。
     * @param expectVersion
     *            期望的版本号。
     * @param actualVersion
     *            实际的版本号。
     * @param caught
     *            捕捉的异常。
     * @param keyValues
     *            表示实体对象的键与值的数组。一般情况下，按照：<br>
     *            <i>键1, 值1, 键2, 值2, ...</i><br>
     *            作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
     */
    public static VersionConflictException create3(String entityName, long expectVersion,
            long actualVersion, Throwable caught, Object... keyValues) {
        String message = buildMessage(entityName, expectVersion, actualVersion, keyValues);
        VersionConflictException exception = new VersionConflictException(message, caught);
        exception.inject(entityName, expectVersion, actualVersion, keyValues);
        return exception;
    }

    public VersionConflictException() {
        // do nothing
    }

    public VersionConflictException(String message) {
        super(message);
    }

    public VersionConflictException(String message, Throwable caught) {
        super(message, caught);
    }

    private String entityName;
    private List<Pair<Object, Object>> keyValues;
    private List<Pair<Object, Object>> roKeyValues;
    private long actualVersion;
    private long expectVersion;

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

    /**
     * 实际的版本号，通常为来自持久层的版本号。
     */
    public long getActualVersion() {
        return actualVersion;
    }

    /**
     * 期望的版本号，即操作调用者传入的版本号。
     */
    public long getExpectVersion() {
        return expectVersion;
    }

    @SuppressWarnings("unchecked")
    protected void inject(String entityName, long expectVersion, long actualVersion,
            Object... keyValues) {
        this.entityName = entityName;
        this.keyValues = EntityUtil.toKeyValuesList(keyValues);
        this.roKeyValues = ListUtils.unmodifiableList(this.keyValues);
        this.expectVersion = expectVersion;
        this.actualVersion = actualVersion;
    }

    private static String buildMessage(String entityName, long expectVersion, long actualVersion,
            Object... keyValues) {
        String entity = EntityUtil.toEntityString(entityName, keyValues);
        return MessageFormat.format(R.R.versionConflict(), entity, expectVersion, actualVersion);
    }

    public static interface R {
        public static final R R = Resources.create(R.class);

        @DefaultStringValue("{0}的版本发生冲突，可能已经被其它用户或系统修改（期望={1}，实际={2}）。")
        String versionConflict();
    }
}
