package com.fr.function;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.fr.data.core.db.BinaryObject;
import com.fr.script.AbstractFunction;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class BinaryImage extends AbstractFunction{

	//����dll��"E:\\bmp\\WltRS"��dll���ļ�����·������������׺��������WltRS.class
	static WltRS wltrs = (WltRS) Native.loadLibrary("E:\\bmp\\WltRS", WltRS.class);
	
	static int index = 0;
	
	public Object run(Object[] args) {
		
		int current = index;
		
		//args[0] �� BinaryObject����ȡΪbo
		BinaryObject bo = (BinaryObject)args[0];
		
		//��boת��Ϊ.wlt�ļ�����������λ��E:\bmp\�����ط���GetBmp�ĵ�һ��������wlt�ļ���·��
		getFile(bo.getBytes(), "E:\\bmp\\", current + ".wlt");
		
		//��ȡ.wltΪ�ļ�
		File file = new File("E:\\bmp\\" + current + ".wlt");  
		
		//���ñ��ط���������ͬ·��������.bmp
		wltrs.GetBmp("E:\\bmp\\" + current + ".wlt", 1);
		
		//��ȡ������ͼƬ
		File imagefile = new File("E:\\bmp\\" + current + ".bmp");
		BufferedImage buffer = null;
		try {
			buffer = ImageIO.read(imagefile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		index = (++index)%300;
		return buffer;
	}
	
	
	// byte[]ת��Ϊfile�ķ���
	public static void getFile(byte[] bfile, String filePath, String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists() && dir.isDirectory()){//�ж��ļ�Ŀ¼�Ƿ����  
                dir.mkdirs();  
            }  
            file = new File(filePath+"\\"+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }
}

//��jna���ñ��ط����ı��벽�裬���庬�岻��
interface WltRS extends Library{
	//����Ҫ���õı��ط���
	void GetBmp(String str, int i);
}