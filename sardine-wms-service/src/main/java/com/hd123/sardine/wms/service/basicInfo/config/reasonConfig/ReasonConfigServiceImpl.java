/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReasonConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月22日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config.reasonConfig;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonConfigService;
import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonType;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.dao.basicInfo.config.reasonconfig.ReasonConfigDao;

/**
 * @author fanqingqing
 *
 */
public class ReasonConfigServiceImpl implements ReasonConfigService {

    @Autowired
    private ReasonConfigDao dao;

    @Override
    public void setReasonConfig(ReasonType type, List<String> reasons)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(type, "type");

        if (CollectionUtils.isEmpty(reasons))
            return;
        dao.remove(type);
        for (String reason : reasons) {
            if (StringUtil.isNullOrBlank(reason))
                continue;
            dao.insert(type, reason);
        }
    }

    @Override
    public List<String> queryReasons(ReasonType type) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(type, "type");

        return dao.query(type);
    }

}
