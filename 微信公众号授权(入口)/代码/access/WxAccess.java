package com.utils.wx.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.wx.access.config.AccessConfig;
import com.utils.wx.access.info.AccessToken;
import com.utils.wx.access.info.WxInfo;
import com.utils.wx.access.utils.HttpTool;

/**
 * 微信公众号授权 入口
 * <p></p>
 * @author YangShaoPing 2017年5月24日 下午3:06:01
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月24日
 * @modify by reason:{方法名}:{原因}
 */
@SuppressWarnings("unchecked")
public class WxAccess {
	
	private static final Logger logger = LoggerFactory.getLogger(WxAccess.class);
	
	/**
	 * 回调中返回的参数
	 * @author yangshaoping 2017年5月31日 下午2:55:24
	 * @param code  微信请求时会给一个code过来
	 * @return
	 * @throws Exception
	 */
	public static AccessToken getAccessToken(String code) throws Exception{
		
		//请求
		StringBuffer url = new StringBuffer();
		url.append(AccessConfig.ACCESS_URL);
		url.append("?appid=" + AccessConfig.APP_ID);
		url.append("&secret=" + AccessConfig.APP_SECRET);
		url.append("&code=" + code);
		url.append("&grant_type=authorization_code");
		
		String result = HttpTool.doPosts(url.toString(), "").toString();
		
		logger.info("AccessToken = " + result);
		
		return toObject(AccessToken.class, result);
	}
	
	/**
	 * 微信用户信息
	 * @author yangshaoping 2016年8月23日 下午4:04:19
	 * @param accessToken
	 * @param openId
	 * @throws Exception
	 */
	public static WxInfo wxInfo(String accessToken,String openId) throws Exception{
		//请求
		StringBuffer url = new StringBuffer();
		url.append(AccessConfig.INFO_URL);
		url.append("?access_token=" + accessToken);
		url.append("&openid=" + openId);
		url.append("&lang=zh_CN");
		
		String result = HttpTool.doPosts(url.toString(), "").toString();
		
		logger.info("WxInfo = " + result);
		
		return toObject(WxInfo.class, result);
	}
	
	
	private static <T> T toObject(Class <T> t, String json){
		
		net.sf.json.JSONObject jo = net.sf.json.JSONObject.fromObject(json);
		T result = (T) net.sf.json.JSONObject.toBean(jo, t);
		
		return result;
	}
}
