package com.hd123.sardine.wms.common.utils;

/**
 * 序列值生成器 | 接口
 * <p>
 * 本工具类提供了生成唯一序列值的功能。
 * <p>
 * 当序列存在时，自动取得序列的下一个值，如果序列不存在，则自动根据传入的序列定义信息创建序列并返回序列的下一个值。
 * <p>
 * 针对于不同数据库采用不同的实现方式：
 * <li>当使用ORACLE数据库时，利用sequence实现，sequence的名称为h5cseq<dbid>，
 * 其中dbid是调用者指定的序列唯一标识，长度不能超过18位。
 * <li>当使用MSSQL数据库时，利用表的自增字段实现，table的名称为h5cseq<dbid>， 各属性含义同ORACLE情况。
 * <p>
 * 暂时不支持除两种以外的的数据库。
 * 
 * @author zhangsai
 * 
 */
public interface ISequenceGenerator {

  /**
   * 取序列的下一个值。
   * 
   * @param seq
   *          序列定义信息 ，如果不存在序列，则会自动根据此信息创建序列，否则直接取序列值，not null
   * @return 返回序列的下一个值。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  public long nextValue(SequenceSpec seq);

  /**
   * 取序列的当前值
   * 
   * @param seq
   *          序列定义信息 ，not null 扑捉异常后，返回 0；
   * @return 序列的当前值。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  public long currentValue(SequenceSpec seq);

}
