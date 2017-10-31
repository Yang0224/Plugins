package com.utils.wx.access.config;

/**
 * 微信入口配置
 * <p></p>
 * @author YangShaoPing 2017年5月31日 下午3:46:42
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月31日
 * @modify by reason:{方法名}:{原因}
 */
public class AccessConfig {
	
		//公众号网页授权信息
		public static final String APP_ID = "";		
		public static final String APP_SECRET = "";
		
		//授权地址
		public static final String ACCESS_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
		//用户信息
		public static final String INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	
}
