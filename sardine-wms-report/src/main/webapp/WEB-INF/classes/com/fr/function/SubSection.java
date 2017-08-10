//SubSection����-Oracle��ѯ������������
package com.fr.function;  
  
import com.fr.general.FArray;
import com.fr.script.AbstractFunction;   
  
public class SubSection extends AbstractFunction {  
    public Object run(Object[] args) {  
        // ��ȡ��һ�����󣬼�ȡ�ô���Ĳ���  
        Object para = args[0];  
        String parastr = para.toString();  
        // �����Ǹ�ѡ���������Ҫȥ��ǰ���"("��")"  
        if (parastr.startsWith("(") && parastr.endsWith(")")) {  
            parastr = parastr.substring(1, parastr.length() - 1);  
        }  
        // ���ַ���תΪ","�ָ������  
        String test[] = parastr.split(",");  
        int len = test.length;  
        int loopnum = len / 500;  
        if (len % 500 != 0) {  
            loopnum += 1;  
        }  
        ;  
        // ���ص�ֵ�����飬��Ҫ����������ڲ�������FArray  
        FArray result = new FArray();  
        String str = "";  
        int k = 1;  
        for (int i = 0; i < loopnum; i++) {  
            for (int j = 500 * i; j < 500 * (i + 1) && j < len; j++) {  
                if (k != 500 && j != (len - 1)) {  
                    str += test[j] + ",";  
                } else {  
                    str += test[j];  
                }  
                k++;  
            }  
            // ÿ500���γ�һ�鲢��ÿ���ⲿ����"("��")"  
            str = "(" + str + ")";  
            result.add(str);  
            str = "";  
            k = 1;  
        }  
        return result;  
    }  
}