/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickUpBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillDao;

/**
 * @author ZhangSai
 *
 */
public class PickUpBillDaoImpl extends NameSpaceSupport implements PickUpBillDao {

    private static final String SAVENEW = "saveNew";
    private static final String SAVEMODIFY = "saveModify";
    private static final String GET = "get";
    private static final String GETBYBILLNUMBER = "getByBillNumber";
    private static final String QUERYPICKTASKVIEW = "queryPickTaskView";
    private static final String QUERYBYWAVEUUID = "queryByWaveUuid";
    private static final String REMOVE = "remove";
    private static final String APPROVEBYWAVEBILLNUMBER = "approveByWaveBillNumber";
    private static final String QUERYBYPAGE = "queryByPage";

    @Override
    public void saveNew(PickUpBill pickUpBill) {
        Assert.assertArgumentNotNull(pickUpBill, "pickUpBill");

        insert(SAVENEW, pickUpBill);
    }

    @Override
    public void saveModify(PickUpBill pickUpBill) {
        Assert.assertArgumentNotNull(pickUpBill, "pickUpBill");

        int updateRows = update(SAVEMODIFY, pickUpBill);
        PersistenceUtils.optimisticVerify(updateRows);
    }

    @Override
    public PickUpBill get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return selectOne(GET, uuid);
    }

    @Override
    public PickUpBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        return selectOne(GETBYBILLNUMBER, map);
    }

    @Override
    public List<PickUpBill> queryByWaveUuid(String waveUuid) {
        if (StringUtil.isNullOrBlank(waveUuid))
            return new ArrayList<PickUpBill>();

        return selectList(QUERYBYWAVEUUID, waveUuid);
    }

    @Override
    public void remove(String uuid, long version) {
        Assert.assertArgumentNotNull(uuid, "uuid");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", uuid);
        map.put("version", version);

        int updateRows = delete(REMOVE, map);
        PersistenceUtils.optimisticVerify(updateRows);
    }

    @Override
    public void approveByWaveBillNumber(String waveBillNumber) {
        Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("waveBillNumber", waveBillNumber);
        map.put("state", PickUpBillState.approved);
        map.put("user", ApplicationContextUtil.getLoginUser());
        map.put("date", new Date());
        update(APPROVEBYWAVEBILLNUMBER, map);
    }

    @Override
    public List<TaskView> queryPickTaskView(String waveBillNumber) {
        if (StringUtil.isNullOrBlank(waveBillNumber))
            return new ArrayList<TaskView>();

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("waveBillNumber", waveBillNumber);
        return selectList(QUERYPICKTASKVIEW, map);
    }

    @Override
    public List<PickUpBill> queryByPage(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        return getSqlSession().selectList(generateStatement(QUERYBYPAGE), definition);
    }
}
