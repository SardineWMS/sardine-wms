package com.fr.data;

import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import com.fr.data.AbstractTableData;
import com.fr.general.data.TableDataException;

public class WebServiceTableData extends AbstractTableData{
	private String[][] data;
	
	public WebServiceTableData() {
		this.data = this.getData();
	}

	//��ȡ����
	public int getColumnCount() throws TableDataException {
		return data[0].length;
	}

	//��ȡ�е�����Ϊ�����е�һ�е�ֵ
	public String getColumnName(int columnIndex) throws TableDataException {
		return data[0][columnIndex];
	}

	//��ȡ����Ϊ���ݵĳ���-1
	public int getRowCount() throws TableDataException {
		return data.length - 1;
	}

	//��ȡֵ
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex + 1][columnIndex];
	}

	public String[][] getData() {
		try {
			String endpoint = "http://localhost:8080/axis/TestWS2TDClient.jws";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://localhost:8080/axis/TestWS2TDClient.jws",
					"getTD"));
			String[][] ret = (String[][])call.invoke(new Object[] {});
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[][] {};
	}
}