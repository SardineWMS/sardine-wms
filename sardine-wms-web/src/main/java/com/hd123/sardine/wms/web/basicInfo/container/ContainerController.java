/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ContainerController.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author Jing
 *
 */
@RestController
@RequestMapping("/basicinfo/container")
public class ContainerController extends BaseController {
    @Autowired
    private ContainerService containerService;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "barcode", required = false) String barcode,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "toPosition", required = false) String toPostion) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ContainerService.QUERY_BARCODE_FIELD, barcode);
            definition.put(ContainerService.QUERY_POSITION_FIELD, position);
            definition.put(ContainerService.QUERY_STATE_FIELD,
                    StringUtil.isNullOrBlank(state) ? null : ContainerState.valueOf(state));
            definition.put(ContainerService.QUERY_TOPOSITION_FIELD, toPostion);
            PageQueryResult<Container> result = containerService.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savenew", method = RequestMethod.PUT)
    public @ResponseBody RespObject saveNew(
            @RequestParam(value = "containerTypeUuid") String containerTypeUuid,
            @RequestParam(value = "token") String token) {
        Assert.assertArgumentNotNull(containerTypeUuid, "containerTypeUuid");
        Assert.assertArgumentNotNull(token, "token");
        RespObject resp = new RespObject();
        try {
            containerService.saveNew(containerTypeUuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增容器失败", e.getMessage());
        }
        return resp;
    }
}
