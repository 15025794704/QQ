package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Member extends Entity implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qunID;//群id
	private String memberQQ;//成员qq
	private int isTop;//置顶
	private int isHide;//屏蔽
	private int isDisturb;//免打扰
	private String niCheng;//成员昵称

	public Member(){}

	protected Member(Parcel in) {
		qunID = in.readString();
		memberQQ = in.readString();
		isTop = in.readInt();
		isHide = in.readInt();
		isDisturb = in.readInt();
		niCheng = in.readString();
	}

	public static final Creator<Member> CREATOR = new Creator<Member>() {
		@Override
		public Member createFromParcel(Parcel in) {
			return new Member(in);
		}

		@Override
		public Member[] newArray(int size) {
			return new Member[size];
		}
	};

	public String getQunID() {
		return qunID;
	}
	public void setQunID(String qunID) {
		this.qunID = qunID;
	}

	public String getNiCheng() {
		return niCheng;
	}
	public void setNiCheng(String niCheng) {
		this.niCheng = niCheng;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public int getIsHide() {
		return isHide;
	}
	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}
	public int getIsDisturb() {
		return isDisturb;
	}
	public void setIsDisturb(int isDisturb) {
		this.isDisturb = isDisturb;
	}
	@Override
	public String toString() {
		return "Member [qunID=" + qunID + ", memberQQ=" + memberQQ + ", isTop=" + isTop + ", isHide=" + isHide
				+ ", isDisturb=" + isDisturb + ", niCheng=" + niCheng + "]";
	}
	public String getMemberQQ() {
		return memberQQ;
	}
	public void setMemberQQ(String memberQQ) {
		this.memberQQ = memberQQ;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(qunID);
		dest.writeString(memberQQ);
		dest.writeInt(isTop);
		dest.writeInt(isHide);
		dest.writeInt(isDisturb);
		dest.writeString(niCheng);
	}
}
