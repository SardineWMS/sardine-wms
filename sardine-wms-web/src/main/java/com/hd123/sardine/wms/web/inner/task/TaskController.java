/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	TaskController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月25日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.inner.task;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillFilter;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.HandoverTaskFilter;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillService;
import com.hd123.sardine.wms.api.tms.shipbill.ShipTaskFilter;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/inner/task")
public class TaskController extends BaseController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RplBillService rplBillService;

	@Autowired
	private PickUpBillService pickUpBillService;

	@Autowired
	private ReturnSupplierBillService returnSupplierBillService;

	@Autowired
	private ShipBillService shipBillService;

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public @ResponseBody RespObject query(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "articleCode", required = false) String articleCode,
			@RequestParam(value = "fromContainerBarcode", required = false) String fromContainerBarcode,
			@RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
			@RequestParam(value = "operator", required = false) String operator,
			@RequestParam(value = "taskType", required = true) String taskType,
			@RequestParam(value = "states", required = false) String state,
			@RequestParam(value = "sourceBillNumber", required = false) String sourceBillNumber,
			@RequestParam(value = "operatorType", required = false) String type,
			@RequestParam(value = "fromBinCode", required = false) String fromBinCode,
			@RequestParam(value = "customer", required = false) String customer,
			@RequestParam(value = "deliveryType", required = false) String deliveryType,
			@RequestParam(value = "pickArea", required = false) String pickArea,
			@RequestParam(value = "pickType", required = false) String pickMethod,
			@RequestParam(value = "owner", required = false) String owner,
			@RequestParam(value = "billNumber", required = false) String billNumber) {
		RespObject resp = new RespObject();
		try {
			PageQueryDefinition definition = new PageQueryDefinition();
			definition.setPage(page);
			definition.setPageSize(pageSize);
			definition.setSortField(sort);
			definition.setOrderDir(OrderDir.valueOf(sortDirection));
			if (StringUtil.isNullOrBlank(taskType) == false)
				definition.put(TaskService.QUERY_FIELD_TASKTYPE, TaskType.valueOf(taskType));
			definition.put(TaskService.QUERY_FIELD_STATE, state);
			definition.put(TaskService.QUERY_FIELD_ARTICLECODE, articleCode);
			definition.put(TaskService.QUERY_FIELD_FROMCONTAINERBARCODE, fromContainerBarcode);
			definition.put(TaskService.QUERY_FIELD_TOCONTAINERBARCODE, toContainerBarcode);
			definition.put(TaskService.QUERY_FIELD_OPERATOR, operator);
			definition.put(TaskService.QUERY_FIELD_TYPE, type);
			definition.put(TaskService.QUERY_FIELD_SOURCEBILL, sourceBillNumber);
			definition.put(TaskService.QUERY_FIELD_FROMBINCODE, fromBinCode);
			definition.put(TaskService.QUERY_FIELD_CUSTOMER, customer);
			definition.put(TaskService.QUERY_FIELD_DELIVERYTYPE, deliveryType);
			definition.put(TaskService.QUERY_FIELD_PICKAREA, pickArea);
			definition.put(TaskService.QUERY_FIELD_PICKMETHOD, pickMethod);
			definition.put(TaskService.QUERY_FIELD_BILLNUMBER, billNumber);
			definition.put(TaskService.QUERY_FIELD_OWNER, owner);
			definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			PageQueryResult<?> result = null;
				result = taskService.query(definition);
			resp.setObj(result);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}
	
	@RequestMapping(value = "/queryPickUpTask", method = RequestMethod.POST)
	public @ResponseBody RespObject queryPickUpTask(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "articleCode", required = false) String articleCodeLike,
			@RequestParam(value = "fromContainerBarcode", required = false) String fromContainerBarcodeLike,
			@RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcodeLike,
			@RequestParam(value = "picker", required = false) String pickerUuidEquals,
			@RequestParam(value = "states", required = false) String stateEquals,
			@RequestParam(value = "sourceBillNumber", required = false) String waveBillNumberLike,
			@RequestParam(value = "method", required = false) String methodEquals,
			@RequestParam(value = "fromBinCode", required = false) String fromBinCodeLike,
			@RequestParam(value = "customer", required = false) String customerUuidEquals,
			@RequestParam(value = "deliveryType", required = false) String deliveryTypeEquals,
			@RequestParam(value = "deliverySystem", required = false) String deliverySystemEquals,
			@RequestParam(value = "pickArea", required = false) String pickAreaEquals,
			@RequestParam(value = "pickType", required = false) String pickTypeEquals,
			@RequestParam(value = "billNumber", required = false) String billNumberLike) {
		RespObject resp = new RespObject();
		try {
			//PageQueryDefinition definition = new PageQueryDefinition();
			PickUpBillFilter filter = new PickUpBillFilter();
			filter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			filter.setPage(page);
			filter.setPageSize(pageSize);
			filter.setSortField(sort);
			filter.setOrderDir(OrderDir.valueOf(sortDirection));
			filter.setArticleCodeLike(articleCodeLike);
			filter.setBillNumberLike(billNumberLike);
			filter.setCustomerUuidEquals(customerUuidEquals);
			filter.setDeliverySystemEquals(deliverySystemEquals);
			filter.setDeliveryTypeEquals(deliveryTypeEquals);
			filter.setFromBinCodeLike(fromBinCodeLike);
			filter.setFromContainerBarcodeLike(fromContainerBarcodeLike);
			filter.setMethodEquals(methodEquals);
			filter.setPickAreaEquals(pickAreaEquals);
			filter.setPickerUuidEquals(pickerUuidEquals);
			filter.setPickTypeEquals(pickTypeEquals);
			filter.setStateEquals(stateEquals);
			filter.setToContainerBarcodeLike(toContainerBarcodeLike);
			filter.setWaveBillNumberLike(waveBillNumberLike);
//			definition.setPage(page);
//			definition.setPageSize(pageSize);
//			definition.setSortField(sort);
//			definition.setOrderDir(OrderDir.valueOf(sortDirection));
//
//			definition.put(PickUpBillService.FIELD_QUERY_ARTICLECODELIKE,articleCodeLike);
//			definition.put(PickUpBillService.FIELD_QUERY_BILLNUMBERLIKE,billNumberLike);
//			definition.put(PickUpBillService.FIELD_QUERY_CUSTOMERUUIDEQUALS,customerUuidEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_DELIVERYSYSTEMEQUALS,deliverySystemEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_DELIVERYTYPEEQUALS,deliveryTypeEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_FROMBINCODELIKE,fromBinCodeLike);
//			definition.put(PickUpBillService.FIELD_QUERY_FROMCONTAINERBARCODELIKE,fromContainerBarcodeLike);
//			definition.put(PickUpBillService.FIELD_QUERY_METHODEQUALS,methodEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_PICKAREAEQUALS,pickAreaEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_PICKERUUIDEQUALS,pickerUuidEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_PICKTYPEEQUALS,pickTypeEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_STATEEQUALS,stateEquals);
//			definition.put(PickUpBillService.FIELD_QUERY_TOCONTAINERBARCODELIKE,toContainerBarcodeLike);
//			definition.put(PickUpBillService.FIELD_QUERY_WAVEBILLNUMBERLIKE,waveBillNumberLike);		
			//definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			//PageQueryResult<?> result = pickUpBillService.query(definition);
			PageQueryResult<?> result = pickUpBillService.query(filter);
			resp.setObj(result);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryhandovertasks", method = RequestMethod.GET)
	public @ResponseBody RespObject queryHandoverTasks(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "supplierCodeLike", required = false) String supplierCodeLike,
			@RequestParam(value = "binCodeLike", required = false) String binCodeLike,
			@RequestParam(value = "containerBarcodeLike", required = false) String containerBarcodeLike,
			@RequestParam(value = "articleCodeLike", required = false) String articleCodeLike) {
		RespObject resp = new RespObject();
		try {
			HandoverTaskFilter filter = new HandoverTaskFilter();
			filter.setArticleCodeLike(articleCodeLike);
			filter.setSupplierCodeLike(supplierCodeLike);
			filter.setBinCodeLike(binCodeLike);
			filter.setContainerBarcodeLike(containerBarcodeLike);
			filter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			filter.setPage(page);
			filter.setPageSize(pageSize);
			filter.setSortField(sort);
			filter.setOrderDir(OrderDir.valueOf(sortDirection));

			PageQueryResult<ReturnSupplierBillItem> tasks = returnSupplierBillService.queryWaitHandoverItems(filter);
			for (ReturnSupplierBillItem item : tasks.getRecords())
				item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
			resp.setObj(tasks);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryshiptasks", method = RequestMethod.GET)
	public @ResponseBody RespObject queryShipTasks(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "binCodeLike", required = false) String binCodeLike,
			@RequestParam(value = "containerBarcodeLike", required = false) String containerBarcodeLike,
			@RequestParam(value = "articleCodeLike", required = false) String articleCodeLike,
			@RequestParam(value = "deliveryType", required = false) String deliveryType,
			@RequestParam(value = "deliverySystem", required = false) String deliverySystem,
			@RequestParam(value = "line", required = false) String line,
			@RequestParam(value = "customerCodeLike", required = false) String customerCodeLike) {
		RespObject resp = new RespObject();
		try {
			ShipTaskFilter filter = new ShipTaskFilter();
			filter.setArticleCodeLike(articleCodeLike);
			filter.setBinCodeLike(binCodeLike);
			filter.setContainerBarcodeLike(containerBarcodeLike);
			filter.setCustomerCodeLike(customerCodeLike);
			filter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			filter.setPage(page);
			filter.setPageSize(pageSize);
			filter.setSortField(sort);
			filter.setOrderDir(OrderDir.valueOf(sortDirection));
			filter.setDeliveryType(deliveryType);
			filter.setLine(line);
			filter.setDeliverySystem(deliverySystem);
			PageQueryResult<ShipBillContainerStock> tasks = shipBillService.queryWaitShipStocks(filter);
			for (ShipBillContainerStock item : tasks.getRecords())
				item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
			resp.setObj(tasks);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/abort", method = RequestMethod.PUT)
	public @ResponseBody RespObject abort(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "version", required = true) long version) {
		RespObject resp = new RespObject();
		try {
			taskService.abort(uuid, version);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("作废指令失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/putaway", method = RequestMethod.PUT)
	public @ResponseBody RespObject putaway(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "toBinCode", required = true) String toBinCode,
			@RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
			@RequestParam(value = "version", required = true) long version) {
		RespObject resp = new RespObject();
		try {
			taskService.putaway(uuid, version, toBinCode, toContainerBarcode);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("收货上架失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/rpl", method = RequestMethod.PUT)
	public @ResponseBody RespObject rpl(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "rplBillUuid", required = true) String rplBillUuid,
			@RequestParam(value = "version", required = true) long version) {
		RespObject resp = new RespObject();

		try {
			rplBillService.rpl(rplBillUuid, version, ApplicationContextUtil.getLoginUser());
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("补货失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/rtnshelf", method = RequestMethod.PUT)
	public @ResponseBody RespObject rtnShelf(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "toBinCode", required = true) String toBinCode,
			@RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
			@RequestParam(value = "version", required = true) long version,
			@RequestParam(value = "qty", required = true) BigDecimal qty) {
		RespObject resp = new RespObject();
		try {
			taskService.rtnShelf(uuid, version, toBinCode, toContainerBarcode, qty);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("收货上架失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	public @ResponseBody RespObject execute(@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "version", required = true) long version) {
		RespObject resp = new RespObject();
		try {
			Task task = taskService.get(uuid);
			if (Objects.isNull(task))
				throw new WMSException(MessageFormat.format("要执行的指令{0}不存在", uuid));
			if (TaskType.Putaway.equals(task.getType()))
				taskService.putaway(uuid, version, task.getToBinCode(), task.getToContainerBarcode());
			if (TaskType.Move.equals(task.getType()))
				taskService.move(uuid, version, task.getQty());
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
			return resp;
		} catch (Exception e) {
			return new ErrorRespObject("指令执行失败：" + e.getMessage());
		}

	}

	@RequestMapping(value = "/batchpick", method = RequestMethod.POST)
	public @ResponseBody RespObject pick(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "toBinCode", required = true) String toBinCode,
			@RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
			@RequestBody List<String> pickItemUuids) {
		RespObject resp = new RespObject();
		try {
			pickUpBillService.pick(pickItemUuids, toBinCode, toContainerBarcode, ApplicationContextUtil.getLoginUser());
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("拣货失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/handover", method = RequestMethod.POST)
	public @ResponseBody RespObject handover(@RequestParam(value = "token", required = true) String token,
			@RequestBody List<ReturnSupplierBillItem> returnHandoverItems) {
		RespObject resp = new RespObject();
		try {
			returnSupplierBillService.generateReturnSupplierBill(returnHandoverItems);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("交接失败：" + e.getMessage());
		}
		return resp;
	}

}
