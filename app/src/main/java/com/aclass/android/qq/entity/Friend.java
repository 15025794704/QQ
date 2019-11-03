package com.aclass.android.qq.entity;

import java.io.Serializable;

public class Friend extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String QQ1;//本qq
	private String QQ2;//好友的qq
	private String beiZhu;//备注
	private String QQgroup;//好友存在的分组
	private int isTop;//好友是否消息置顶
	private int isDisturb;//消息免打扰
	private int isHide;//是否屏蔽好友
	private int isAgree;//好友申请是否通过了
	
	public String getQQ1() {
		return QQ1;
	}
	public void setQQ1(String qQ1) {
		QQ1 = qQ1;
	}
	public String getQQ2() {
		return QQ2;
	}
	public void setQQ2(String qQ2) {
		QQ2 = qQ2;
	}
	public String getBeiZhu() {
		return beiZhu;
	}
	public void setBeiZhu(String beiZhu) {
		this.beiZhu = beiZhu;
	}
	public String getQQgroup() {
		return QQgroup;
	}
	public void setQQgroup(String qQgroup) {
		QQgroup = qQgroup;
	}
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public int getIsDisturb() {
		return isDisturb;
	}
	public void setIsDisturb(int isDisturb) {
		this.isDisturb = isDisturb;
	}
	public int getIsHide() {
		return isHide;
	}
	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}
	public int getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(int isAgree) {
		this.isAgree = isAgree;
	}
	@Override
	public String toString() {
		return "Friend [QQ1=" + QQ1 + ", QQ2=" + QQ2 + ", beiZhu=" + beiZhu + ", QQgroup=" + QQgroup + ", isTop="
				+ isTop + ", isDisturb=" + isDisturb + ", isHide=" + isHide + ", isAgree=" + isAgree + "]";
	}
	
}
