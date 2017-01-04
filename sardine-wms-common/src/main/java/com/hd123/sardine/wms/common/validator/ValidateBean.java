/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	ValidateBean.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

/**
 * 验证中间对象
 *
 * @author zhangsai
 */
public class ValidateBean<S> {

    private String field;
    private S bean;

    public ValidateBean(String field, S bean) {
        setField(field);
        setBean(bean);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public S getBean() {
        return bean;
    }

    public void setBean(S bean) {
        this.bean = bean;
    }

}
