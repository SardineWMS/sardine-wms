package com.fr.demo;

import com.fr.base.Utils;

import com.fr.data.JobValue;
import com.fr.data.TotalVerifyJob;
import com.fr.data.Verifier;
import com.fr.script.Calculator;

public class TotalVerifyJobDemo extends TotalVerifyJob{
	/*
	 * type : ����Ҫ�����˽�б������������ɸģ���ʾУ��״̬
	 * 0  ��ʾУ��ɹ���Ĭ��У��״̬λΪ0
	 * 1  ��ʾУ��ʧ��
	 */
	private int type = 0; 
	
	@Override
	protected void doTotalJob(Data data, Calculator calculator) 
	throws Exception { // @param data �Զ�ά�����е������ύ����
		int sale, min;
		JobValue salenum, minnum; 

		int row = data.getRowCount(); // ��ȡһ������������
		for (int i = 0; i < row; i++) {  // ����ÿ�У�����У��
			salenum = (JobValue) data.getValueAt(i, 0);
            sale = Integer.parseInt(Utils.objectToString(salenum.getValue())); 
            
            minnum = (JobValue) data.getValueAt(i, 1);
            min = Integer.parseInt(Utils.objectToString(minnum.getValue()));
            
            if(sale < min){ //У���ж�
				type = 1;
            }
        }  
		
	}

	public String getMessage() {
		// ����У��״̬�ǳɹ�����ʧ�ܣ����ö�Ӧ�ķ�����Ϣ
		if(type == 0){
			return "��ϲ�㣬У��ɹ�";
		}else{
			return "����ֵ����С����С����";
		}
	}

	public Verifier.Status getType() {
		// ����У��״̬
		return Verifier.Status.parse(type);
	}

	public String getJobType() {
		return "totalVerifyJob";
	}
}
