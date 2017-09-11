/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ResourceServiceTest_New.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.resource;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceConstants;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class ResourceServiceTest_New extends BaseTest {
  @Autowired
  private ResourceService service;

  @Test
  public void testQueryByUpResource() {
    List<Resource> resource = service.queryByUpperResource(ResourceConstants.FORWARD_UUID);
    System.out.println(Arrays.toString(resource.toArray()));
  }
}
