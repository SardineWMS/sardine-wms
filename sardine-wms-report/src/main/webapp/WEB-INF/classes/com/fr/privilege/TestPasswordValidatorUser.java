package com.fr.privilege;

import com.fr.privilege.providers.dao.AbstractPasswordEncode;

public class TestPasswordValidatorUser extends AbstractPasswordEncode {

    /**
     * ������������������㷨���������ݿ�����=FR+�û���+����+RF������true
     * @param localPassword �洢�����ݿ��е�����
     * @param clientPassword �û����������
     * @param clientUsername �û���
     * @return �Ƿ���֤�ɹ�
     */
	@Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
    
    public String encodePassword(String clientPassword, String clientUsername) {
       return "FR" + clientUsername + clientPassword + "RF";
     
    }

    /**
     * ��֤����ʱ�Ƿ�Ҫ�����û���
     */
    public boolean shouldIgnoreUsername() {
        return false;
    }

	@Override
	public String encodePassword(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 2��������������֤������ֱ��return false
     */
    }