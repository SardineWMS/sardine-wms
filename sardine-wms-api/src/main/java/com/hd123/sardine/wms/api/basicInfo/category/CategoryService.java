/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CategoryService.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.category;

import java.util.List;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 商品类别
 * 
 * @author Jing
 *
 */
public interface CategoryService {
    /***
     * 获取根类别列表，包含子节点
     * 
     * @param companyUuid
     *            公司UUID not null
     * @return 类别列表
     */
    List<Category> getRootCategorys(String companyUuid);

    /**
     * 查询制定类别的下级类别列表
     * 
     * @param categoryUuid
     *            类别UUID not null
     * @return 类别列表
     */
    List<Category> getLowerCategorys(String categoryUuid);

    /**
     * 根据类别UUID查询类别
     * 
     * @param categoryUuid
     * @return 类别
     */
    Category get(String categoryUuid);

    /***
     * 根据类别代码和组织查询商品类别
     * 
     * @param categoryCode
     *            not null
     * @param companyUuid
     *            not null
     * @return 商品类别
     */
    Category getByCode(String categoryCode, String companyUuid);

    /**
     * 新增商品类别
     * 
     * @param category
     *            类别 not null
     * @param operCtx
     */
    void saveNew(Category category, OperateContext operCtx) throws WMSException;

    /**
     * 修改商品类别信息
     * <p>
     * 
     * @param category
     * @param operCtx
     */
    void saveModify(Category category, OperateContext operCtx) throws WMSException;

    /**
     * 删除商品类别，存在子类别的一并删除
     * 
     * @param uuid
     *            not null
     * @param verison
     *            not null
     * @param operCtx
     */
    void remove(String uuid, long verison, OperateContext operCtx);
}