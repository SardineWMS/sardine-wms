//package com.hd123.sardine.wms.web.base.jedis;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.exceptions.JedisException;
//
//public abstract class JCacheTools {
//  public abstract int getDBIndex();
//
//  @Resource
//  protected JedisPool jedisPool;
//
//  protected Jedis getJedis() throws JedisException {
//    Jedis jedis = null;
//    try {
//      jedis = jedisPool.getResource();
//    } catch (JedisException e) {
//      if (jedis != null) {
//        jedisPool.returnBrokenResource(jedis);
//      }
//      throw e;
//    }
//    return jedis;
//  }
//
//  protected void release(Jedis jedis, boolean isBroken) {
//    if (jedis != null) {
//      if (isBroken) {
//        jedisPool.returnBrokenResource(jedis);
//      } else {
//        jedisPool.returnResource(jedis);
//      }
//    }
//  }
//
//  protected String addStringToJedis(String key, String value, int cacheSeconds, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    String lastVal = null;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      lastVal = jedis.getSet(key, value);
//      if (cacheSeconds != 0) {
//        jedis.expire(key, cacheSeconds);
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return lastVal;
//  }
//
//  protected void addStringToJedis(Map<String, String> batchData, int cacheSeconds,
//      String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      Pipeline pipeline = jedis.pipelined();
//      for (Map.Entry<String, String> element : batchData.entrySet()) {
//        if (cacheSeconds != 0) {
//          pipeline.setex(element.getKey(), cacheSeconds, element.getValue());
//        } else {
//          pipeline.set(element.getKey(), element.getValue());
//        }
//      }
//      pipeline.sync();
//    } catch (Exception e) {
//      isBroken = true;
//      e.printStackTrace();
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void addListToJedis(String key, List<String> list, int cacheSeconds,
//      String methodName) {
//    if (list != null && list.size() > 0) {
//      Jedis jedis = null;
//      boolean isBroken = false;
//      try {
//        jedis = this.getJedis();
//        jedis.select(getDBIndex());
//        if (jedis.exists(key)) {
//          jedis.del(key);
//        }
//        for (String aList : list) {
//          jedis.rpush(key, aList);
//        }
//        if (cacheSeconds != 0) {
//          jedis.expire(key, cacheSeconds);
//        }
//      } catch (JedisException e) {
//        isBroken = true;
//      } catch (Exception e) {
//        isBroken = true;
//      } finally {
//        release(jedis, isBroken);
//      }
//    }
//  }
//
//  protected void addToSetJedis(String key, String[] value, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      jedis.sadd(key, value);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void removeSetJedis(String key, String value, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      jedis.srem(key, value);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void pushDataToListJedis(String key, String data, int cacheSeconds, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      jedis.rpush(key, data);
//      if (cacheSeconds != 0) {
//        jedis.expire(key, cacheSeconds);
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void pushDataToListJedis(String key, List<String> batchData, int cacheSeconds,
//      String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      jedis.del(key);
//      jedis.lpush(key, batchData.toArray(new String[batchData.size()]));
//      if (cacheSeconds != 0)
//        jedis.expire(key, cacheSeconds);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  /**
//   * 删除list中的元素
//   * 
//   * @param key
//   * @param values
//   * @param methodName
//   */
//  protected void deleteDataFromListJedis(String key, List<String> values, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      Pipeline pipeline = jedis.pipelined();
//      if (values != null && !values.isEmpty()) {
//        for (String val : values) {
//          pipeline.lrem(key, 0, val);
//        }
//      }
//      pipeline.sync();
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void addHashMapToJedis(String key, Map<String, String> map, int cacheSeconds,
//      boolean isModified, String methodName) {
//    boolean isBroken = false;
//    Jedis jedis = null;
//    if (map != null && map.size() > 0) {
//      try {
//        jedis = this.getJedis();
//        jedis.select(getDBIndex());
//        jedis.hmset(key, map);
//        if (cacheSeconds >= 0)
//          jedis.expire(key, cacheSeconds);
//      } catch (Exception e) {
//        isBroken = true;
//      } finally {
//        release(jedis, isBroken);
//      }
//    }
//  }
//
//  protected void addHashMapToJedis(String key, String field, String value, int cacheSeconds,
//      String methodName) {
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      if (jedis != null) {
//        jedis.select(getDBIndex());
//        jedis.hset(key, field, value);
//        jedis.expire(key, cacheSeconds);
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  protected void updateHashMapToJedis(String key, String incrementField, long incrementValue,
//      String dateField, String dateValue, String methodName) {
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      jedis.hincrBy(key, incrementField, incrementValue);
//      jedis.hset(key, dateField, dateValue);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  public String getStringFromJedis(String key, String methodName) {
//    String value = null;
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      if (jedis.exists(key)) {
//        value = jedis.get(key);
//        value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return value;
//  }
//
//  public List<String> getStringFromJedis(String[] keys, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      return jedis.mget(keys);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return null;
//  }
//
//  protected List<String> getListFromJedis(String key, String methodName) throws JMSCacheException {
//    List<String> list = null;
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      if (jedis.exists(key)) {
//        list = jedis.lrange(key, 0, -1);
//      }
//    } catch (JedisException e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return list;
//  }
//
//  protected Set<String> getSetFromJedis(String key, String methodName) throws JMSCacheException {
//    Set<String> list = null;
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      if (jedis.exists(key)) {
//        list = jedis.smembers(key);
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return list;
//  }
//
//  protected Map<String, String> getHashMapFromJedis(String key, String methodName) {
//    Map<String, String> hashMap = null;
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      hashMap = jedis.hgetAll(key);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return hashMap;
//  }
//
//  protected String getHashMapValueFromJedis(String key, String field, String methodName) {
//    String value = null;
//    boolean isBroken = false;
//    Jedis jedis = null;
//    try {
//      jedis = this.getJedis();
//      if (jedis != null) {
//        jedis.select(getDBIndex());
//        if (jedis.exists(key)) {
//          value = jedis.hget(key, field);
//        }
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return value;
//  }
//
//  public Long getIdentifyId(String identifyName, String methodName) {
//    boolean isBroken = false;
//    Jedis jedis = null;
//    Long identify = null;
//    try {
//      jedis = this.getJedis();
//      if (jedis != null) {
//        jedis.select(getDBIndex());
//        identify = jedis.incr(identifyName);
//        if (identify == 0) {
//          return jedis.incr(identifyName);
//        } else {
//          return identify;
//        }
//      }
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return null;
//  }
//
//  /**
//   * 删除某db的某个key值
//   * 
//   * @param key
//   * @return
//   */
//  public Long delKeyFromJedis(String key) {
//    boolean isBroken = false;
//    Jedis jedis = null;
//    long result = 0;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      return jedis.del(key);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return result;
//  }
//
//  /**
//   * 根据dbIndex flushDB每个shard
//   * 
//   * @param dbIndex
//   */
//  public void flushDBFromJedis(int dbIndex) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(dbIndex);
//      jedis.flushDB();
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//  }
//
//  public boolean existKey(String key, String methodName) {
//    Jedis jedis = null;
//    boolean isBroken = false;
//    try {
//      jedis = this.getJedis();
//      jedis.select(getDBIndex());
//      return jedis.exists(key);
//    } catch (Exception e) {
//      isBroken = true;
//    } finally {
//      release(jedis, isBroken);
//    }
//    return false;
//  }
//
//}