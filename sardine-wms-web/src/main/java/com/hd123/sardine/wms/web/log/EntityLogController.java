/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	EntityLogController.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月23日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.log.EntityLog;
import com.hd123.sardine.wms.api.log.EntityLogService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 实体日志控制管理层
 * 
 * @author fanqingqing
 */
@RestController
@RequestMapping("/log/entitylog")
public class EntityLogController extends BaseController {

  @Autowired
  private EntityLogService service;

  @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = false) String token,
      @RequestParam(value = "entityUuid", required = false) String entityUuid) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(EntityLogService.QUERY_ENTITYUUID_FIELD, entityUuid);
      PageQueryResult<EntityLog> result = service.query(definition);

      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败：" + e.getMessage());
    }
    return resp;
  }

}
