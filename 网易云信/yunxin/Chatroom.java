package com.utils.yunxin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.utils.yunxin.config.Config;
import com.utils.yunxin.utils.CheckSumBuilder;

/**
 * 云信聊天室
 * <p></p>
 * @author YangShaoPing 2017年4月18日 下午3:47:18
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年4月18日
 * @modify by reason:{方法名}:{原因}
 */
@SuppressWarnings({ "deprecation", "resource" })
public class Chatroom {
	
	public static void main(String[] args) {
		try {
			System.out.println(createChatroom("600000", "测试"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建聊天室
	 * @author yangshaoping 2017年4月18日 下午3:49:27
	 * @param creator	聊天室属主的账号accid
	 * @param name		聊天室名称，长度限制128个字符
	 * @throws Exception
	 */
	public static String createChatroom(String creator, String name) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/chatroom/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = Config.APPKEY;
        String appSecret = Config.APP_SECRET;
        String nonce =  CheckSumBuilder.getNonce();
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("creator", creator));
        nvps.add(new BasicNameValuePair("name", name));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        // 打印执行结果
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        int code = jb.getInt("code");
        if(code == 200){
        	JSONObject chatroom = jb.getJSONObject("chatroom");
        	int roomid = chatroom.getInt("roomid");
        	return String.valueOf(roomid);
        }
        return null;
	}
	
	/**
	 * 查询聊天室信息
	 * @author yangshaoping 2017年4月18日 下午4:05:19
	 * @param roomid		聊天室id
	 * @param needOnlineUserCount		是否需要返回在线人数，true或false，默认false
	 * @throws Exception
	 */
	public static JSONObject chatroomInfo(String roomid, boolean needOnlineUserCount) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/chatroom/get.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = Config.APPKEY;
        String appSecret = Config.APP_SECRET;
        String nonce =  CheckSumBuilder.getNonce();
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("needOnlineUserCount", String.valueOf(needOnlineUserCount)));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        
        // 打印执行结果
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        
        int code = jb.getInt("code");
        if(code == 200){
        	JSONObject chatroom = jb.getJSONObject("chatroom");
        	return chatroom;
        }
        return null;
	}
	
	/**
	 * 发送聊天室消息
	 * @author yangshaoping 2017年4月18日 下午4:29:38
	 * @param roomid		聊天室id
	 * @param fromAccid		消息发出者的账号accid
	 * @param msgType		消息类型：0: 表示文本消息， 1: 表示图片， 2: 表示语音， 3: 表示视频， 4: 表示地理位置信息，6: 表示文件，10: 表示Tips消息，100: 自定义消息类型
	 * @param msg			消息内容
	 * @param ext			消息扩展字段，内容可自定义，请使用JSON格式，长度限制4096字符
	 * @throws Exception
	 */
	public static void sendMsg(String roomid, String fromAccid, int msgType, String msg, String ext) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/chatroom/get.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = Config.APPKEY;
        String appSecret = Config.APP_SECRET;
        String nonce =  CheckSumBuilder.getNonce();
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码
        
        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        Map<String, Object> attach = new HashMap<String, Object>();
        attach.put("msg", msg);
        
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("roomid", roomid));
        nvps.add(new BasicNameValuePair("msgId", UUID.randomUUID().toString().replaceAll("-", "")));
        nvps.add(new BasicNameValuePair("fromAccid", fromAccid));
        nvps.add(new BasicNameValuePair("msgType", String.valueOf(msgType)));
        nvps.add(new BasicNameValuePair("attach", new JSONObject(attach).toString()));
        nvps.add(new BasicNameValuePair("ext", ext));
        
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	}
	
}
