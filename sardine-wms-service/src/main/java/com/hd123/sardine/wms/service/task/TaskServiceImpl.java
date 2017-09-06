/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.task.TaskDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author zhangsai
 *
 */
public class TaskServiceImpl extends BaseWMSService implements TaskService {

  @Autowired
  private TaskDao taskDao;

  @Autowired
  private TaskVerifier taskVerifier;

  @Autowired
  private TaskHandler taskHandler;

  @Autowired
  private EntityLogger logger;

  @Autowired
  private PickUpBillService pickUpBillService;

  @Autowired
  private RplBillService rplBillService;

  @Override
  public void insert(List<Task> tasks)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(tasks, "tasks");

    if (tasks.isEmpty())
      return;

    for (Task task : tasks) {
      taskVerifier.verifySourceStock(task);

      task.setUuid(UUIDGenerator.genUUID());
      task.setTaskNo(billNumberGenerator.allocateNextBillNumber(Task.class.getSimpleName()));
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setCreateTime(new Date());
      task.setState(TaskState.Initial);
      taskDao.insert(task);

      taskHandler.lockStock(task);

      logger.injectContext(this, task.getUuid(), Task.class.getName(),
          ApplicationContextUtil.getOperateContext());
      logger.log(EntityLogger.EVENT_ADDNEW, "新增" + task.getTaskType().getCaption());
    }
  }

  @Override
  public void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    PickUpBill pickUpBill = pickUpBillService.get(uuid);
    if (pickUpBill != null)
      throw new WMSException("拣货指令不能作废！");
    RplBill rplBill = rplBillService.get(uuid);
    if (rplBill != null)
      throw new WMSException("补货指令不能作废！");
    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("作废的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());
    if (task.getTaskType().equals(TaskType.Putaway)
        || task.getTaskType().equals(TaskType.RtnPutaway))
      throw new WMSException("上架或者退仓上架指令不能作废！");
    if (TaskState.Initial.equals(task) == false)
      throw new WMSException("当前状态的指令不能作废");

    task.setState(TaskState.Aborted);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    taskHandler.releaseStock(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废" + task.getTaskType().getCaption());
  }

  @Override
  public void putaway(String uuid, long version, String toBinCode, String toContainerBarcode)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null || (TaskType.RtnPutaway.equals(task.getTaskType()) == false
        && TaskType.Putaway.equals(task.getTaskType()) == false))
      throw new WMSException(
          "指定的指令不存在或不是" + TaskType.RtnPutaway.getCaption() + "、" + TaskType.Putaway.getCaption());
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());
    taskVerifier.verifyAndRefreshPutawayTask(task, toBinCode, toContainerBarcode);

    task.setState(TaskState.Finished);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setRealQty(task.getQty());
    task.setRealCaseQtyStr(task.getCaseQtyStr());
    task.setEndOperateTime(new Date());
    task.setType(OperateMode.ManualBill);
    taskDao.update(task);

    taskHandler.shiftStock(task);
    taskHandler.manageBinAndContainer(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "执行上架指令");
  }

  @Override
  public PageQueryResult<TaskView> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<TaskView> pgr = new PageQueryResult<TaskView>();
    List<TaskView> list = taskDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public void move(String uuid, long version, BigDecimal qty) {

  }

  @Override
  public void rpl(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    taskVerifier.verifyAndRefreshTaskState(task);

    task.setState(TaskState.Finished);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setRealQty(task.getQty());
    task.setRealCaseQtyStr(task.getCaseQtyStr());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    taskHandler.shiftStock(task);
    taskHandler.manageBinAndContainer(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "执行上架指令");
  }

  @Override
  public void rtnShelf(String uuid, long version, String binCode, String containerBarcode,
      BigDecimal qty) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    taskVerifier.verifyAndRefreshUnShelveTask(task, binCode, containerBarcode);

    task.setState(TaskState.Finished);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    if (qty != null)
      task.setRealQty(qty);
    else
      task.setRealQty(qty);
    task.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getRealQty(), task.getQpcStr()));
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    taskHandler.shiftStock(task);
    taskHandler.manageBinAndContainer(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "执行上架指令");
  }

  @Override
  public Task get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return taskDao.get(uuid);
  }
}
