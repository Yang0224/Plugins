package com.utils.ali.zhima;

import java.util.Date;

import org.json.JSONObject;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.alipay.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.alipay.api.request.ZhimaCustomerCertificationQueryRequest;
import com.alipay.api.response.ZhimaCustomerCertificationCertifyResponse;
import com.alipay.api.response.ZhimaCustomerCertificationInitializeResponse;
import com.alipay.api.response.ZhimaCustomerCertificationQueryResponse;
import com.memory.platform.common.util.DateUtil;
import com.utils.ali.zhima.config.ZhimaConfig;

public class Zhima {
	
	public static void main(String[] args) throws AlipayApiException {
		String transactionId = getTransactionId();
		System.out.println(transactionId);
		String bizno = initialize(transactionId, "杨绍平", "510***********2752");
		System.out.println(bizno);
		String url = certify(bizno);
		System.out.println(url);
		//query(bizno);
	}
	
	/**
	 * 认证初始化  
	 * @author yangshaoping 2017年5月24日 下午6:18:42
	 * @param transaction_id		//商户请求的唯一标志，32位长度的字母数字下划线组合
	 * @throws AlipayApiException
	 */
	public static String initialize(String transaction_id, String cert_name, String cert_no) throws AlipayApiException{
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ZhimaConfig.APP_ID, ZhimaConfig.PRIVATE_KEY,"json","UTF8", ZhimaConfig.PUBLIC_KEY, "RSA2");
		ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
		
		JSONObject identity = new JSONObject();		//身份信息
		identity.append("identity_type", "CERT_INFO");		//证件类型为身份证
		identity.append("cert_type", "IDENTITY_CARD");		//必要信息	
		//identity.append("cert_name", "");		
		//identity.append("cert_no", "");			//cert_name、cert_no可以选填  商户的用户主体
		
		StringBuffer params = new StringBuffer();
		params.append("{");
		params.append("\"transaction_id\":\"" + transaction_id + "\",");		//商户请求的唯一标志，32位长度的字母数字下划线组合 建议:前面几位字符是商户自定义的简称,中间可以使用一段日期,结尾可以使用一个序列
		params.append("\"product_code\":\"w1010100000000002978\",");			//芝麻认证产品码
		params.append("\"biz_code\":\"FACE\",");		//认证场景码,常用的场景码有: FACE:人脸认证
		params.append("\"identity_param\":\"{\\\"identity_type\\\":\\\"CERT_INFO\\\",\\\"cert_type\\\":\\\"IDENTITY_CARD\\\",\\\"cert_name\\\":\\\"" + cert_name + "\\\",\\\"cert_no\\\":\\\"" + cert_no + "\\\"}\",");
		params.append("\"ext_biz_param\":\"{}\"");		//对应用户在商户端唯一标识 如果商户传了principal_id,后续会为商户提供更强大功能
		params.append("}");
		
		System.out.println(params);
		
		request.setBizContent(params.toString());
		//request.setNotifyUrl("http://www.wehang.net/flblive/appservice/wx/zhima");
		
		ZhimaCustomerCertificationInitializeResponse response = alipayClient.execute(request);
		
		if(response.isSuccess()){
			System.out.println("调用成功");
			if(response.getBizNo() != null){
				return response.getBizNo();
			}
		} else {
			System.out.println("调用失败");
		}
		return null;
	}
	
	/**
	 * 开始认证
	 * @author yangshaoping 2017年5月26日 上午10:22:41
	 * @param bizno
	 * @return
	 * @throws AlipayApiException
	 */
	public static String certify(String bizno) throws AlipayApiException{
		
		// 获取alipay client
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ZhimaConfig.APP_ID, ZhimaConfig.PRIVATE_KEY,"json","UTF-8", ZhimaConfig.PUBLIC_KEY,"RSA2");
		ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
		 
		// 设置业务参数,必须要biz_no
		request.setBizContent("{\"biz_no\":\"" + bizno + "\"}");
		 
		// 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议
		// alipay://www.taobao.com 或者 alipays://www.taobao.com,分别对应http和https请求
		// 设置业务参数,必须要biz_no
		request.setReturnUrl("alipays://www.taobao.com");
		//request.setNotifyUrl("http://www.wehang.net/flblive/appservice/wx/zhima");
		 
		// 这里一定要使用GET模式
		ZhimaCustomerCertificationCertifyResponse response = alipayClient.pageExecute(request, "GET");
		// 从body中获取URL
		String url = response.getBody();
		System.out.println("generateCertifyUrl url:" + url);
		return url;
	}
	
	/**
	 * 查询
	 * @author yangshaoping 2017年5月26日 上午11:18:37
	 * @param bizno
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean query(String bizno) throws AlipayApiException{
		
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", ZhimaConfig.APP_ID, ZhimaConfig.PRIVATE_KEY,"json","UTF-8", ZhimaConfig.PUBLIC_KEY,"RSA2");
		ZhimaCustomerCertificationQueryRequest request = new ZhimaCustomerCertificationQueryRequest();
		request.setBizContent("{\"biz_no\":\"" + bizno + "\"}");
		ZhimaCustomerCertificationQueryResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			if("1000".equals(response.getCode())){
				if("true".equals(response.getPassed())){
					//通过
					return true;
				}else{
					System.out.println(response.getFailedReason());
				}
			}
		} else {
			System.out.println("调用失败");
		}
		return false;
	}
	
	
	
	/**
	 * 获取唯一标识
	 * @author yangshaoping 2017年5月26日 上午10:56:51
	 * @return
	 */
	private static String getTransactionId(){
		Date now = new Date();
		String time = new String("WHCD");
		time += DateUtil.dateToString(now, "yyyy");
		time += DateUtil.dateToString(now, "MM");
		time += DateUtil.dateToString(now, "dd");
		time += DateUtil.dateToString(now, "hh");
		time += DateUtil.dateToString(now, "mm");
		time += DateUtil.dateToString(now, "ss");
		for (int i = 0; i < 7; i++) {
			time += (int)(Math.random()*(9-0 + 1)+0);
		}
		return time;
	}
	
}
