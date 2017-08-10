// �����������API 
package com.fr.io;  
  
import java.awt.Color;
import java.io.File;  
import java.io.FileOutputStream;  
import com.fr.base.FRContext;  
import com.fr.base.ModuleContext;  
import com.fr.base.background.ColorBackground;
import com.fr.dav.LocalEnv;
import com.fr.general.Background;
import com.fr.io.TemplateWorkBookIO;    
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.report.module.EngineModule;
  
public class SetParameterWindow {  
    public static void main(String[] args) {  
        try {  
            // ���屨�����л���,����ִ�б���  
            String envPath = "D:\\FineReport\\develop\\code\\build\\package\\WebReport\\WEB-INF";  
            FRContext.setCurrentEnv(new LocalEnv(envPath));  
            ModuleContext.startModule(EngineModule.class.getName());  
            // ��ȡģ�屣��ΪWorkBook����  
            WorkBook workbook = (WorkBook) TemplateWorkBookIO  
                    .readTemplateWorkBook(FRContext.getCurrentEnv(),  
                            "\\doc\\Primary\\Parameter\\Parameter.cpt");  
            // ��ȡWorkBook�������Ĳ�������ReportParameterAttr  
            ReportParameterAttr paraAttr = workbook.getReportParameterAttr();  
            /* ��������Ĳ���
            * 0 : ����
            * 1 ������
            * 2 �� ����
            */
            paraAttr.setAlign(1);
            /*
            * ���ò������汳��
            * ColorBackground ����ɫ����
            * GradientBackground ������ɫ����
            * ImageBackground ��ͼƬ����
            * PatternBackground ��ͼ������
            * TextureBackground ��������
            */
            Background background = ColorBackground.getInstance(new Color(0,255,255));
            paraAttr.setBackground(background);
            // �������ò�������,�������ս��  
            workbook.setReportParameterAttr(paraAttr);  
            FileOutputStream outputStream = new FileOutputStream(new File(  
                    "D:\\newParameter.cpt"));  
            EmbeddedTableDataExporter templateExporter = new EmbeddedTableDataExporter();  
            templateExporter.export(outputStream, workbook);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}