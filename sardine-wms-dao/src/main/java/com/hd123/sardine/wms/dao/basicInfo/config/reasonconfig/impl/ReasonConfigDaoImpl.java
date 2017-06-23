/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReasonConfigDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.reasonconfig.impl;

import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonType;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.config.reasonconfig.ReasonConfigDao;

/**
 * @author fanqingqing
 *
 */
public class ReasonConfigDaoImpl extends NameSpaceSupport implements ReasonConfigDao {

    @Override
    public void insert(ReasonType reasonType, String reason) {
        Assert.assertArgumentNotNull(reasonType, "reasonType");
        Assert.assertArgumentNotNull(reason, "reason");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("reasonType", reasonType);
        map.put("reason", reason);

        insert(MAPPER_INSERT, map);
    }

    @Override
    public void remove(ReasonType reasonType) {
        Assert.assertArgumentNotNull(reasonType, "reasonType");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("reasonType", reasonType);
        delete(MAPPER_REMOVE, map);
    }

    @Override
    public List<String> query(ReasonType reasonType) {
        Assert.assertArgumentNotNull(reasonType, "reasonType");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("reasonType", reasonType);
        return selectList(MAPPER_QUERY_BYLIST, map);
    }

}
