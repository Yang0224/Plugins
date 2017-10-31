package com.utils.yunxin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * 云信IM
 * <p></p>
 * @author YangShaoPing 2017年4月18日 下午3:24:33
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年4月18日
 * @modify by reason:{方法名}:{原因}
 */
@SuppressWarnings({ "deprecation", "resource" })
public class IM {
	
	
	
	/**
	 * 创建IM
	 * @author yangshaoping 2017年4月18日 下午3:59:14
	 * @param accid		云信ID，最大长度32字符，必须保证一个APP内唯一(建议取用户ID)
	 * @throws Exception
	 */
	public static boolean createIM(String accid) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
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
        nvps.add(new BasicNameValuePair("accid", accid));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        int code = jb.getInt("code");
        if(code == 200){
        	return true;
        }
        return false;
    }
	
	/**
	 * 更新并获取新token  登录
	 * @author yangshaoping 2017年5月24日 下午5:16:37
	 * @param accid
	 * @return
	 * @throws Exception
	 */
	public static String refreshToken(String accid) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/refreshToken.action";
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
        nvps.add(new BasicNameValuePair("accid", accid));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        System.out.println(jb);
        
        int code = jb.getInt("code");
        if(code == 200){
        	JSONObject info = jb.getJSONObject("info");
        	if(info.isNull("token")){
        		return null;
        	}
        	return info.getString("token"); 
        }
        return null;
    }
	
	/**
	 * 封禁网易云通信ID
	 * 
	 * 禁用某个网易云通信ID的IM功能
	 * 封禁网易云通信ID后，此ID将不能登陆网易云通信imserver
	 * 
	 * @author yangshaoping 2017年5月27日 下午5:39:15
	 * @param accid
	 * @return
	 * @throws Exception
	 */
	public static boolean block(String accid) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/block.action";
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
        nvps.add(new BasicNameValuePair("accid", accid));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        System.out.println(jb);
        
        int code = jb.getInt("code");
        if(code == 200){
        	return true;
        }
        return false;
    }
	
	/**
	 * 解禁网易云通信ID
	 * 
	 * 解禁被封禁的网易云通信ID
	 * 
	 * @author yangshaoping 2017年5月27日 下午5:40:41
	 * @param accid
	 * @return
	 * @throws Exception
	 */
	public static boolean unblock(String accid) throws Exception{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/unblock.action";
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
        nvps.add(new BasicNameValuePair("accid", accid));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        String fruit = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(fruit);
        
        JSONObject jb = new JSONObject(fruit);
        System.out.println(jb);
        
        int code = jb.getInt("code");
        if(code == 200){
        	return true;
        }
        return false;
    }
	
	public static void main(String[] args) {
		try {
			System.out.println(createIM("600059"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}