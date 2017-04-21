/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BinServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.bin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinInfo;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.bin.Path;
import com.hd123.sardine.wms.api.basicInfo.bin.Shelf;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.bin.WrhType;
import com.hd123.sardine.wms.api.basicInfo.bin.Zone;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinTypeService;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.bin.BinDao;
import com.hd123.sardine.wms.dao.basicInfo.bin.PathDao;
import com.hd123.sardine.wms.dao.basicInfo.bin.ShelfDao;
import com.hd123.sardine.wms.dao.basicInfo.bin.WrhDao;
import com.hd123.sardine.wms.dao.basicInfo.bin.ZoneDao;
import com.hd123.sardine.wms.dao.basicInfo.seq.Sequence;
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * 货位服务：实现
 * 
 * @author zhangsai
 *
 */
public class BinServiceImpl extends BaseWMSService implements BinService {

  @Autowired
  private WrhDao wrhDao;

  @Autowired
  private ZoneDao zoneDao;

  @Autowired
  private PathDao pathDao;

  @Autowired
  private ShelfDao shelfDao;

  @Autowired
  private BinDao binDao;

  @Autowired
  private SequenceDao sequenceDao;

  @Autowired
  private BinTypeService binTypeService;

  @Autowired
  private ValidateHandler<OperateContext> operateContextValidateHandler;

  @Override
  public void insertWrh(Wrh wrh, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(wrh, "wrh");
    Assert.assertArgumentNotNull(wrh.getCode(), "wrh.code");
    Assert.assertArgumentNotNull(wrh.getName(), "wrh.name");
    Assert.assertArgumentNotNull(wrh.getCompanyUuid(), "wrh.companyUuid");
    Assert.assertStringNotTooLong(wrh.getCode(), Wrh.LENGTH_CODE, "wrh.code");
    Assert.assertStringNotTooLong(wrh.getName(), Wrh.LENGTH_NAME, "wrh.name");
    Assert.assertStringNotTooLong(wrh.getNote(), Wrh.LENGTH_NOTE, "wrh.note");

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    Wrh codeEqualsWrh = wrhDao.getByCode(wrh.getCompanyUuid(), wrh.getCode());
    if (codeEqualsWrh != null && Objects.equals(codeEqualsWrh.getUuid(), wrh.getUuid()) == false)
      throw new WMSException("仓位" + wrh.getCode() + "已存在。");

    wrh.setUuid(UUIDGenerator.genUUID());
    wrhDao.insert(wrh);
  }

  @Override
  public void insertZone(Zone zone, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(zone, "zone");
    Assert.assertArgumentNotNull(zone.getCode(), "zone.code");
    Assert.assertArgumentNotNull(zone.getName(), "zone.name");
    Assert.assertArgumentNotNull(zone.getWrh(), "zone.wrh");
    Assert.assertArgumentNotNull(zone.getCompanyUuid(), "zone.companyUuid");
    Assert.assertStringNotTooLong(zone.getCode(), Zone.LENGTH_CODE, "zone.code");
    Assert.assertStringNotTooLong(zone.getName(), Zone.LENGTH_NAME, "zone.name");
    Assert.assertStringNotTooLong(zone.getNote(), Zone.LENGTH_NOTE, "zone.note");

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    Zone codeEqualsZone = zoneDao.getByCode(zone.getCompanyUuid(), zone.getCode());
    if (codeEqualsZone != null && Objects.equals(codeEqualsZone.getUuid(), zone.getUuid()) == false)
      throw new WMSException("货区" + zone.getCode() + "已存在。");

    Wrh zoneOfWrh = wrhDao.get(zone.getWrh().getUuid(), zone.getCompanyUuid());
    if (zoneOfWrh == null) {
      throw new WMSException("货区对应的仓位" + zone.getWrh().getUuid() + "不存在。");
    }

    zone.setWrh(new UCN(zoneOfWrh.getUuid(), zoneOfWrh.getCode(), zoneOfWrh.getName()));
    zone.setUuid(UUIDGenerator.genUUID());
    zoneDao.insert(zone);
  }

  @Override
  public void insertPath(Path path, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(path, "path");
    Assert.assertArgumentNotNull(path.getZoneUuid(), "path.zone");
    Assert.assertArgumentNotNull(path.getCompanyUuid(), "path.companyUuid");
    Assert.assertStringNotTooLong(path.getNote(), Path.LENGTH_NOTE, "path.note");

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    Zone pathOfZone = zoneDao.get(path.getZoneUuid(), path.getCompanyUuid());
    if (pathOfZone == null) {
      throw new WMSException("货道对应的货区" + path.getZoneUuid() + "不存在。");
    }

    int pathCodeByInt = getSequenceNextValue(Path.class.getSimpleName() + pathOfZone.getCode(),
        path.getCompanyUuid());
    String pathCode = pathCodeByInt < 10 ? "0" + String.valueOf(pathCodeByInt)
        : String.valueOf(pathCodeByInt);

    path.setCode(pathOfZone.getCode() + pathCode);
    path.setUuid(UUIDGenerator.genUUID());
    pathDao.insert(path);
  }

  @Override
  public void insertShelf(String pathCode, String companyUuid, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(pathCode, "pathCode");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertStringNotTooLong(pathCode, Path.LENGTH_CODE, "pathCode");

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    Path shelfOfPath = pathDao.getByCode(companyUuid, pathCode);
    if (shelfOfPath == null) {
      throw new WMSException("货架对应的货道" + pathCode + "不存在。");
    }

    int shelfCodeByInt = getSequenceNextValue(Shelf.class.getSimpleName() + shelfOfPath.getCode(),
        companyUuid);
    String shelfCode = shelfCodeByInt < 10 ? "0" + String.valueOf(shelfCodeByInt)
        : String.valueOf(shelfCodeByInt);

    Shelf shelf = new Shelf();
    shelf.setPathUuid(shelfOfPath.getUuid());
    shelf.setCompanyUuid(companyUuid);
    shelf.setCode(pathCode + shelfCode);
    shelf.setUuid(UUIDGenerator.genUUID());
    shelfDao.insert(shelf);
  }

  @Override
  public void insertBin(Bin bin, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(bin, "bin");
    Assert.assertArgumentNotNull(bin.getCode(), "bin.code");
    Assert.assertArgumentNotNull(bin.getUsage(), "bin.usage");
    Assert.assertArgumentNotNull(bin.getBinType(), "bin.type");
    Assert.assertArgumentNotNull(bin.getCompanyUuid(), "bin.companyUuid");
    Assert.assertStringNotTooLong(bin.getCode(), Bin.LENGTH_CODE, "bin.code");

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    BinType binType = binTypeService.get(bin.getBinType().getUuid());
    if (binType == null) {
      throw new WMSException("货位对应货位类型" + bin.getBinType().getCode() + "不存在。");
    }
    Shelf binOfShelf = shelfDao.getByCode(bin.getCompanyUuid(), bin.getCode().substring(0, 6));
    if (binOfShelf == null) {
      throw new WMSException("货位对应货架" + bin.getShelfUuid() + "不存在。");
    }
    Path binOfPath = pathDao.get(binOfShelf.getPathUuid(), binOfShelf.getCompanyUuid());
    if (binOfPath == null) {
      throw new IllegalArgumentException("货道不存在。");
    }
    Zone binOfZone = zoneDao.get(binOfPath.getZoneUuid(), binOfPath.getCompanyUuid());
    if (binOfZone == null) {
      throw new IllegalArgumentException("货区不存在。");
    }
    Bin codeEqualsBin = binDao.getByCode(bin.getCompanyUuid(), bin.getCode());
    if (codeEqualsBin != null && Objects.equals(codeEqualsBin.getUuid(), bin.getUuid()) == false)
      throw new WMSException("货位" + bin.getCode() + "已存在。");

    bin.setShelfUuid(binOfShelf.getUuid());
    bin.setBinLevel(bin.getCode().substring(7));
    bin.setBinColumn(bin.getCode().substring(7, 8));
    bin.setBinType(new UCN(binType.getUuid(), binType.getCode(), binType.getName()));
    bin.setWrh(binOfZone.getWrh());
    bin.setState(BinState.free);
    bin.setUuid(UUIDGenerator.genUUID());
    binDao.insert(bin);
  }

  @Override
  public PageQueryResult<Bin> queryBin(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<Bin> pgr = new PageQueryResult<Bin>();
    List<Bin> list = binDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public List<BinInfo> queryTreeData(String companyUuid) {
    if (StringUtil.isNullOrBlank(companyUuid))
      return new ArrayList<BinInfo>();

    List<BinInfo> result = new ArrayList<BinInfo>();
    List<Wrh> wrhs = wrhDao.query(companyUuid);
    for (Wrh wrh : wrhs) {
      BinInfo wrhInfo = new BinInfo();
      wrhInfo.setTitle(wrh.getUuid());
      wrhInfo.setKey(wrh.toFriendString());
      wrhInfo.setType(WrhType.wrh);

      List<Zone> wrhOfZones = zoneDao.query(companyUuid, wrh.getUuid());
      for (Zone zone : wrhOfZones) {
        BinInfo zoneInfo = new BinInfo();
        zoneInfo.setTitle(zone.getUuid());
        zoneInfo.setKey(zone.toFriendString());
        zoneInfo.setType(WrhType.zone);

        List<Path> zoneOfPaths = pathDao.query(companyUuid, zone.getUuid());
        for (Path path : zoneOfPaths) {
          BinInfo pathInfo = new BinInfo();
          pathInfo.setTitle(path.getUuid());
          pathInfo.setKey(path.getCode());
          pathInfo.setType(WrhType.path);

          List<Shelf> pathOfShelfs = shelfDao.query(companyUuid, path.getUuid());
          for (Shelf shelf : pathOfShelfs) {
            BinInfo shelfInfo = new BinInfo();
            shelfInfo.setTitle(shelf.getUuid());
            shelfInfo.setKey(shelf.getCode());
            shelfInfo.setType(WrhType.shelf);
            pathInfo.getChildren().add(shelfInfo);
          }
          zoneInfo.getChildren().add(pathInfo);
        }
        wrhInfo.getChildren().add(zoneInfo);
      }
      result.add(wrhInfo);
    }
    return result;
  }

  @Override
  public void remove(String uuid, long version, String companyUuid, OperateContext operCtx)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    Bin bin = binDao.get(uuid, companyUuid);
    if (bin == null)
      return;

    PersistenceUtils.checkVersion(version, bin, "货位", bin.getCode());
    binDao.remove(uuid, version);
  }

  @Override
  public List<Wrh> queryWrhs(String companyUuid) {
    return wrhDao.query(companyUuid);
  }

  @Override
  public List<Zone> queryZones(String companyUuid) {
    List<Wrh> wrhs = wrhDao.query(companyUuid);
    List<Zone> zones = new ArrayList<Zone>();
    for (Wrh wrh : wrhs) {
      zones.addAll(zoneDao.query(companyUuid, wrh.getUuid()));
    }
    return zones;
  }

  private synchronized int getSequenceNextValue(String sequenceName, String companyUuid) {
    int currentValue = sequenceDao.getCurrentValue(sequenceName + companyUuid, companyUuid);
    if (currentValue == 0) {
      Sequence seq = new Sequence();
      seq.setCompanyUuid(companyUuid);
      seq.setCurrentValue(0);
      seq.setIncrement(1);
      seq.setSeqName(sequenceName + companyUuid);
      sequenceDao.saveSequence(seq);
    }

    return sequenceDao.getNextValue(sequenceName + companyUuid, companyUuid);
  }

  @Override
  public Wrh getWrh(String wrhUuid) {
    return wrhDao.get(wrhUuid, ApplicationContextUtil.getCompanyUuid());
  }

  @Override
  public Bin getBinByCode(String binCode) {
    if (StringUtil.isNullOrBlank(binCode))
      return null;
    return binDao.getByCode(ApplicationContextUtil.getCompanyUuid(), binCode);
  }

  @Override
  public Bin getBinByWrhAndUsage(String wrhUuid, BinUsage usage) {
    return binDao.getBinByWrhAndUsage(wrhUuid, usage);
  }

  @Override
  public void changeState(String uuid, long version, BinState state)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(state, "state");

    Bin bin = binDao.get(uuid, ApplicationContextUtil.getCompanyUuid());
    if (bin == null)
      throw new WMSException("指定货位不存在！");
    if (bin.getState().equals(state))
      return;

    PersistenceUtils.checkVersion(version, bin, "货位", bin.getCode());
    bin.setState(state);
    binDao.changeState(uuid, version, state);
  }
}
