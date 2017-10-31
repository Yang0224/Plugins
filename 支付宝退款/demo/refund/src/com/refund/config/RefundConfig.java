package com.refund.config;

/**
 * 退款配置文件
 * <p></p>
 * @author YangShaoPing 2017年5月18日 下午3:49:15
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月18日
 * @modify by reason:{方法名}:{原因}
 */
public final class RefundConfig {
	
		/**
		 * https://openhome.alipay.com/platform/keyManage.htm
		 * 开放平台密钥 ==> APPID
		 */
		public static final String APP_ID = "";
		
		/**
		 * https://openhome.alipay.com/platform/keyManage.htm
		 * 开放平台密钥  ==>  RSA2(SHA256)密钥(推荐)  ==>  设置应用公钥  ==>  生成工具（支付宝RAS密钥生成器）
		 *  ==>  生成后的公钥复制到网站的输入框中保存  ==>  此处取rsa_private_key_pkcs8.pem文件中的字符串（去掉开头与结尾，去掉换行、空格等）
		 */
		public static final String PRIVATE_KEY = "";
		
		/**
		 * https://openhome.alipay.com/platform/keyManage.htm
		 * 开放平台密钥  ==>  RSA2(SHA256)密钥(推荐)  ==>  查看支付宝公钥
		 */
		public static final String PUBLIC_KEY = "";
		
	
}
