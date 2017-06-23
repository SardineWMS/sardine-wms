/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReasonConfigService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.reasonconfig;

import java.util.List;

import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author fanqingqing
 *
 */
public interface ReasonConfigService {

    void setReasonConfig(ReasonType type, List<String> reasons)
            throws IllegalArgumentException, WMSException;

    List<String> queryReasons(ReasonType type) throws IllegalArgumentException;
}
