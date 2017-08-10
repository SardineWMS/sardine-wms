// �������籨���л�ȡrequest�е�ֵ 
package com.fr.demo;  

import java.util.Map;
import java.util.logging.Level;

import com.fr.base.FRContext;    
import com.fr.general.ModuleContext;
import com.fr.main.TemplateWorkBook;    
import com.fr.report.module.EngineModule;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;
import com.fr.io.TemplateWorkBookIO;    
import com.fr.base.Parameter;    
import com.fr.dav.LocalEnv;
import com.fr.page.stable.ReportPageAttr;
    
    
public class URLParameterDemo extends Reportlet {    
    public TemplateWorkBook createReport(ReportletRequest reportletRequest) {   
          
         String envPath="D:\\FineReport_8.0\\WebReport\\WEB-INF";    
         FRContext.setCurrentEnv(new LocalEnv(envPath));    
         ModuleContext.startModule(EngineModule.class.getName());   
        // ��ȡ�ⲿ�����Ĳ���    
        TemplateWorkBook wbTpl = null;  
        String countryValue = reportletRequest.getParameter("����").toString(); 
        try {    
            wbTpl = TemplateWorkBookIO.readTemplateWorkBook(    
                    FRContext.getCurrentEnv(), "\\doc\\Primary\\Parameter\\Parameter.cpt");    
            // ��ȡ��������飬����ԭģ��ֻ��countryһ�����������ֱ��ȡindexΪ0�Ĳ����������ⲿ�����ֵ�����ò���    
            Parameter[] ps = wbTpl.getParameters();    
            ps[0].setValue(countryValue);    
            // ԭģ�嶨���в������棬�����Ѿ����ⲿ��ã�ȥ������ҳ��    
            // �����뱣���������棬��ģ������Ϊ���ӳٱ���չʾ���ٴ��������ֱ�Ӹ��ݲ���ֵ��ʾ�����������Ҫ�ٴε����ѯ��ť    
            wbTpl.getReportParameterAttr().setParameterUI(null);    
        } catch (Exception e) {    
            e.printStackTrace();    
            return null;    
        }    
        return wbTpl;   
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