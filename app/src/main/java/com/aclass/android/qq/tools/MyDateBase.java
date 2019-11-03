package com.aclass.android.qq.tools;

import com.aclass.android.qq.entity.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MyDateBase {
	private static DatagramSocket socket;
	private static String ip="47.107.138.4";
	private static Map<Class<?>,String> table=new HashMap<Class<?>, String>();
	static {
		try {
			socket=new DatagramSocket();
			socket.setSoTimeout(3000);//超时
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		table.put(Friend.class, "T_friends");
		table.put(IP.class, "T_ip");
		table.put(Member.class, "T_members");
		table.put(Message.class, "T_msg");
		table.put(Qun.class, "T_qun");
		table.put(User.class, "T_user");
	}
	
	/**
	 * 接收字节数组数据
	 * @return
	 */
	public static byte[] receiveData() {
		byte[] b=new byte[1024*20];
		DatagramPacket packet=new DatagramPacket(b,b.length);
		try {
			socket.receive(packet);
			byte[] data=new byte[packet.getLength()];
			data=packet.getData();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 接收对象数据
	 * @return
	 */
	public static Object receiveObject() {
		byte[] b=new byte[1024*20];
		DatagramPacket packet=new DatagramPacket(b,b.length);
		try {
			socket.receive(packet);
			 ByteArrayInputStream bis=new ByteArrayInputStream(b,0, b.length);
	         ObjectInputStream ois=new ObjectInputStream(bis);
	         return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送字节数组
	 * @param data
	 */
	public static void UDPsend(byte[] data) {
		try {
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,InetAddress.getByName(ip),889);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送对象
	 * @param obj
	 */
	public static void UDPsend(Object obj) {
		try {
			 ByteArrayOutputStream bos=new ByteArrayOutputStream();
	         ObjectOutputStream output=new ObjectOutputStream(bos);
	         output.writeObject(obj);
	         byte[] data= bos.toByteArray();
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,InetAddress.getByName(ip),889);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 指定客户端地点发送字节数组
	 * 视频通话使用
	 * @param socket
	 * @param address
	 * @param data
	 */
	public static void UDPsend(DatagramSocket socket,SocketAddress address,byte[] data) {
		try {
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,address);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过qq号获取qq实体
	 * @param qqNum
	 * @return
	 */
	public static User getUser(String qqNum){
		Request request=new Request(1, "select * from T_user where QQNum='"+qqNum+"'", new User());
		MyDateBase.UDPsend(request);
		List<User> list=(List<User>)MyDateBase.receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}
	
	/**
	 * 通过qq号获取好友实体的集合
	 * @param qqNum
	 * @return
	 */
	public static List<Friend> getFriends(String qqNum){
		Request request=new Request(1, "select * from T_friends where QQ1='"+qqNum+"'", new Friend());
		MyDateBase.UDPsend(request);
		return (List<Friend>)MyDateBase.receiveObject();
	}
	
	/**
	 * 通过qq号 和 好友的qq号  获取单个好友的实体
	 * @param qqNum
	 * @param qqNumFriend
	 * @return
	 */
	public static Friend getFriend(String qqNum,String qqNumFriend){
		Request request=new Request(1, "select * from T_friends where QQ1='"+qqNum+"' and QQ2='"+qqNumFriend+"'", new Friend());
		MyDateBase.UDPsend(request);
		List<Friend> list=(List<Friend>)MyDateBase.receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}
	
	/**
	 * 通过qq号获取未接收的离线消息实体集合
	 * @param qqNum
	 * @return
	 */
	public static List<Message> getMessages(String qqNum){
		Request request=new Request(1, "select * from T_msg where receiveNum='"+qqNum+"'", new Message());
		MyDateBase.UDPsend(request);
		return (List<Message>)MyDateBase.receiveObject();
	}
	
	/**
	 * 通过群号 获取 群成员实体集合
	 * @param qunID
	 * @return
	 */
	public static List<Member> getMembersByID(String qunID){
		Request request=new Request(1, "select * from T_members where qunID='"+qunID+"'", new Member());
		MyDateBase.UDPsend(request);
		return (List<Member>)MyDateBase.receiveObject();
	}
	
	/**
	 * 通过qq号获取 群成员包含改qq的实体集合
	 * @param qqNum
	 * @return
	 */
	public static List<Member> getMembersByQQ(String qqNum){
		Request request=new Request(1, "select * from T_members where memberQQ='"+qqNum+"'", new Member());
		MyDateBase.UDPsend(request);
		return (List<Member>)MyDateBase.receiveObject();
	}
	
	/**
	 * 通过qq号 和 群id 获取 qq加入的单个群成员实体
	 * @param qunID
	 * @param qqNum
	 * @return
	 */
	public static Member getMemberByQQAndID(String qunID,String qqNum){
		Request request=new Request(1, "select * from T_members where memberQQ='"+qqNum+"' and qunID='"+qunID+"'", new Member());
		MyDateBase.UDPsend(request);
		List<Member> list=(List<Member>)MyDateBase.receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}
	
	/**
	 * 通过群号获取单个群实体
	 * @param qunID
	 * @return
	 */
	public static Qun getQun(String qunID){
		Request request=new Request(1, "select * from T_qun where qunID='"+qunID+"'", new Qun());
		MyDateBase.UDPsend(request);
		List<Qun> list=(List<Qun>)MyDateBase.receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 更新实体对象
	 * Friend，Member，Qun，User
	 * @param entity
	 */
	public static int updateEntity(Entity entity) {
		String sql="update "+table.get(entity.getClass())+" set ";
		Field fields[]=entity.getClass().getDeclaredFields();
		try {
			for(Field field:fields) {
				if(!field.getName().equals("serialVersionUID")) {
					Object value;
//					value = new PropertyDescriptor(field.getName(),entity.getClass()).getReadMethod().invoke(entity);
					String name=field.getName();
					name="get"+name.substring(0,1).toUpperCase()+name.substring(1);
					value = entity.getClass().getMethod(name).invoke(entity);
					sql+="["+field.getName()+"]='"+value+"',";
				}
			}
			sql=sql.substring(0,sql.length()-1);

			Class<?> clazz=entity.getClass();
			if(clazz==Friend.class) {
				sql+=" where QQ1='"+entity.getQQ1()+"' and QQ2='"+entity.getQQ2()+"'";
			}
			else if(clazz==Member.class) {
				sql+=" where qunID='"+entity.getQunID()+"' and memberQQ='"+entity.getMemberQQ()+"'";
			}
			else if(clazz==Qun.class) {
				sql+=" where qunID='"+entity.getQunID()+"'";
			}
			else if(clazz==User.class) {
				sql+=" where QQNum='"+entity.getQQNum()+"'";
			}
			else {
				throw new Exception("其他类不可用，请咨询吴志维");
			}
			Request request=new Request(2, sql, null);
			UDPsend(request);
			return (int)MyDateBase.receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
