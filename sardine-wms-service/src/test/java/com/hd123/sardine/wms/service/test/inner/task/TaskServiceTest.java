/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月2日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.inner.task;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class TaskServiceTest extends BaseTest {
  @Autowired
  private TaskService taskService;

  @Test
  public void testPutaway() throws WMSException {
    taskService.putaway("ca5c482b45b2451bacbdce8d51c45485", 0, "02010211", "P000006");
  }

  @Test
  public void testRtnShlef() throws WMSException {
    taskService.rtnShelf("a5d4878260d04cd6857d2ef6fa959c3f", 0, null, "P000015", new BigDecimal(2));
  }
}
