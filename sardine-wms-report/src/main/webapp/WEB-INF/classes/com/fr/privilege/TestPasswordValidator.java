package com.fr.privilege;

import com.fr.privilege.providers.dao.AbstractPasswordEncode;

public class TestPasswordValidator extends AbstractPasswordEncode{
	
	@Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
    
	public String encodePassword(String clientPassword) {
		return (clientPassword+"FR");//����ȡ�û����������Ȼ���ں������FR���������ݿ�����ƥ�䡣
	}

}