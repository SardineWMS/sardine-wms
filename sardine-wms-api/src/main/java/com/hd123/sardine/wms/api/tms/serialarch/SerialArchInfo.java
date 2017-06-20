/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SerialArchInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.serialarch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 构造树形结构对象，其他请使用{@link SerialArch}
 * 
 * @author yangwenzhu
 *
 */
public class SerialArchInfo implements Serializable {
    private static final long serialVersionUID = 2473097770572410708L;
    private String key;
    private String title;

    private List<SerialArchInfo> children = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SerialArchInfo> getChildren() {
        return children;
    }

    public void setChildren(List<SerialArchInfo> children) {
        this.children = children;
    }
}
