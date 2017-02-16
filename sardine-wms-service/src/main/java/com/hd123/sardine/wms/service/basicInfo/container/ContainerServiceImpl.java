/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ContainerServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.container;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerTypeService;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.basicInfo.container.ContainerDao;
import com.hd123.sardine.wms.dao.basicInfo.seq.Sequence;
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;

/**
 * @author Jing
 *
 */
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private ContainerDao dao;
    @Autowired
    private SequenceDao seqDao;
    @Autowired
    private ContainerTypeService containerTypeService;

    @Override
    public void saveNew(String containerTypeUuid, OperateContext operateContext)
            throws WMSException {
        Assert.assertArgumentNotNull(containerTypeUuid, "containerTypeUuid");

        ContainerType containerType = containerTypeService.get(containerTypeUuid);
        if (containerType == null)
            throw new WMSException("容器类型不存在。");

        int currentValue = seqDao.getCurrentValue(containerType.getBarCodePrefix(),
                containerType.getCompanyUuid());
        if (currentValue == 0) {
            Sequence seq = new Sequence();
            seq.setSeqName(containerType.getBarCodePrefix());
            seq.setIncrement(1);
            seq.setCurrentValue(0);
            seq.setCompanyUuid(containerType.getCompanyUuid());
            seqDao.saveSequence(seq);
        }

        Container container = new Container();
        container.setUuid(UUIDGenerator.genUUID());
        String flowCode = String.valueOf(seqDao.getNextValue(containerType.getBarCodePrefix(),
                containerType.getCompanyUuid()));
        container.setBarcode(containerType.getBarCodePrefix()
                + StringUtils.repeat("0", containerType.getBarCodeLength() - flowCode.length())
                + flowCode);
        container.setContainerType(
                new UCN(containerTypeUuid, containerType.getCode(), containerType.getName()));
        container.setCompanyUuid(containerType.getCompanyUuid());
        container.setCreateInfo(OperateInfo.newInstance(operateContext));
        container.setLastModifyInfo(OperateInfo.newInstance(operateContext));
        dao.insert(container);
    }

    @Override
    public Container getByBarcode(String barcode, String companyUuid) {
        Assert.assertArgumentNotNull(barcode, "barcode");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");

        return dao.getByBarcode(barcode, companyUuid);
    }

    @Override
    public PageQueryResult<Container> query(PageQueryDefinition definition) {
        PageQueryResult<Container> pgr = new PageQueryResult<Container>();
        List<Container> list = dao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }
}
