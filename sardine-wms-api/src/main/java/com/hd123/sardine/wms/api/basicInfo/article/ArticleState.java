/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleState.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

/**
 * 商品状态
 * 
 * @author zhangsai
 *
 */
public enum ArticleState {
    /** 正常 */
    normal("正常");

    private String caption;

    private ArticleState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
