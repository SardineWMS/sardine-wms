/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	wechange-commons-biz
 * 文件名：	PersistenceUtils.java
 * 模块说明：	
 * 修改历史：
 * 2016年11月26日 - Jing - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author Jing
 *
 */
public class PersistenceUtils {
    /**
     * 版本号检查。
     * 
     * @param version
     * @param hv
     * @param classCaption
     * @param entitySpec
     * @throws WMSException
     */
    public static void checkVersion(long version, HasVersion hv, String classCaption,
            String entitySpec) throws VersionConflictException {
        if (hv != null && version != hv.getVersion()) {
            String identifier = StringUtils.isBlank(entitySpec) ? "" : "(" + entitySpec + ")";
            throw new VersionConflictException(
                    "指定" + classCaption + identifier + "已经被其他用户修改，操作将被取消。");
        }
    }

    /**
     * 检查乐观锁。
     * 
     * @param updateRows
     * @throws VersionConflictException
     */
    public static void optimisticVerify(int updateRows) throws VersionConflictException {
        if (updateRows == 0) {
            throw new VersionConflictException("指定数据已被修改，操作将被取消。");
        }
    }
}
