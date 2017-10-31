package com.utils.wx.transfers.config;


/**
 * 微信企业付款配置
 * <p></p>
 * @author YangShaoPing 2017年5月31日 下午2:23:22
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月31日
 * @modify by reason:{方法名}:{原因}
 */
public class TransfersConfig {
	
	public static final String APP_ID = "";		//公众平台的APPID 	
	public static final String APP_SECRET = "";			//和appid对应的secret
	public static final String MCH_ID = "";	//商户平台的商户号
	
	//API密钥   商户平台 ==>> API安全 ==> API密钥 ==> 设置密钥(设置之后的那个值就是partnerkey，32位)
	public static final String PARTNER_KEY = "";
	
}
