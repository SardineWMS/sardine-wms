/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	OperateContext.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作上下文接口{@link IsOperateContext}的实现。
 * <p>
 * 
 * 该类利用java.util.HashMap实现操作上下文接口{@link IsOperateContext}
 * ，对于扩展属性的取值类型没有任何限制（java.lang.Object的子类）。
 * 
 * @author zhangsai
 * 
 * @param V
 *            扩展属性取值类型，可以不指定，等价于java.lang.Object，即可以被放入任意类型。
 * 
 */
public class OperateContext implements Serializable {

    private static final long serialVersionUID = 3036262989331048433L;

    /**
     * 创建对象，取得当前系统时间作为操作发生时间。
     */
    public OperateContext() {
        this(null, null);
    }

    /**
     * 创建对象，取得当前系统时间作为操作发生时间。
     * 
     * @param operator
     *            操作人对象。
     */
    public OperateContext(Operator operator) {
        this(operator, null);
    }

    /**
     * 创建对象。
     * 
     * @param operator
     *            操作人对象。
     * @param time
     *            指定操作发生时间，传入null将导致取得当前系统时间作为操作时间。
     */
    public OperateContext(Operator operator, Date time) {
        this.time = time == null ? new Date() : (Date) time.clone();
        this.operator = operator;
    }

    private Date time = new Date();
    private Operator operator;

    public Date getTime() {
        return (Date) time.clone();
    }

    /**
     * @param time
     *            传入null等价于指定为当前系统时间。
     */
    public void setTime(Date time) throws UnsupportedOperationException {
        this.time = time == null ? new Date() : (Date) time.clone();
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) throws UnsupportedOperationException {
        this.operator = operator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OperateContext other = (OperateContext) obj;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (operator != null) {
            sb.append(operator.toString());
        }
        sb.append(", ");
        if (time != null) {
            sb.append(time.toString());
        }
        return sb.toString();
    }

}
