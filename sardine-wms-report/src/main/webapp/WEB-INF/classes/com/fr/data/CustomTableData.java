package com.fr.data;


import com.fr.data.AbstractTableData;
import com.fr.general.data.TableDataException;

public class CustomTableData extends AbstractTableData {
    public CustomTableData() {
        
    }

    /**
     * ��ȡ���ݼ�������
     * @return ���ݼ�����
     * @throws TableDataException
     */
    public int getColumnCount() throws TableDataException {
        return 0;
    }

    /**
     * ��ȡ���ݼ�ָ���е�����
     * @param columnIndex ָ���е�����
     * @return ָ���е�����
     * @throws TableDataException
     */
    public String getColumnName(int columnIndex) throws TableDataException {
        return null;
    }

    /**
     * ��ȡ���ݼ�������
     * @return ���ݼ���������
     * @throws TableDataException
     */
    public int getRowCount() throws TableDataException {
        return 0;
    }

    /**
     * ��ȡ���ݼ�ָ��λ���ϵ�ֵ
     * @param rowIndex ָ����������
     * @param columnIndex  ָ����������
     * @return  ָ��λ�õ�ֵ
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
