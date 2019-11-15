package com.aclass.android.qq.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aclass.android.qq.entity.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MyDateBase {
	private  DatagramSocket socket;
	private  SocketAddress sendAddress;
	private static String ip="47.107.138.4";
	private static Map<Class<?>,String> table=new HashMap<Class<?>, String>();
	static {
		table.put(Friend.class, "T_friends");
		table.put(IP.class, "T_ip");
		table.put(Member.class, "T_members");
		table.put(Message.class, "T_msg");
		table.put(Qun.class, "T_qun");
		table.put(User.class, "T_user");
	}

	public MyDateBase(){
		try {
			socket=new DatagramSocket();
			socket.setSoTimeout(3000);//超时
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public MyDateBase(int port){
		try {
			socket=new DatagramSocket(port);
			socket.setSoTimeout(0);//超时
		} catch (SocketException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public DatagramSocket getSokect(){
		return socket;
	}

	public void setTimeout(int timeout){
		try {
			socket.setSoTimeout(timeout);//超时
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 接收字节数组数据
	 * @return
	 */
	public  byte[] receiveData() {
		byte[] b=new byte[1024*100];
		DatagramPacket packet=new DatagramPacket(b,b.length);
		try {
			socket.receive(packet);
			byte[] data=new byte[packet.getLength()];
			for(int i=0;i<packet.getLength();i++)
				data[i]=b[i];
			sendAddress=packet.getSocketAddress();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public  void receiveACK() throws IOException {
		byte[] b=new byte[1024];
		DatagramPacket packet=new DatagramPacket(b,b.length);
			socket.receive(packet);
			byte[] data=new byte[packet.getLength()];
			for(int i=0;i<packet.getLength();i++)
				data[i]=b[i];
			sendAddress=packet.getSocketAddress();
	}

	public SocketAddress getSendAddress(){
		return sendAddress;
	}

    /**
     * 字节转换成对象
     * @param b
     * @return
     */
	public static Object toObject(byte[] b,int length){
		try {
			ByteArrayInputStream bis=new ByteArrayInputStream(b,0, length);
			ObjectInputStream ois=new ObjectInputStream(bis);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * 对象转换成字节数组
     * @param obj
     * @return
     */
	public static byte[] toByteArray(Object obj){
		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream ois=new ObjectOutputStream(bos);
			ois.writeObject(obj);
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 接收对象数据
	 * @return
	 */
	public  Object receiveObject() {
		byte[] b=new byte[1024*100];
		DatagramPacket packet=new DatagramPacket(b,b.length);
		try {
			socket.receive(packet);
			 ByteArrayInputStream bis=new ByteArrayInputStream(b,0, packet.getLength());
	         ObjectInputStream ois=new ObjectInputStream(bis);
			sendAddress=packet.getSocketAddress();
	         return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *关闭端口
	 */
	public void Destory(){
		socket.close();
	}

	/**
	 * 发送字节数组
	 * @param data
	 */
	public  void UDPsend(byte[] data) {
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
	public  void UDPsend(Object obj) {
		try {
	         byte[] data= toByteArray(obj);
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
	public  void UDPsend(int port,Object obj) {
		try {
			byte[] data= toByteArray(obj);
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,InetAddress.getByName(ip),port);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送对象
	 * @param
	 */
	public  void UDPsend(int port,byte[] data) {
		try {
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,InetAddress.getByName(ip),port);
			socket.send(sendpacket);
		} catch (Exception e) {
			Log.e("长度",data.length+"");
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
	public  void UDPsend(DatagramSocket socket,SocketAddress address,byte[] data) {
		try {
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,address);
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
	 */
	public  void UDPsend(DatagramSocket socket,SocketAddress address,Object obj) {
		try {
			byte[] data= toByteArray(obj);
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,address);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定客户端地点发送字节数组
	 * 视频通话使用
	 * @param address
	 * @param data
	 */
	public  void UDPsend(SocketAddress address,byte[] data) {
		try {
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,address);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定客户端地点发送字节数组
	 * 视频通话使用
	 */
	public  void UDPsend(SocketAddress address,Object obj) {
		try {
			byte[] data= toByteArray(obj);
			DatagramPacket sendpacket=new DatagramPacket(data, data.length,address);
			socket.send(sendpacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 数据库中是否存在某个特定的qq账号
	*/
	public boolean isExitqqNum(String qqNum)
	{
		Request request=new Request(1,"select * from T_user where QQNum='"+qqNum+"'",new User());
		UDPsend(request);
		List<User> list= (List<User>) receiveObject();
		if(!list.isEmpty())
		{
			return true;
		}
		else
			return false;
	}

	/**
	 * 通过qq号获取qq实体
	 * @param qqNum
	 * @return
	 */
	public  User getUser(String qqNum){
		Request request=new Request(1, "select * from T_user where QQNum='"+qqNum+"'", new User());
		UDPsend(request);
		List<User> list=(List<User>)receiveObject();
		if(list!=null &&list.size()!=0)
			return list.get(0);
		else
			return null;
	}
	
	/**
	 * 通过qq号获取好友实体的集合
	 * @param qqNum
	 * @return
	 */
	public  List<Friend> getFriends(String qqNum){
		Request request=new Request(1, "select * from T_friends where QQ1='"+qqNum+"'", new Friend());
		UDPsend(request);
		return (List<Friend>)receiveObject();
	}
	
	/**
	 * 通过qq号 和 好友的qq号  获取单个好友的实体
	 * @param qqNum
	 * @param qqNumFriend
	 * @return
	 */
	public  Friend getFriend(String qqNum,String qqNumFriend){
		Request request=new Request(1, "select * from T_friends where QQ1='"+qqNum+"' and QQ2='"+qqNumFriend+"'", new Friend());
		UDPsend(request);
		List<Friend> list=(List<Friend>)receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}
	/*
	* 通过组名获取好友列表
	*
	* */
	public List<Friend> getFriendsByqqGroup(String qqGroupName)
	{
		Request request=new Request(1,"select * from T_friends where QQgroup='"+qqGroupName+"'",new Friend());
		UDPsend(request);
		List<Friend> list= (List<Friend>) receiveObject();
		return list;
	}
	
	/**
	 * 通过qq号获取未接收的离线消息实体集合
	 * @param qqNum
	 * @return
	 */
	public  List<Message> getMessages(String qqNum){
		Request request=new Request(1, "select * from T_msg where receiveNum='"+qqNum+"'", new Message());
		UDPsend(request);
		return (List<Message>)receiveObject();
	}
	
	/**
	 * 通过群号 获取 群成员实体集合
	 * @param qunID
	 * @return
	 */
	public  List<Member> getMembersByID(String qunID){
		Request request=new Request(1, "select * from T_members where qunID='"+qunID+"'", new Member());
		UDPsend(request);
		return (List<Member>)receiveObject();
	}
	
	/**
	 * 通过qq号获取 群成员包含改qq的实体集合
	 * @param qqNum
	 * @return
	 */
	public  List<Member> getMembersByQQ(String qqNum){
		Request request=new Request(1, "select * from T_members where memberQQ='"+qqNum+"'", new Member());
		UDPsend(request);
		return (List<Member>)receiveObject();
	}

	/**
	 * 通过qq号 和 群id 获取 qq加入的单个群成员实体
	 * @param qunID
	 * @param qqNum
	 * @return
	 */
	public  Member getMemberByQQAndID(String qunID,String qqNum){
		Request request=new Request(1, "select * from T_members where memberQQ='"+qqNum+"' and qunID='"+qunID+"'", new Member());
		UDPsend(request);
		List<Member> list=(List<Member>)receiveObject();
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
	public  Qun getQun(String qunID){
		Request request=new Request(1, "select * from T_qun where qunID='"+qunID+"'", new Qun());
		UDPsend(request);
		List<Qun> list=(List<Qun>)receiveObject();
		if(list.size()!=0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 更新实体对象
	 * 返回更新成功的条数
	 * Friend，Member，Qun，User
	 * @param entity
	 */
	public  int updateEntity(Entity entity) {
		String sql="update "+table.get(entity.getClass())+" set ";
		Field fields[]=entity.getClass().getDeclaredFields();
		try {
			for(Field field:fields) {
				if(Modifier.isStatic(field.getModifiers()))
					continue;
				if(!field.getName().equals("serialVersionUID") && !field.getName().equals("head_Image")) {
					Object value;
//					value = new PropertyDescriptor(field.getName(),entity.getClass()).getReadMethod().invoke(entity);
					String name=field.getName();
					name="get"+name.substring(0,1).toUpperCase()+name.substring(1);
					value = entity.getClass().getMethod(name).invoke(entity);

					if(value==null){
						sql+="["+field.getName()+"]=null,";
						continue;
					}
					if(value.getClass()==Date.class)
						sql+="["+field.getName()+"]='"+((Date)value).toLocaleString()+"',";
					else
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
			return (int)receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 新加实体对象
	 * 返回加入成功的条数
	 * Friend，Member，Qun，User
	 * @param entity
	 */
	public  int insertEntity(Entity entity) {
		String sql = "insert into ["+table.get(entity.getClass())+"] ";
		String feildStr="(";
		String valueStr=" (";
		Field fields[]=entity.getClass().getDeclaredFields();
		try {
			for(Field field:fields) {
				if(Modifier.isStatic(field.getModifiers()))
					continue;
				if(!field.getName().equals("serialVersionUID") && !field.getName().equals("head_Image")
						&& !(field.getName().equals("id")&&entity.getClass()==Message.class)) {
					feildStr+="["+field.getName()+"],";
					Object value;
					String name=field.getName();
					name="get"+name.substring(0,1).toUpperCase()+name.substring(1);
					value = entity.getClass().getMethod(name).invoke(entity);
					if(value==null){
						valueStr+="null,";
						continue;
					}
					if(value.getClass()== Date.class)
						valueStr+="'"+((Date)value).toLocaleString()+"',";
					else
						valueStr+="'"+value+"',";
				}
			}
			valueStr=valueStr.substring(0,valueStr.length()-1)+")";
			feildStr=feildStr.substring(0,feildStr.length()-1)+")";
			sql=sql+feildStr+" VALUES "+valueStr;
			Request request=new Request(4, sql, null);
			UDPsend(request);
			return (int)receiveObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取qq的头像
	 * 如果qq不存在就返回null
	 * @param QQ
	 * @return
     */
	public Bitmap getImageByQQ(String QQ){
		Bitmap bitmap= null;
		byte[] b=new byte[1024*60];
		DatagramPacket packet=new DatagramPacket(b,b.length);
		try {
			UDPsend(new Request(7,QQ,null));
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			for(;;) {
				socket.receive(packet);
				if(packet.getLength()==0)
					break;
				bos.write(b,0,packet.getLength());
			}
			if(bos.size()==0) {
				bos.close();
				return null;
			}
			bitmap=BitmapFactory.decodeByteArray(bos.toByteArray(),0,bos.size());
			bos.close();
			return bitmap;
		}
		catch (Exception e){}
		return null;
	}
}
