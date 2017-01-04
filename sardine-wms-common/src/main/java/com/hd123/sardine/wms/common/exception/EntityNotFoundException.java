/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	rumba-commons-biz
 * 文件名：	EntityNotFoundException.java
 * 模块说明：	
 * 修改历史：
 * 2013-3-6 - lxm - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

import com.hd123.rumba.commons.i18n.DefaultStringValue;
import com.hd123.rumba.commons.i18n.Resources;
import com.hd123.sardine.wms.common.utils.EntityUtil;

/**
 * 意味着指定的实体不存在。
 * 
 * @author lxm
 * @since 1.3
 * 
 */
public class EntityNotFoundException extends EntityInvalidException {

  private static final long serialVersionUID = -4023193186352680081L;

  /**
   * @param entityName
   *          实体名。
   * @param keyValues
   *          表示实体对象的键与值的数组。一般情况下，按照：<br>
   *          <i>键1, 值1, 键2, 值2, ...</i><br>
   *          作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
   */
  public static EntityNotFoundException create(String entityName, Object... keyValues) {
    return create2(entityName, null, keyValues);
  }

  /**
   * @param entityName
   *          实体名。
   * @param caught
   *          捕捉的异常。
   * @param keyValues
   *          表示实体对象的键与值的数组。一般情况下，按照：<br>
   *          <i>键1, 值1, 键2, 值2, ...</i><br>
   *          作为特例，如果数组长度为1，则将唯一的数组元素解释为唯一的值。允许数组长度为0。
   */
  public static EntityNotFoundException create2(String entityName, Throwable caught,
      Object... keyValues) throws IllegalArgumentException {
    EntityNotFoundException exception = new EntityNotFoundException(buildMessage(entityName,
        keyValues), caught);
    exception.inject(entityName, keyValues);
    return exception;
  }

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable caught) {
    super(message, caught);
  }

  private static String buildMessage(String entityName, Object... keyValues) {
    String entity = EntityUtil.toEntityString(entityName, keyValues);
    return MessageFormat.format(R.R.entityNotFound(), entity);
  }
  
  public static interface R {
    public static final R R = Resources.create(R.class);
    
    @DefaultStringValue("指定实体“{0}”不存在。")
    String entityNotFound();
  }
}
