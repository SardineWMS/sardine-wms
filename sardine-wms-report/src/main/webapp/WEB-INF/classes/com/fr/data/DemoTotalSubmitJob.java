package com.fr.data;

import com.fr.data.JobValue;
import com.fr.data.TotalSubmitJob;
import com.fr.script.Calculator;

public class DemoTotalSubmitJob extends TotalSubmitJob {

	/**
	 * ͬһ�ύ�¼�����һ���ύ������ִֻ��һ��
	 * @param data �Զ�ά�����е������ύ����
	 * 
	 */
	protected void doTotalJob(Data data, Calculator calculator)
			throws Exception {
		data.getColumnCount(); // ��ȡ�е�������ÿһ�ж�Ӧһ����ӵ�����
		for (int i = 0; i < data.getColumnCount(); i++) {
			System.out.println(data.getColumnName(i));   // ��ȡ��Ӧ����������
		}
		
		for (int i = 0; i < data.getRowCount(); i++) {   // getRowCount ��ȡһ������������
			System.out.print("ROW " + i + " {");
			for (int j = 0; j < data.getColumnCount(); j++) {
				if (j > 0) System.out.print(", ");
				Object value = data.getValueAt(i, j);    // ��ȡ��Ӧλ�õ�ֵ
				if (value instanceof JobValue) {
					JobValue ce = (JobValue)value;
					// JobValue��getValueState()������ȡ�˶�Ӧ��Ԫ���״̬
					if (ce.getValueState() == JobValue.VALUE_STATE_CHANGED) {
						// �˵�Ԫ���ֵ�ڱ����ʼ�����޸Ĺ�
					} else if (ce.getValueState() == JobValue.VALUE_STATE_INSERT) {
						// �˵�Ԫ�����ڱ����ʼ����������(����ִ���˲����в���)
					} else if (ce.getValueState() == JobValue.VALUE_STATE_DELETED) {
						// �˵�Ԫ�����ڵļ�¼��ִ����ɾ������
					} else if (ce.getValueState() == JobValue.VALUE_STATE_DEFAULT) {
						// �˵�Ԫ���ڱ����ʼ����û�б仯
					}
					
					value = ce.getValue();               // ͨ��JobValue��getValue������õ�Ԫ���ֵ
				}
				
				System.out.print(data.getColumnName(j) + " : " + value);
			}
			System.out.print("}");
			System.out.println();
		}
	}
}
