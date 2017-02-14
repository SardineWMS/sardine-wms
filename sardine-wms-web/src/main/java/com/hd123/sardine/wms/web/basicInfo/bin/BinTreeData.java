/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BinTreeData.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.bin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsai
 *
 */
public class BinTreeData implements Serializable {
  private static final long serialVersionUID = -2305322803644086352L;

  private String key;
  private String title;
  private List<BinTreeData> children = new ArrayList<BinTreeData>();

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

  public List<BinTreeData> getChildren() {
    return children;
  }

  public void setChildren(List<BinTreeData> children) {
    this.children = children;
  }
}
