package com.hd123.sardine.wms.web.base.jedis;

import org.apache.log4j.Logger;

import com.hd123.rumba.commons.lang.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 工具类
 * 
 * @author caspar http://blog.csdn.net/tuposky
 */
public class RedisUtil {

  protected Logger logger = Logger.getLogger(RedisUtil.class);

  // Redis服务器IP
  private String address = "";

  // Redis的端口号
  private int port = 0;

  // 访问密码
  private String auth = "sardine";

  // 可用连接实例的最大数目，默认值为8；
  // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
  private int maxActive = 100;

  // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
  private int maxIdle = 50;

  // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
  private int maxWait = 10;

  // 超时时间
  private int timeout = 60000;

  public void setPort(int port) {
    this.port = port;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAuth(String auth) {
    this.auth = auth;
  }

  public void setMaxActive(int maxActive) {
    this.maxActive = maxActive;
  }

  public void setMaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
  }

  public void setMaxWait(int maxWait) {
    this.maxWait = maxWait;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
  private boolean TEST_ON_BORROW = true;

  private JedisPool jedisPool = null;

  /**
   * redis过期时间,以秒为单位
   */
  public final int EXRP_HOUR = 60 * 60; // 一小时
  public final int EXRP_DAY = 60 * 60 * 24; // 一天
  public final int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

  /**
   * 初始化Redis连接池
   */
  private void initialPool() {
    try {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxActive(maxActive);
      config.setMaxIdle(maxIdle);
      config.setMaxWait(maxWait);
      config.setTestOnBorrow(TEST_ON_BORROW);
      jedisPool = new JedisPool(config, address, port, timeout, auth);
    } catch (Exception e) {
      logger.error("Jedis连接池创建失败！" + e);
    }
  }

  /**
   * 在多线程环境同步初始化
   */
  private synchronized void poolInit() {
    if (jedisPool == null) {
      initialPool();
    }
  }

  /**
   * 同步获取Jedis实例
   * 
   * @return Jedis
   */
  public synchronized Jedis getJedis() {
    if (jedisPool == null) {
      poolInit();
    }
    Jedis jedis = null;
    try {
      if (jedisPool != null) {
        jedis = jedisPool.getResource();
      }
    } catch (Exception e) {
      logger.error("Get jedis error : " + e);
    } finally {
      returnResource(jedis);
    }
    return jedis;
  }

  /**
   * 释放jedis资源
   * 
   * @param jedis
   */
  public void returnResource(final Jedis jedis) {
    if (jedis != null && jedisPool != null) {
      jedisPool.returnResource(jedis);
    }
  }

  /**
   * 设置 String
   * 
   * @param key
   * @param value
   */
  public void setString(String key, String value) {
    try {
      value = StringUtil.isNullOrBlank(value) ? "" : value;
      getJedis().set(key, value);
    } catch (Exception e) {
      logger.error("Set key error : " + e);
    }
  }

  /**
   * 设置 过期时间
   * 
   * @param key
   * @param seconds
   *          以秒为单位
   * @param value
   */
  public void setString(String key, int seconds, String value) {
    try {
      value = StringUtil.isNullOrBlank(value) ? "" : value;
      getJedis().setex(key, seconds, value);
    } catch (Exception e) {
      logger.error("Set keyex error : " + e);
    }
  }

  /**
   * 获取String值
   * 
   * @param key
   * @return value
   */
  public String getString(String key) {
    Jedis jedis = getJedis();
    if (jedis == null || !jedis.exists(key)) {
      return null;
    }
    return jedis.get(key);
  }

  public String deleteString(String key) {
    Jedis jedis = getJedis();
    if (jedis == null || !jedis.exists(key)) {
      return null;
    }
    String value = jedis.get(key);
    jedis.del(key);
    return value;
  }
}