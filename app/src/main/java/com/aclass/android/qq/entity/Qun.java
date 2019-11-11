package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Qun extends Entity implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qunID;//群id
	private String name;//群名称
	private String host;//群主

	public Qun(){}

	protected Qun(Parcel in) {
		qunID = in.readString();
		name = in.readString();
		host = in.readString();
	}

	public static final Creator<Qun> CREATOR = new Creator<Qun>() {
		@Override
		public Qun createFromParcel(Parcel in) {
			return new Qun(in);
		}

		@Override
		public Qun[] newArray(int size) {
			return new Qun[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(qunID);
		dest.writeString(name);
		dest.writeString(host);
	}
}
