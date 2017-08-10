package com.fr.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fr.data.DefinedSubmitJob;
import com.fr.data.JobValue;
import com.fr.script.Calculator;

public class DemoSubmitJob2 extends DefinedSubmitJob {

	/**
	 * ÿһ����¼ִ��һ�δ˷���
	 * ͬһ�ύ�¼���һ�����������ڣ��˶�����Ψһ��
	 */
	public void doJob(Calculator calculator) throws Exception {
		// ͬ������ֱ���ڴ����calculator�л�ȡ��������Լ����Ӧ��ֵ
		Map map = (Map)calculator.getAttribute(PROPERTY_VALUE);
		if (map == null) return;
		
		Set set = map.entrySet();
		Iterator it = set.iterator();
		Entry entry;
		// ����Map��ȡ�������Լ���ֵ
		while (it.hasNext()) {
			entry = (Entry)it.next();
			System.out.print(" " + entry.getKey() + ": ");
			
			// JobValue��Ӧ��Ԫ��
			if (entry.getValue() instanceof JobValue) {
				JobValue ce = (JobValue)entry.getValue();
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
				
				System.out.print(ce.getValue());         // ͨ��JobValue��getValue������õ�Ԫ���ֵ
			} else {
				// �ǵ�Ԫ�����Ӧ��������ֵ
				System.out.print(entry.getValue().toString());
			}
		}
		System.out.println();
	}
}
