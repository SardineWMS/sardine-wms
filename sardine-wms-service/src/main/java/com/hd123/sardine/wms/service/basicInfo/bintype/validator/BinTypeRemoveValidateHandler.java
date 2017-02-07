/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BinTypeRemoveValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.bintype.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author yangwenzhu
 *
 */
public class BinTypeRemoveValidateHandler extends GenericValidateHandler<String> {

    public static final String KEY_OPERATOR_BINTYPE = "operator_bintype";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Autowired
    private Validator<ValidateBean<HasVersion>> versionValidator;

    @Override
    protected void prepareValidators(String bean) {
        on("货位类型", bean, notNullValidator);

        BinType binType = (BinType) getAttribute(KEY_OPERATOR_BINTYPE);
        on("操作的货位类型", binType, entityNotNullValidator);

        on(binType, versionValidator);

    }

}
