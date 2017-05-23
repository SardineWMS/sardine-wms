/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	LoginService.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.login;

import com.hd123.sardine.wms.api.ia.user.RegisterInfo;
import com.hd123.sardine.wms.api.ia.user.RegisterUser;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 登录服务
 * <p>
 * <ul>
 * <li>登录获取用户信息</li>
 * <li>注册新用户获取用户信息</li>
 * <li>修改密码</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public interface LoginService {

  /**
   * 登录
   * 
   * @param userCode
   *          登录名，not null
   * @param passwd
   *          密码
   * @return 用户信息
   * @throws IllegalArgumentException
   *           登录名为空时抛出
   * @throws WMSException
   *           登录验证失败时抛出
   */
  UserInfo login(String userCode, String passwd) throws IllegalArgumentException, WMSException;

  /**
   * 注册新用户
   * <p>
   * 并分配新的组织id
   * 
   * @param registerUser
   *          not null
   * @return 用户信息
   * @throws IllegalArgumentException
   *           参数为空时抛出
   * @throws WMSException
   *           用户名重复时抛出异常
   */
  UserInfo register(RegisterUser registerUser) throws IllegalArgumentException, WMSException;

  /**
   * 企业注册
   * <p>
   * 通过该注册将注册一个企业级用户，并自动给该用户添加相应的权限
   * 
   * @param registerInfo
   * @return
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  UserInfo register(RegisterInfo registerInfo) throws IllegalArgumentException, WMSException;

  /**
   * 修改密码
   * 
   * @param userUuid
   *          被修改用户的uuid，not null
   * @param oldPasswd
   *          用户原密码，not null
   * @param newPasswd
   *          用户新密码，not null
   * @return 返回修改后的用户信息
   * @throws IllegalArgumentException
   *           参数为空
   * @throws EntityNotFoundException
   *           用户不存在
   * @throws WMSException
   *           用户不存在、原密码不符合
   */
  UserInfo updatePasswd(String userUuid, String oldPasswd, String newPasswd)
      throws IllegalArgumentException, EntityNotFoundException, WMSException;
}
