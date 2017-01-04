package com.hd123.sardine.wms.common.utils;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2011，所有权利保留。
 * 
 * 项目名：	e21-h5-core
 * 文件名：	OracleSequenceGenerator.java
 * 模块说明：	
 * 修改历史：
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 序列化生成器实现 | ORACLE版本
 * 
 */
public class OracleSequenceGenerator extends SqlSessionDaoSupport implements ISequenceGenerator {

    public OracleSequenceGenerator() {
        // do nothing
    }

    /** 序列的前缀 */
    public static final String PREFIX_SEQ = "WMS";
    /** 序列名需要满足的规则 */
    private static final String PATTERN_DBID = "^[\\w]+$";
    /** 序列名的最大长度 */
    private static final int MAX_DBID_LENGTH = 40;

    public long nextValue(SequenceSpec seq) {
        if (!Pattern.matches(PATTERN_DBID, seq.getDbId()))
            throw new IllegalArgumentException("数据库序列标识只能由数字或字母组成。");

        Assert.assertStringNotTooLong(seq.getDbId(), MAX_DBID_LENGTH, "数据库序列标识");
        try {
            return nextValueNoCheck(seq);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public long currentValue(SequenceSpec seq) {
        if (!Pattern.matches(PATTERN_DBID, seq.getDbId()))
            throw new IllegalArgumentException("数据库序列标识只能由数字或字母组成。");
        Assert.assertStringNotTooLong(seq.getDbId(), MAX_DBID_LENGTH, "数据库序列标识");

        if (seq.getStartValue() <= 0)
            throw new IllegalArgumentException("数据库序列的起始值必须大于0。");
        try {
            return fetchCurrent(seq);
        } catch (Exception e) {
            return 0;
        }
    }

    public long nextValueNoCheck(SequenceSpec seq) throws Exception {
        try {
            return fetchNext(seq);
        } catch (Exception e) {
            tryCreateSeq(seq);
            return fetchNext(seq);
        }
    }

    private long fetchCurrent(SequenceSpec seq) throws Exception {
        String sql = "select last_number from USER_SEQUENCES where sequence_name='"
                + getSeqName(seq) + "'";
        return fetch(seq, sql);
    }

    private long fetchNext(SequenceSpec seq) throws Exception {
        String sql = "select " + getSeqName(seq) + ".nextVal from dual";
        return fetch(seq, sql);
    }

    private long fetch(SequenceSpec seq, String sql) throws SQLException {
        Statement stat = null;
        ResultSet rs = null;

        try {
            stat = getConnnection().createStatement();
            rs = stat.executeQuery(sql);
            Number num = null;
            while (rs.next()) {
                num = rs.getInt(1);
            }
            if (num == null)
                throw new Exception("取下一流水号失败。");
            return num.longValue();
        } catch (Exception e) {
            if (reachMaxValue(e))
                throw new IllegalArgumentException("达到最大流水号，无法再继续分配。");
            throw new IllegalArgumentException(e);
        } finally {
            if (rs != null)
                rs.close();
            if (stat != null)
                stat.close();
        }
    }

    private boolean reachMaxValue(Exception e) {
        if (e.getMessage() != null && e.getMessage().contains("ORA-08004:"))
            return true;
        return false;
    }

    private void tryCreateSeq(SequenceSpec seq) throws Exception {
        PreparedStatement stat = null;
        try {
            String sql = "create sequence " + getSeqName(seq) + " increment by "
                    + seq.getIncrement() + " start with " + seq.getStartValue()
                    + " minValue 0 maxValue " + seq.getMaxValue() + " nocycle nocache";
            stat = getConnnection().prepareStatement(sql);
            stat.execute();
        } catch (Exception e) {
            System.out.println("创建序列失败。");
            e.printStackTrace();
        } finally {
            if (stat != null)
                stat.close();
        }
    }

    private String getSeqName(SequenceSpec seq) {
        return PREFIX_SEQ + seq.getDbId().toUpperCase();
    }

    private Connection getConnnection() {
        return getSqlSession().getConnection();
    }
}
