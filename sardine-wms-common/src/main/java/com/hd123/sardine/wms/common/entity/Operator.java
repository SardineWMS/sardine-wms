/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	Operator.java
 * 模块说明：	
 * 修改历史：
 * 2012-2-29 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 操作人
 * 
 * @author zhangsai
 * 
 */
@XmlRootElement
public class Operator implements Serializable, Injectable {

    private static final long serialVersionUID = 1129343019741384454L;

    /**
     * 构造操作人对象。
     */
    public Operator() {
        // Do Nothing
    }

    /**
     * 指定操作人标识和全名构造操作人对象。
     * 
     * @param id
     * @param code
     * @param fullName
     */
    public Operator(String id, String code, String fullName) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
    }

    private String id;
    private String code;
    private String fullName;

    /**
     * 操作人标识，通常为操作人的登录名。
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public void inject(Object source) {
        if (source == null) {
            return;
        }
        if (source instanceof Operator) {
            id = ((Operator) source).getId();
            code = ((Operator) source).getCode();
            fullName = ((Operator) source).getFullName();
        }
    }

    @Override
    public Operator clone() {
        Operator other = new Operator();
        other.inject(this);
        return other;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        if (!(obj instanceof Operator)) {
            return false;
        }
        Operator other = (Operator) obj;
        if (fullName == null) {
            if (other.fullName != null) {
                return false;
            }
        } else if (!fullName.equals(other.fullName)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (fullName != null) {
            sb.append(fullName);
        }
        sb.append("[");
        if (code != null) {
            sb.append(code);
        }
        sb.append("]");
        return sb.toString();
    }

}
