/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CustomerServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.basicinfo.customer;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerState;
import com.hd123.sardine.wms.dao.basicInfo.customer.CustomerDao;
import com.hd123.sardine.wms.service.basicInfo.customer.CustomerServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author yangwenzhu
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest extends BaseServiceTest {
  private static final String UUID = "UUID";

  @InjectMocks
  public CustomerServiceImpl service;
  @Mock
  private CustomerDao dao;
  @Captor
  private ArgumentCaptor<Customer> customerCaptor;

  @Test
  public void insert() throws Exception {
    Customer customer = CustomerBuilder.customer().withState(CustomerState.online).build();
    when(dao.getByCode(anyString())).thenReturn(null);
    service.insert(customer);
    verify(dao).insert(customerCaptor.capture());
    Assertions.assertThat(customerCaptor.getValue().getUuid()).isNotEmpty();
  }

  @Test
  public void deleteState() throws Exception {
    Customer customer = CustomerBuilder.customer().withState(CustomerState.online).withVersion(0)
        .withUuid(UUID).build();
    when(dao.get(anyString())).thenReturn(customer);
    service.offline(UUID, 0);
    verify(dao).update(customerCaptor.capture());
    Assertions.assertThat(customerCaptor.getValue().getState()).isEqualTo(CustomerState.offline);
  }

  @Test
  public void recover() throws Exception {
    Customer customer = CustomerBuilder.customer().withState(CustomerState.offline).withVersion(0)
        .withUuid(UUID).build();
    when(dao.get(anyString())).thenReturn(customer);
    service.online(UUID, 0);
    verify(dao).update(customerCaptor.capture());
    Assertions.assertThat(customerCaptor.getValue().getState()).isEqualTo(CustomerState.online);
  }
}
