/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	UCN.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 接口{@link HasUCN}的默认实现。
 * 
 * @author zhangsai
 * 
 */
@XmlRootElement
public class UCN extends Entity implements HasUCN {

    private static final long serialVersionUID = -2994021327618598476L;

    /**
     * 构造对象。
     */
    public UCN() {
        // Do Nothing
    }

    /**
     * 指定全局唯一标识、代码和名称构造对象。
     * 
     * @param uuid
     * @param code
     * @param name
     */
    public UCN(String uuid, String code, String name) {
        setUuid(uuid);
        this.code = code;
        this.name = name;
    }

    /**
     * 从指定对象中获取数据构造对象。
     * 
     * @param source
     *            传入null将导致返回null。
     */
    public static UCN newInstance(Object source) {
        if (source == null) {
            return null;
        }
        UCN target = new UCN();
        target.inject(source);
        return target;
    }

    private String code;
    private String name;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void inject(Object source) {
        if (source == null) {
            return;
        }
        if (source instanceof HasUuid) {
            setUuid(((HasUuid) source).getUuid());
        }
        if (source instanceof HasCode) {
            this.code = ((HasCode) source).getCode();
        }
        if (source instanceof HasName) {
            this.name = ((HasName) source).getName();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        String uuid = getUuid();
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
        if (!(obj instanceof UCN)) {
            return false;
        }
        UCN other = (UCN) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (getUuid() == null) {
            if (other.getUuid() != null) {
                return false;
            }
        } else if (!getUuid().equals(other.getUuid())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append("[");
        sb.append(code);
        sb.append("]");
        return sb.toString();
    }
}
