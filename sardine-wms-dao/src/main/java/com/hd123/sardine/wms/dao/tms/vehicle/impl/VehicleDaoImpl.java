/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	VehicleDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.tms.vehicle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.vehicle.Vehicle;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.tms.vehicle.VehicleDao;

/**
 * @author yangwenzhu
 *
 */
public class VehicleDaoImpl extends BaseDaoImpl<Vehicle> implements VehicleDao {
    public static final String MAPPER_GETBYCODE = "getByCode";
    public static final String MAPPER_QUERYBYTYPE = "queryByType";
    public static final String MAPPER_GETBYVEHICLENO = "getByVehicleNo";

    @Override
    public Vehicle getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("code", code);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
    }

    @Override
    public List<Vehicle> queryByType(String typeUuid) {
        if (StringUtil.isNullOrBlank(typeUuid))
            return new ArrayList<Vehicle>();
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("typeUuid", typeUuid);
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYBYTYPE), map);
    }

    @Override
    public Vehicle getByVehicleNo(String vehicleNo) {
        if (StringUtil.isNullOrBlank(vehicleNo))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("vehicleNo", vehicleNo);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYVEHICLENO), map);
    }

}
