/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	UserDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月15日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.user.impl;

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.ia.user.UserDao;

/**
 * @author zhangsai
 *
 */
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

  public static final String MAPPER_GETBYCODE = "getByCode";
  public static final String MAPPER_LOGIN = "login";
  public static final String MAPPER_UPDATEPASSWD = "updatePasswd";
  public static final String MAPPER_SAVEUSERROLE = "saveUserRole";
  public static final String MAPPER_REMOVEROLESBYUSER = "removeRolesByUser";

  @Override
  public User getByCode(String userCode) {
    if (StringUtil.isNullOrBlank(userCode))
      return null;
    Map<String, Object> map = ApplicationContextUtil.mapWithParentCompanyUuid();
    map.put("code", userCode);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), userCode);
  }

  @Override
  public User login(String userCode, String passwd) {
    if (StringUtil.isNullOrBlank(userCode) || StringUtil.isNullOrBlank(passwd))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("userCode", userCode);
    map.put("passwd", passwd);

    return getSqlSession().selectOne(generateStatement(MAPPER_LOGIN), map);
  }

  @Override
  public int updatePasswd(String userUuid, String oldPasswd, String newPasswd,
      OperateContext operCtx) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userUuid", userUuid);
    map.put("oldPasswd", oldPasswd);
    map.put("newPasswd", newPasswd);
    map.put("lastModifyInfo", OperateInfo.newInstance(operCtx));
    int i = getSqlSession().update(generateStatement(MAPPER_UPDATEPASSWD), map);
    PersistenceUtils.optimisticVerify(i);
    return i;
  }

  @Override
  public void saveUserRole(String userUuid, String roleUuid) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userUuid", userUuid);
    map.put("roleUuid", roleUuid);
    getSqlSession().insert(generateStatement(MAPPER_SAVEUSERROLE), map);
  }

  @Override
  public void removeRolesByUser(String userUuid) {
    getSqlSession().delete(generateStatement(MAPPER_REMOVEROLESBYUSER), userUuid);
  }
}
