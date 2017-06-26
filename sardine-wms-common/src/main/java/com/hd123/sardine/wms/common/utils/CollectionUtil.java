/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	CollectionUtil.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hd123.sardine.wms.common.entity.IsEntity;

/**
 * @author yangwenzhu
 *
 */
public class CollectionUtil {
    public static List<String> toUuids(Collection<? extends IsEntity> entities) {
        List<String> result = new ArrayList<String>();
        if (entities == null || entities.isEmpty())
            return result;
        for (IsEntity entity : entities) {
            result.add(entity.getUuid());
        }
        return result;
    }
}
