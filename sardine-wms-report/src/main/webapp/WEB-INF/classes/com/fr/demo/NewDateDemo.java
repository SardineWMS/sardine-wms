//��̬�޸�����
package com.fr.demo;    
     
import java.util.Map;

import com.fr.base.Env;  
import com.fr.base.FRContext;     
import com.fr.base.ModuleContext;
import com.fr.data.ArrayTableDataDemo;    
import com.fr.io.TemplateWorkBookIO;  
import com.fr.main.TemplateWorkBook;  
import com.fr.report.module.EngineModule;
import com.fr.web.core.Reportlet;  
import com.fr.web.request.ReportletRequest;
import com.fr.page.stable.ReportPageAttr;
  
public class NewDateDemo extends Reportlet {    
    public TemplateWorkBook createReport(ReportletRequest reportletrequest) {    
        TemplateWorkBook workbook = null;    
        Env oldEnv = FRContext.getCurrentEnv(); 
        ModuleContext.startModule(EngineModule.class.getName());   
        try {    
            // ����workbook���󣬽�ģ�屣��Ϊworkbook���󲢷���    
            workbook = TemplateWorkBookIO.readTemplateWorkBook(oldEnv,"1.cpt");  
            ArrayTableDataDemo a = new ArrayTableDataDemo(); // ���ö���ĳ������ݼ�����    
            workbook.putTableData("ds2", a); // ��ģ�帳�µ����ݼ�    
        } catch (Exception e) {    
            e.getStackTrace();    
        }    
        return workbook;    
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