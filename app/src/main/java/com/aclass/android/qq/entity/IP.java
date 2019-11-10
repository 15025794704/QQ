package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class IP extends Entity implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String QQ;
	private String ip;
	private int port;

	public IP(){}

	protected IP(Parcel in) {
		QQ = in.readString();
		ip = in.readString();
		port = in.readInt();
	}

	public static final Creator<IP> CREATOR = new Creator<IP>() {
		@Override
		public IP createFromParcel(Parcel in) {
			return new IP(in);
		}

		@Override
		public IP[] newArray(int size) {
			return new IP[size];
		}
	};

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "IP [QQ=" + QQ + ", ip=" + ip + ", port=" + port + "]";
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(QQ);
		dest.writeString(ip);
		dest.writeInt(port);
	}
}
