/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ReasonConfigController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月22日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.config.reasonconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonConfigService;
import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonType;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/basicinfo/reasonConfig")
public class ReasonConfigController extends BaseController {
    @Autowired
    private ReasonConfigService service;

    @RequestMapping(value = "/setreasonconfig", method = RequestMethod.PUT)
    public @ResponseBody RespObject setReasonConfig(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "reasonType", required = true) String reasonType,
            @RequestBody List<String> reasons) {
        RespObject resp = new RespObject();
        try {
            ReasonType type = ReasonType.valueOf(reasonType);
            service.setReasonConfig(type, reasons);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("原因配置失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryreasons", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "reasonType", required = true) String reasonType) {
        RespObject resp = new RespObject();
        try {

            ReasonType type = ReasonType.valueOf(reasonType);
            List<String> result = service.queryReasons(type);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
        }
        return resp;
    }

}
