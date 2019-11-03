package com.aclass.android.qq.entity;

import java.io.Serializable;
import java.util.Date;

public class Message extends Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号唯一
	private String sendQQ;//发送者qq
	private String receiveNum;//接受者号码（qq号，群号）
	private String context;//内容
	private Date time;//时间
	private int state;//状态（已接收到>0 ,未接收到=0）
	public String getSendQQ() {
		return sendQQ;
	}
	public void setSendQQ(String sendQQ) {
		this.sendQQ = sendQQ;
	}
	public String getReceiveNum() {
		return receiveNum;
	}
	public void setReceiveNum(String receiveNum) {
		this.receiveNum = receiveNum;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", sendQQ=" + sendQQ + ", receiveNum=" + receiveNum + ", context=" + context
				+ ", time=" + time + ", state=" + state + "]";
	}
	
}
