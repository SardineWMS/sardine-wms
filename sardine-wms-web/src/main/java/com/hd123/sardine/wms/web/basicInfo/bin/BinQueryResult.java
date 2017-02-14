/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BinQueryResult.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.bin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author zhangsai
 *
 */
public class BinQueryResult implements Serializable {
  private static final long serialVersionUID = 4475194825649953084L;

  private PageQueryResult<Bin> pageData = new PageQueryResult<Bin>();
  private List<BinTreeData> treeData = new ArrayList<BinTreeData>();

  public PageQueryResult<Bin> getPageData() {
    return pageData;
  }

  public void setPageData(PageQueryResult<Bin> pageData) {
    this.pageData = pageData;
  }

  public List<BinTreeData> getTreeData() {
    return treeData;
  }

  public void setTreeData(List<BinTreeData> treeData) {
    this.treeData = treeData;
  }
}
