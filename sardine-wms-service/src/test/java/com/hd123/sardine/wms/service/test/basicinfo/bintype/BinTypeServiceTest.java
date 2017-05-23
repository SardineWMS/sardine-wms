/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BinTypeServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.basicinfo.bintype;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.dao.basicInfo.bintype.BinTypeDao;
import com.hd123.sardine.wms.service.basicInfo.bintype.BinTypeServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author yangwenzhu
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BinTypeServiceTest extends BaseServiceTest {
  private static final BigDecimal PLOT_RATIO = new BigDecimal(99.999);
  private static final String UUID = "uuid";
  private static final long VERSION = 0;

  @InjectMocks
  public BinTypeServiceImpl service;

  @Mock
  private BinTypeDao dao;

  @Captor
  private ArgumentCaptor<BinType> binTypeCaptor;

  @Test
  public void insert() throws Exception {
    BinType binType = BinTypeBuilder.binType().withPlotRatio(PLOT_RATIO).build();
    when(dao.getByCode(anyString())).thenReturn(null);
    service.insert(binType);
    verify(dao).insert(binTypeCaptor.capture());
    Assertions.assertThat(binTypeCaptor.getValue().getUuid()).isNotEmpty();
  }

  @Test
  public void remove() throws Exception {
    BinType binType = BinTypeBuilder.binType().withUuid(UUID).withVersion(VERSION).build();
    when(dao.get(anyString())).thenReturn(binType);
    service.remove(UUID, VERSION);

    verify(dao).remove(UUID, VERSION);
  }

  @Test
  public void update() throws Exception {
    BinType binType = BinTypeBuilder.binType().withUuid(UUID).withPlotRatio(PLOT_RATIO)
        .withVersion(VERSION).build();
    when(dao.getByCode(anyString())).thenReturn(BinTypeBuilder.binType().withUuid(UUID).build());
    when(dao.get(anyString())).thenReturn(BinTypeBuilder.binType().withVersion(VERSION).build());
    service.update(binType);

    verify(dao).update(binTypeCaptor.capture());
  }
}
