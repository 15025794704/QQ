package com.aclass.android.qq.entity;

import java.io.Serializable;

public class Qun extends Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qunID;//群id
	private String name;//群名称
	private String host;//群主
	public String getQunID() {
		return qunID;
	}
	public void setQunID(String qunID) {
		this.qunID = qunID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Override
	public String toString() {
		return "Qun [qunID=" + qunID + ", name=" + name + ", host=" + host + "]";
	}
	
}
