/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BinTypeInsertValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.bintype.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 
 * @author yangwenzhu
 *
 */
public class BinTypeInsertValidateHandler extends GenericValidateHandler<BinType> {

    public static final String KEY_CODEEXISTS_BINTYPE = "codeExists_binType";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Autowired
    private Validator<ValidateBean<IsEntity>> nullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> numberValidator;

    @Autowired
    private Validator<ValidateBean<Object>> ratioValidator;

    @Autowired
    private Validator<ValidateBean<Object>> nonnegativeNumberValidator;

    @Override
    protected void prepareValidators(BinType bean) {
        on("货位类型", bean, notNullValidator);
        on("货位类型代码", bean.getCode(), notNullValidator);
        on("货位类型代码", bean.getCode(), length30Validator);
        on("货位类型名称", bean.getName(), notNullValidator);
        on("货位类型名称", bean.getName(), length100Validator);
        // on("货位类型长度", bean.getLength(), notNullValidator);
        // on("货位类型长度", bean.getLength(), numberValidator);
        // on("货位类型宽度", bean.getWidth(), notNullValidator);
        // on("货位类型宽度", bean.getWidth(), numberValidator);
        // on("货位类型高度", bean.getHeight(), notNullValidator);
        // on("货位类型高度", bean.getHeight(), numberValidator);
        // on("容积率", bean.getPlotRatio(), notNullValidator);
        // on("容积率", bean.getPlotRatio(), ratioValidator);
        on("承重", bean.getBearing(), notNullValidator);
        on("承重", bean.getBearing(), nonnegativeNumberValidator);

        BinType binType = (BinType) getAttribute(KEY_CODEEXISTS_BINTYPE);
        on("货位类型代码" + bean.getCode(), binType, nullValidator);
    }

}
