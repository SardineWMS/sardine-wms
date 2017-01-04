/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	OperateInfo.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 操作信息
 * 
 * @author zhangsai
 * 
 */
@XmlRootElement
public class OperateInfo implements Serializable, Injectable {

    private static final long serialVersionUID = 5079511842825449614L;

    /**
     * 采用系统时间作为操作发生时间，构造操作信息对象。
     */
    public OperateInfo() {
        this(new Date(), null);
    }

    /**
     * 指定操作人，采用系统时间作为操作发生时间，构造操作信息对象。
     * 
     * @param operator
     */
    public OperateInfo(Operator operator) {
        this(new Date(), operator);
    }

    /**
     * 指定操作人和操作发生时间，构造操作信息对象。
     * 
     * @param time
     * @param operator
     */
    public OperateInfo(Date time, Operator operator) {
        this.time = time == null ? null : (Date) time.clone();
        if (operator == null) {
            this.operator = null;
        } else {
            this.operator = new Operator();
            this.operator.inject(operator);
        }
    }

    /**
     * 指定{@link HasOperateInfo}构造操作信息对象。
     * 
     * @param source
     *            传入null将导致返回null。
     */
    public static OperateInfo newInstance(OperateContext source) {
        if (source == null) {
            return null;
        }
        OperateInfo target = new OperateInfo();
        target.setOperator(source.getOperator());
        target.setTime(source.getTime());
        return target;
    }

    private Date time;
    private Operator operator;

    /**
     * 操作发生时间，通常来说是用户（人）通过界面发起操作请求的时刻。默认取值为对象创建时间。
     */
    public Date getTime() {
        return time == null ? null : (Date) time.clone();
    }

    public void setTime(Date time) {
        this.time = time == null ? null : (Date) time.clone();
    }

    /**
     * 操作人信息。
     */
    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void inject(Object source) throws UnsupportedOperationException {
        if (source == null) {
            return;
        }
        if (source instanceof OperateInfo) {
            setTime(((OperateInfo) source).getTime());
            if (((OperateInfo) source).getOperator() == null) {
                operator = null;
            } else {
                if (operator == null) {
                    operator = new Operator();
                }
                operator.inject(((OperateInfo) source).getOperator());
            }
        }
    }

    @Override
    public OperateInfo clone() {
        OperateInfo other = new OperateInfo();
        other.inject(this);
        return other;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OperateInfo)) {
            return false;
        }
        OperateInfo other = (OperateInfo) obj;
        if (operator == null) {
            if (other.operator != null) {
                return false;
            }
        } else if (!operator.equals(other.operator)) {
            return false;
        }
        if (time == null) {
            if (other.time != null) {
                return false;
            }
        } else if (!time.equals(other.time)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (operator != null) {
            sb.append(operator.toString());
        }
        sb.append("\n");
        if (time != null) {
            sb.append(time.toString());
        }
        return sb.toString();
    }

}
