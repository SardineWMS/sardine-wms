/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	UserState.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

/**
 * 用户状态
 * <p>
 * 启用状态下的用户可登录系统且使用正常功能<br>
 * 禁用状态则不能登录系统
 * 
 * @author zhangsai
 *
 */
public enum UserState {
    /** 启用 */
    online("启用"),
    /** 停用 */
    offline("停用");

    private String caption;

    private UserState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
