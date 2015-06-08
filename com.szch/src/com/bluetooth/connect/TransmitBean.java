package com.bluetooth.connect;

import java.io.Serializable;

/**
 * ���ڴ���������
 * @author GuoDong
 *
 */
public class TransmitBean implements Serializable{

	private String msg = "";
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
}
