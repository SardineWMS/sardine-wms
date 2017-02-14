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

import java.util.ArrayList;
import java.util.List;

import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 带有子集关系的查询结果
 * 
 * @author zhangsai
 *
 */
public class BinInfo extends Entity {
  private static final long serialVersionUID = -2804975662207861655L;

  private String code;

  private List<BinInfo> childers = new ArrayList<BinInfo>();

  /** 代码，仓位、货区：[code]name, 货道、货架、货位：code */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 当前级别的子集，仓位--货区、货区--货道、货道--货架、货架--货位 */
  public List<BinInfo> getChilders() {
    return childers;
  }

  public void setChilders(List<BinInfo> childers) {
    this.childers = childers;
  }
}
