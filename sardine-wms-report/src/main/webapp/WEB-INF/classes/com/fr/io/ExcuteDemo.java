package com.fr.io;  
  
import java.io.File;  
import java.io.FileOutputStream;  
import com.fr.base.FRContext;  
import com.fr.general.ModuleContext;  
import com.fr.dav.LocalEnv; 
import com.fr.io.exporter.ExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.report.module.EngineModule;
import com.fr.stable.WriteActor;
   
  
public class ExcuteDemo {  
    public static void main(String[] args) {  
        try {  
            // ������Ҫ����ִ�����ڵĻ���������������ȷ��ȡ���ݿ���Ϣ  
            String envPath = "D:\\FineReport_8.0\\WebReport\\WEB-INF";  
            FRContext.setCurrentEnv(new LocalEnv(envPath));  
            ModuleContext.startModule(EngineModule.class.getName());  
            // ��ȡģ��  
            TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(),"\\doc\\Primary\\Parameter\\Parameter.cpt");  
            /* 
             * ���ɲ���map��ע��������Ӧ��ֵ������ִ�б��� ��ģ����ֻ��һ���������������丳ֵ���� 
             * �������ڷ�������ʱ������������ͨ��req.getParameter(name)��� 
             * ��õĲ���put��map�У�paraMap.put(paraname,paravalue) 
             */  
            java.util.Map paraMap = new java.util.HashMap();  
            paraMap.put("����", "����");  
            // ʹ��paraMapִ�����ɽ��  
            ResultWorkBook result = workbook.execute(paraMap,new WriteActor());  
            // ʹ�ý���絼����excel  
            FileOutputStream outputStream = new FileOutputStream(new File(  
                    "D:\\Parameter.xls"));  
            ExcelExporter excelExporter = new ExcelExporter();  
            excelExporter.export(outputStream, result);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}