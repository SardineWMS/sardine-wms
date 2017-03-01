/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	EntityLogSerivceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月22日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.log.EntityLog;
import com.hd123.sardine.wms.api.log.EntityLogService;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.dao.log.EntityLogDao;

/**
 * @author fanqingqing
 *
 */
public class EntityLogServiceImpl implements EntityLogService {
    @Autowired
    private EntityLogDao entityLogDao;

    @Override
    public PageQueryResult<EntityLog> query(PageQueryDefinition filter) {
        Assert.assertArgumentNotNull(filter, "filter");

        PageQueryResult<EntityLog> pgr = new PageQueryResult<EntityLog>();
        List<EntityLog> list = entityLogDao.getEntityLog(filter);
        PageQueryUtil.assignPageInfo(pgr, filter);
        pgr.setRecords(list);
        return pgr;
    }

}
