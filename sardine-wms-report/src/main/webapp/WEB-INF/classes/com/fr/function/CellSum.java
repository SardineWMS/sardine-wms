// �Զ��庯���л�ȡ��ʽ���ڵ�Ԫ�� 
package com.fr.function;  
  
import com.fr.base.Utils;
import com.fr.script.AbstractFunction;  

public class CellSum extends AbstractFunction {  
    public Object run(Object[] args) {  
        String sum = Utils.objectToNumber(new SUM().run(args), false)  
                .toString(); // ֱ�ӵ���FR�ڲ���SUM����  
        String result = "���ڵ�Ԫ��Ϊ��" + this.getCalculator().getCurrentColumnRow()  
                + "���ܺ�Ϊ��" + sum; // ��ȡ��ǰ��Ԫ��ƴ�����ս��  
        return result;  
    }  
}