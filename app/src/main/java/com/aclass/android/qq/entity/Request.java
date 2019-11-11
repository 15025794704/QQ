package com.aclass.android.qq.entity;

import java.io.Serializable;

public class Request implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 1.select，有返回查询的数据,需要写查询语句，obj填写需要的javaBean对象实例，返回list<E>
	 * 2.update，返回的boolean,执行成功与否
	 * 3.delete，返回的boolean,执行成功与否
	 * 4.insert,返回的boolean,执行成功与否
	 * 5.消息发送类,无返回
	 * 6.上传图片
	 * 7.获取图片
	 * 8.视频请求
	 * 9.视频回复
	 * 10.添加好友请求
	 * 11.添加好友请求回复
	 * 500.已接收到数据报
	 */
	private int requestType=1;

	/**
	 * 执行的sql语句
	 */
	private String sql;

	/**
	 * 该次请求需要发送的对象，如果没有，可以为null
	 * 在发送消息的时候用到
	 * 在上传图片的时候用到
	 */
	private Object obj;


	public Request(int requestType, String sql, Object obj) {
		this.requestType = requestType;
		this.sql=sql;
		this.obj = obj;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}

}
