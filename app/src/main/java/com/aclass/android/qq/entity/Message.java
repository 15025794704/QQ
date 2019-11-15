package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Message extends Entity implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//编号唯一
	private String sendQQ;//发送者qq
	private String receiveNum;//接受者号码（qq号，群号）
	private String context;//内容
	private Date time;//时间
	private String sendTime;//发送时间，用字符串比date方便存储
	private int state;//状态（已接收到>0 ,未接收到=0）

	public Message(){}

	protected Message(Parcel in) {
		id = in.readInt();
		sendQQ = in.readString();
		receiveNum = in.readString();
		context = in.readString();
		state = in.readInt();
	}

	public static final Creator<Message> CREATOR = new Creator<Message>() {
		@Override
		public Message createFromParcel(Parcel in) {
			return new Message(in);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(sendQQ);
		dest.writeString(receiveNum);
		dest.writeString(context);
		dest.writeInt(state);
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
}
