package com.hd123.sardine.wms.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hd123.rumba.commons.lang.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis 工具类
 * 
 */
public class RedisUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

  // Redis服务器IP
  private static String address = "172.17.1.54";

  // Redis的端口号
  private static int port = 6379;

  // 访问密码
  private static String auth = "sardine";

  private JedisPool jedisPool = null;

  /**
   * redis过期时间,以秒为单位
   */
  public final int EXRP_HOUR = 60 * 60; // 一小时
  public final int EXRP_DAY = 60 * 60 * 24; // 一天
  public final int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

  /**
   * 同步获取Jedis实例
   * 
   * @return Jedis
   */
  public static synchronized Jedis getJedis() {
    Jedis jedis = null;
    try {
      jedis = new Jedis(address, port, 60000);
      jedis.auth(auth);
      jedis.connect();
    } catch (Exception e) {
      LOGGER.error("获取缓存服务器客户端失败！ " + e.getMessage());
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
    Jedis jedis = null;
    try {
      jedis = getJedis();
      value = StringUtil.isNullOrBlank(value) ? "" : value;
      jedis.set(key, value);
    } catch (Exception e) {
      LOGGER.error("缓存信息存储失败！ " + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.disconnect();
        jedis = null;
      }
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
    Jedis jedis = null;
    try {
      jedis = getJedis();
      value = StringUtil.isNullOrBlank(value) ? "" : value;
      jedis.setex(key, seconds, value);
    } catch (Exception e) {
      LOGGER.error("缓存信息存储失败！ " + e.getMessage());
    } finally {
      if (jedis != null) {
        jedis.disconnect();
        jedis = null;
      }
    }
  }

  /**
   * 获取String值
   * 
   * @param key
   * @return value
   */
  public static String getString(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      if (jedis == null || !jedis.exists(key)) {
        return null;
      }
      return jedis.get(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (jedis != null) {
        jedis.disconnect();
        jedis = null;
      }
    }
  }

  public String deleteString(String key) {
    Jedis jedis = null;
    try {
      jedis = getJedis();
      if (jedis == null || !jedis.exists(key)) {
        return null;
      }
      String value = jedis.get(key);
      jedis.del(key);
      return value;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (jedis != null) {
        jedis.disconnect();
        jedis = null;
      }
    }
  }
}