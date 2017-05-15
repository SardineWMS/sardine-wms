/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	UtilController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月10日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.util;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/common/helper")
public class UtilController extends BaseController {

    @RequestMapping(value = "/qtyToCaseQtyStr", method = RequestMethod.PUT)
    public @ResponseBody RespObject qtyToCaseQtyStr(
            @RequestParam(value = "qty", required = true) BigDecimal qty,
            @RequestParam(value = "qpcStr", required = true) String qpcStr) {
        RespObject resp = new RespObject();
        try {
            String caseQtyStr = QpcHelper.qtyToCaseQtyStr(qty, qpcStr);
            resp.setObj(caseQtyStr);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("件数转化失败！", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/caseQtyStrAdd", method = RequestMethod.PUT)
    public @ResponseBody RespObject caseQtyStrAdd(
            @RequestParam(value = "addend", required = true) String addend,
            @RequestParam(value = "augend", required = true) String augend) {
        RespObject resp = new RespObject();
        try {
            String add = QpcHelper.caseQtyStrAdd(addend, augend);
            resp.setObj(add);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("件数相加失败", e.getMessage());
        }
        return resp;
    }
}
