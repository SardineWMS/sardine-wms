package com.fr.io;    

import java.io.File;    
import java.util.HashMap; 
import com.fr.base.FRContext;      
import com.fr.base.Parameter;    
import com.fr.dav.LocalEnv;
import com.fr.main.TemplateWorkBook; 
import com.fr.print.PrintUtils;  
   
  
public class JavaPrint {    
     public static void main(String[] args) {    
         // ���屨�����л���,����ִ�б���    
         String envPath = "D:\\FineReport\\develop\\code\\build\\package\\WebReport\\WEB-INF";    
         FRContext.setCurrentEnv(new LocalEnv(envPath));    
         try {    
             TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), "GettingStarted.cpt");    
             // ������ֵ    
             Parameter[] parameters = workbook.getParameters();    
             HashMap<String, String> paraMap = new HashMap<String, String>();  
             paraMap.put(parameters[0].getName(), "����");  
               
             // java�е��ñ����ӡ����    
             boolean a = PrintUtils.printWorkBook("GettingStarted.cpt", paraMap, true);    
             if (a == false) {    
                 System.out.println("ʧ����������" + a);    
             } else {    
                 System.out.println("�ɹ�������" + a);    
             }    
         } catch (Exception e) {    
             e.printStackTrace();    
         }    
     }    
 }