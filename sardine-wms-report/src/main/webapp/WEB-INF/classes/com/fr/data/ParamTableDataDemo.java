package com.fr.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.data.AbstractTableData;
import com.fr.base.Parameter;

public class ParamTableDataDemo extends AbstractTableData {
	// �������飬����������ݼ���������
	private String[] columnNames = null;
	// ����������ݼ���������
	private int columnNum = 10;
	// �����ѯ���ʵ��������
	private int colNum = 0;
	// �����ѯ�õ���ֵ
	private ArrayList valueList = null;

	// ���캯���������ṹ���ñ���10�������У�����Ϊcolumn#0��column#1��������������column#9
	public ParamTableDataDemo() {
		// ����tableName����
		setDefaultParameters(new Parameter[] { new Parameter("tableName") });
		// ����������ݼ�����
		columnNames = new String[columnNum];
		for (int i = 0; i < columnNum; i++) {
			columnNames[i] = "column#" + String.valueOf(i);
		}
	}

	// ʵ�������ĸ�����
	public int getColumnCount() {
		return columnNum;
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getRowCount() {
		init();
		return valueList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		init();
		if (columnIndex >= colNum) {
			return null;
		}
		return ((Object[]) valueList.get(rowIndex))[columnIndex];
	}

	// ׼������
	public void init() {
		// ȷ��ֻ��ִ��һ��
		if (valueList != null) {
			return;
		}
		// ����õ������ݿ����
		String tableName = parameters[0].getValue().toString();
		// ����SQL���,����ӡ����
		String sql = "select * from " + tableName + ";";
		FRContext.getLogger().info("Query SQL of ParamTableDataDemo: \n" + sql);
		// ����õ��Ľ����
		valueList = new ArrayList();
		// ���濪ʼ�������ݿ����ӣ����ողŵ�SQL�����в�ѯ
		Connection conn = this.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			// ��ü�¼����ϸ��Ϣ��Ȼ����������
			ResultSetMetaData rsmd = rs.getMetaData();
			colNum = rsmd.getColumnCount();
			// �ö��󱣴�����
			Object[] objArray = null;
			while (rs.next()) {
				objArray = new Object[colNum];
				for (int i = 0; i < colNum; i++) {
					objArray[i] = rs.getObject(i + 1);
				}
				// ��valueList�м�����һ������
				valueList.add(objArray);
			}
			// �ͷ����ݿ���Դ
			rs.close();
			stmt.close();
			conn.close();
			// ��ӡһ��ȡ��������������
			FRContext.getLogger().info(
					"Query SQL of ParamTableDataDemo: \n" + valueList.size()
							+ " rows selected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ȡ���ݿ����� driverName�� url ���Ի�������Ҫ��
	public Connection getConnection() {
		
		String driverName = "org.sqlite.JDBC";
		String url = "jdbc:sqlite://D:\\FineReport_8.0\\WebReport\\FRDemo.db";
		String username = "";
		String password = "";
		Connection con = null;
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return con;
	}

	// �ͷ�һЩ��Դ����Ϊ���ܻ����ظ����ã��������ͷ�valueList�����ϴβ�ѯ�Ľ���ͷŵ�
	public void release() throws Exception {
		super.release();
		this.valueList = null;
	}
}