package com.fr.data;

import com.fr.data.DefinedSubmitJob;
import com.fr.data.JobValue;
import com.fr.script.Calculator;

public class DemoSubmitJob1 extends DefinedSubmitJob {
	/**
	 * ��ģ���Զ����¼����ӵ����� ��������������ж�Ӧʱ������Զ���ֵ�ڴ˶�Ӧ����
	 */
	private JobValue studentno;   // JobValue��Ӧ��Ԫ��
	private JobValue name;
	private JobValue grade;
	private boolean isPass;       // �ǵ�Ԫ�����Ӧ��������ֵ

	/**
	 * ÿһ����¼ִ��һ�δ˷���
	 * ͬһ�ύ�¼���һ�����������ڣ��˶�����Ψһ��
	 */
	public void doJob(Calculator calculator) throws Exception {
		// JobValue��getValueState()������ȡ�˶�Ӧ��Ԫ���״̬
		if (studentno.getValueState() == JobValue.VALUE_STATE_CHANGED) {
			// �˵�Ԫ���ֵ�ڱ����ʼ�����޸Ĺ�
		} else if (studentno.getValueState() == JobValue.VALUE_STATE_INSERT) {
			// �˵�Ԫ�����ڱ����ʼ����������(����ִ���˲����в���)
		} else if (studentno.getValueState() == JobValue.VALUE_STATE_DELETED) {
			// �˵�Ԫ�����ڵļ�¼��ִ����ɾ������
		} else if (studentno.getValueState() == JobValue.VALUE_STATE_DEFAULT) {
			// �˵�Ԫ���ڱ����ʼ����û�б仯
		}
		
		// ֵ��ȡ
		System.out.print(" ѧ��: " + studentno.getValue());  // ͨ��JobValue��getValue������õ�Ԫ���ֵ
		System.out.print(" ����: " + name.getValue());
		System.out.print(" �ܷ�: " + grade.getValue());
		System.out.print(" �Ƿ���: " + isPass);
		System.out.println();
	}
}
