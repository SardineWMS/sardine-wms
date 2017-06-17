/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickAreaService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.pickarea;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface PickAreaService {
    public static final String QUERY_CODE_LIKE = "code";
    public static final String QUERY_NAME_EQUALS = "name";

    /**
     * 新增拣货分区
     * 
     * @param area
     *            拣货分区，not null
     * @return 新增拣货分区的UUID
     * @throws IllegalArgumentException
     * @throws WMSException
     */
    String saveNew(PickArea area) throws WMSException;

    /***
     * 修改拣货分区
     * 
     * @param area
     *            要修改的拣货分区，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void saveModify(PickArea area) throws WMSException;

    /**
     * 根据UUID，删除拣货分区
     * 
     * @param uuid
     *            uuid, not null
     * @param version
     *            版本号,not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void remove(String uuid, long version) throws WMSException;

    /**
     * 根据UUID获取拣货分区
     * 
     * @param uuid
     *            uuid,为空，则返回null
     * @return 拣货分区实体
     * @throws IllegalArgumentException
     */
    PickArea get(String uuid);

    /**
     * 根据代码获取拣货分区
     * 
     * @param code
     *            code，为空，则返回null
     * @return 拣货分区实体
     * @throws IllegalArgumentException
     */
    PickArea getByCode(String code);

    /**
     * 分区查询拣货分区
     * 
     * @param definition
     *            拣货分区
     * @return 分页结果集
     * @throws IllegalArgumentException
     */
    PageQueryResult<PickArea> query(PageQueryDefinition definition);

    /**
     * 校验货位是否在拣货分区对应的货位范围内
     * 
     * @param pickAreaUuid
     *            拣货分区UUID， not null
     * @param binCode
     *            货位条码，not null
     * @return 校验结果
     */

}
