package com.aclass.android.qq.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;

public class User extends Entity implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String QQNum;//	QQ号
	private String password;//密码
	private String niCheng;//昵称
	private String sex;//性别
	private int age;//年龄
	private String xingZuo;//星座
	private Date birthday;//生日日期
	private String qianMing;//个性签名
	private byte[] head_Image;//头像图片,需要工具来获取
	private String zhiYe;//职业
	private String gongSi;//公司
	private String address;//所在地
	private String homeAddress;//家乡
	private String email;//邮箱
	private String descript;//个人说明
	private String school;//学校
	private String phone;//电话

	public User(){}

	protected User(Parcel in) {
		QQNum = in.readString();
		password = in.readString();
		niCheng = in.readString();
		sex = in.readString();
		age = in.readInt();
		xingZuo = in.readString();
		qianMing = in.readString();
		head_Image = in.createByteArray();
		zhiYe = in.readString();
		gongSi = in.readString();
		address = in.readString();
		homeAddress = in.readString();
		email = in.readString();
		descript = in.readString();
		school = in.readString();
		phone = in.readString();
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public String getQQNum() {
		return QQNum;
	}
	public void setQQNum(String qQNum) {
		QQNum = qQNum;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNiCheng() {
		return niCheng;
	}
	public void setNiCheng(String niCheng) {
		this.niCheng = niCheng;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getXingZuo() {
		return xingZuo;
	}
	public void setXingZuo(String xingZuo) {
		this.xingZuo = xingZuo;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getQianMing() {
		return qianMing;
	}
	public void setQianMing(String qianMing) {
		this.qianMing = qianMing;
	}
	public byte[] getHead_Image() {
		return head_Image;
	}
	public void setHead_Image(byte[] headImage) {
		this.head_Image = headImage;
	}
	public String getZhiYe() {
		return zhiYe;
	}
	public void setZhiYe(String zhiYe) {
		this.zhiYe = zhiYe;
	}
	public String getGongSi() {
		return gongSi;
	}
	public void setGongSi(String gongSi) {
		this.gongSi = gongSi;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "User [QQNum=" + QQNum + ", password=" + password + ", niCheng=" + niCheng + ", sex=" + sex + ", age="
				+ age + ", xingZuo=" + xingZuo + ", birthday=" + birthday + ", qianMing=" + qianMing + ", headImage="
				 + ", zhiYe=" + zhiYe + ", gongSi=" + gongSi + ", address=" + address
				+ ", homeAddress=" + homeAddress + ", email=" + email + ", descript=" + descript + ", school=" + school
				+ ", phone=" + phone + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(QQNum);
		dest.writeString(password);
		dest.writeString(niCheng);
		dest.writeString(sex);
		dest.writeInt(age);
		dest.writeString(xingZuo);
		dest.writeString(qianMing);
		dest.writeByteArray(head_Image);
		dest.writeString(zhiYe);
		dest.writeString(gongSi);
		dest.writeString(address);
		dest.writeString(homeAddress);
		dest.writeString(email);
		dest.writeString(descript);
		dest.writeString(school);
		dest.writeString(phone);
	}
}
