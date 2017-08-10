//�������籨��
package com.fr.demo;  
  
import java.util.Map;

import com.fr.base.Env;
import com.fr.base.FRContext; 
import com.fr.main.TemplateWorkBook;  
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.io.TemplateWorkBookIO;  
 
  
public class SimpleReportletDemo extends Reportlet {  
    public TemplateWorkBook createReport(ReportletRequest reportletrequest) {  
        // �½�һ��WorkBook�������ڱ������շ��صı���  
        Env oldEnv = FRContext.getCurrentEnv();  
        TemplateWorkBook WorkBook = null;  
        try {  
            // ��ȡģ�壬��ģ�屣��Ϊworkbook���󲢷���  
            WorkBook = TemplateWorkBookIO.readTemplateWorkBook(oldEnv,  
                    "\\doc\\Primary\\Parameter\\Parameter.cpt");  
        } catch (Exception e) {  
            e.getStackTrace();  
        }  
        return WorkBook;  
    }

	@Override
	public void setParameterMap(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTplPath(String arg0) {
		// TODO Auto-generated method stub
		
	}  
}