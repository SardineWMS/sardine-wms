/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	Length100Validator.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

/**
 * 名称长度校验器
 * <p>
 *     检验内容：
 *     <li>最大长度100</li>
 * </p>
 * 
 * @author zhangsai
 *
 */
public class Length100Validator extends StringLengthValidator {

    public Length100Validator() {
        super(0, 100);
    }

    @Override
    protected String getDefaultCaption() {
        return "名称";
    }

}
