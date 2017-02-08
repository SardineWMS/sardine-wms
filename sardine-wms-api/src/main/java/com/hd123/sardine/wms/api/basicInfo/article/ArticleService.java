/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleService.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月30日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 基本资料-商品 服务接口
 * <p>
 * 包括：
 * <ul>
 * <li>新增商品</li>
 * <li>编辑商品</li>
 * <li>根据商品uuid查询</li>
 * <li>根据商品code查询</li>
 * <li>根据商品barcode查询</li>
 * <li>分页查询</li>
 * <li>新增商品供应商</li>
 * <li>删除商品供应商</li>
 * <li>指定商品供应商为默认供应商</li>
 * <li>新增商品规格</li>
 * <li>删除商品规格</li>
 * <li>指定商品规格为默认规格</li>
 * <li>新增商品条码</li>
 * <li>删除商品条码</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public interface ArticleService {

  /** 查询条件 */
  public static final String QUERY_CODE_FIELD = "code";
  public static final String QUERY_NAME_FIELD = "name";
  public static final String QUERY_STATE_FIELD = "state";

  /** 排序字段 */
  public static final String ORDER_CODE_FIELD = "code";
  public static final String ORDER_NAME_FIELD = "name";

  /**
   * 新增商品
   * <p>
   * 商品代码不允许重复
   * 
   * @param article
   *          商品，not null
   * @param operCtx
   *          操作信息，not null
   * @return 商品UUID
   * @throws IllegalArgumentException
   *           参数为空或者不合法
   * @throws WMSException
   *           代码重复
   */
  String insert(Article article, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 编辑商品
   * <p>
   * 商品代码不允许重复
   * 
   * @param article
   *          商品，not null
   * @param operCtx
   *          操作信息，not null
   * @return 商品UUID
   * @throws IllegalArgumentException
   *           参数为空或者不合法
   * @throws WMSException
   *           代码重复
   */
  void update(Article article, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 根据uuid查询商品
   * 
   * @param uuid
   *          uuid
   * @return 商品，不存在则返回null
   */
  Article get(String uuid);

  /**
   * 根据code查询商品
   * 
   * @param code
   *          代码
   * @param companyUuid
   *          组织id
   * @return 商品，不存在则返回null
   */
  Article getByCode(String code, String companyUuid);

  /**
   * 根据barcode查询商品
   * 
   * @param barcode
   *          条码
   * @param companyUuid
   *          组织id
   * @return 商品，不存在则返回null
   */
  Article getByBarcode(String barcode, String companyUuid);

  /**
   * 分页查询商品
   * 
   * @param definition
   *          查询条件，not null
   * @return 分页结果集
   * @throws IllegalArgumentException
   */
  PageQueryResult<Article> query(PageQueryDefinition definition) throws IllegalArgumentException;

  /**
   * 新增商品规格
   * <p>
   * 如果{@link ArticleQpc#getQpcStr()}对应的商品已存在，则抛出{@link WMSException}<br>
   * 如果{@link ArticleQpc#getUuid()}对应的商品已存在，则先删除原规格，再插入新规格<br>
   * 新增规格都是非默认规格
   * 
   * @param qpc
   *          规格，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void insertArticleQpc(ArticleQpc qpc, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 新增商品条码
   * <p>
   * 如果{@link ArticleBarcode#getBarcode()}对应的商品已存在，则抛出{@link WMSException}<br>
   * 如果{@link ArticleBarcode#getUuid()}对应的商品已存在，则先删除原条码，再插入新条码<br>
   * 
   * @param barcode
   *          条码，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void insertArticleBarcode(ArticleBarcode barcode, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 新增商品规格
   * <p>
   * 如果{@link ArticleSupplier#getSupplierUuid()}对应的商品已存在，则抛出{@link WMSException}
   * <br>
   * 如果{@link ArticleSupplier#getUuid()}对应的商品已存在，则先删除原供应商，再插入新供应商<br>
   * 
   * @param supplier
   *          商品供应商，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void insertArticleSupplier(ArticleSupplier supplier, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 删除指定uuid的商品规格，不存在时直接返回
   * 
   * @param articleUuid
   *          商品uuid，not null
   * @param qpcUuid
   *          规格uuid，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void deleteArticleQpc(String articleUuid, String qpcUuid, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 删除指定uuid的商品条码，不存在时直接返回
   * 
   * @param articleUuid
   *          商品uuid，not null
   * @param barcodeUuid
   *          条码uuid，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void deleteArticleBarcode(String articleUuid, String barcodeUuid, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 删除指定uuid的商品供应商，不存在时直接返回
   * 
   * @param articleUuid
   *          商品uuid，not null
   * @param articleSupplierUuid
   *          商品供应商uuid，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void deleteArticleSupplier(String articleUuid, String articleSupplierUuid, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;

  /**
   * 将指定uuid的商品供应商设置为商品的默认供应商
   * <p>
   * 一个商品只有一个默认供应商
   * 
   * @param articleUuid
   *          商品uuid，not null
   * @param articleSupplierUuid
   *          供应商uuid，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   *           指定的供应商不存在
   */
  void setDefaultArticleSupplier(String articleUuid, String articleSupplierUuid,
      OperateContext operCtx) throws IllegalArgumentException, WMSException;

  /**
   * 将指定uuid的商品规格设置为商品的默认规格
   * <p>
   * 一个商品只有一个默认规格
   * 
   * @param articleUuid
   *          商品uuid，not null
   * @param qpcUuid
   *          规格uuid，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   *           指定的规格不存在
   */
  void setDefaultArticleQpc(String articleUuid, String qpcUuid, OperateContext operCtx)
      throws IllegalArgumentException, WMSException;
}
