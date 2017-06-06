/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CompanyService.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 企业服务接口
 * 
 * @author yangwenzhu
 *
 */
public interface CompanyService {
    /**
     * 保存企业
     * 
     * @param company
     *            要保存的企业，not null
     * @return 保存企业的UUID
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     *             其他业务异常
     */
    String insert(Company company) throws IllegalArgumentException, WMSException;

    /**
     * 根据名称获取企业信息
     * 
     * @param name
     *            企业名称
     * @return 企业信息
     */
    Company getByName(String name);

    /**
     * 根据UUID获取企业
     * 
     * @param uuid
     *            企业UUID
     * @return 企业实体
     * @throws IllegalArgumentException
     *             参数为空时抛出
     */
    Company get(String uuid) throws IllegalArgumentException;
}
