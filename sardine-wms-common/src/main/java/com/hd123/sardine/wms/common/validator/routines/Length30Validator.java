/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	Length30Validator.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

/**
 * 代码长度校验器
 * <p>
 * 检验内容：
 * <li>最大长度30</li>
 * </p>
 * 
 * @author zhangsai
 *
 */
public class Length30Validator extends StringLengthValidator {

    public Length30Validator() {
        super(0, 30);
    }

    @Override
    protected String getDefaultCaption() {
        return "代码";
    }
}
