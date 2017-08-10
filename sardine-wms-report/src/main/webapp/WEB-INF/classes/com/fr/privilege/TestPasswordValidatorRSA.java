package com.fr.privilege;  

import com.fr.privilege.providers.dao.AbstractPasswordValidator;  
public class TestPasswordValidatorRSA extends AbstractPasswordValidator{  
    //@Override
    public String encodePassword( String clinetPassword) {
    	try {
    		//��������з�ת������ab��ת��Ϊba
    		StringBuffer sb = new StringBuffer();  
	        sb.append(new String(clinetPassword));
	        String bb = sb.reverse().toString();
    		//���м���
    		byte[] en_test = RSAUtil.encrypt(RSAUtil.getKeyPair().getPublic(),bb.getBytes());     		
    		//���н��ܣ�������ݿ����汣����Ǽ����룬��˴�����Ҫ���н���
    		byte[] de_test = RSAUtil.decrypt(RSAUtil.getKeyPair().getPrivate(),en_test);  
    		//���ؼ�������
    		clinetPassword=new String(de_test);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clinetPassword; //����ȡ���������������ݿ�����ƥ�䡣  
    }

	@Override
	public boolean validatePassword(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}


} 