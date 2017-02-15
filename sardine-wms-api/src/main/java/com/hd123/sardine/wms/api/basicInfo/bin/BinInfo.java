/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月9日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 带有子集关系的查询结果
 * 
 * @author zhangsai
 *
 */
public class BinInfo implements Serializable {
  private static final long serialVersionUID = -2804975662207861655L;

  private String key;
  private String title;
  private WrhType type;

  private List<BinInfo> children = new ArrayList<BinInfo>();

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

  public WrhType getType() {
    return type;
  }

  public void setType(WrhType type) {
    this.type = type;
  }

  public List<BinInfo> getChildren() {
    return children;
  }

  public void setChildren(List<BinInfo> children) {
    this.children = children;
  }
}
