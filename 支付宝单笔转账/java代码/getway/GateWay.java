package com.utils.ali.getway;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.utils.ali.getway.config.GateWayConfig;
import com.utils.ali.getway.info.GatWayResult;

public class GateWay {
	
	public static void main(String[] args) throws AlipayApiException {
		gateWay("6123114564156456", "ALIPAY_LOGONID", "eaftmq8354@sandbox.com", 1, "成都伟航创达科技有限公司提现", "沙箱环境", "提现");
	}
	
	/**
	 * 转账  
	 * @author yangshaoping 2017年5月24日 下午2:06:56
	 * @param out_biz_no		商户转账唯一订单号。发起转账来源方定义的转账单据ID，用于将转账回执通知给来源方。 
	 * @param payee_type		收款方账户类型。ALIPAY_USERID：支付宝账号对应的支付宝唯一用户号。ALIPAY_LOGONID：支付宝登录号，支持邮箱和手机号格式。
	 * @param payee_account		收款方账户。与payee_type配合使用。
	 * @param amount			转账金额，单位：元。
	 * @param payer_show_name	付款方显示姓名  如果不传，则默认显示该账户在支付宝登记的实名
	 * @param payee_real_name	收款方真实姓名  如果本参数不为空，则会校验该账户在支付宝登记的实名是否与收款方真实姓名一致。
	 * @param remark			转账备注	且转账金额达到（大于等于）50000元，remark不能为空。
	 * @throws AlipayApiException
	 */
	private static GatWayResult gateWay(String out_biz_no, String payee_type, String payee_account, double amount, String payer_show_name, String payee_real_name, String remark) throws AlipayApiException{
		
		GatWayResult gwr = new GatWayResult();
		
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", GateWayConfig.APP_ID, GateWayConfig.PRIVATE_KEY,"json","GBK", GateWayConfig.PUBLIC_KEY,"RSA2");
		
		//沙箱测试环境
		//AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", GateWayConfig.APP_ID, GateWayConfig.PRIVATE_KEY,"json","GBK", GateWayConfig.PUBLIC_KEY,"RSA2");
		
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		
		StringBuffer params = new StringBuffer();
		params.append("{");
		params.append("\"out_biz_no\":\"" + out_biz_no + "\",");			//商户转账唯一订单号。发起转账来源方定义的转账单据ID，用于将转账回执通知给来源方。 
		params.append("\"payee_type\":\"" + payee_type + "\",");			//收款方账户类型。ALIPAY_USERID：支付宝账号对应的支付宝唯一用户号。ALIPAY_LOGONID：支付宝登录号，支持邮箱和手机号格式。
		params.append("\"payee_account\":\"" + payee_account + "\",");		//收款方账户。与payee_type配合使用。
		params.append("\"amount\":\"" + amount + "\"");						//转账金额，单位：元。
		if(payer_show_name != null){
			params.append(",\"payer_show_name\":\"" + payer_show_name + "\"");	//付款方显示姓名  如果不传，则默认显示该账户在支付宝登记的实名
		}
		if(payee_real_name != null){
			params.append(",\"payee_real_name\":\"" + payee_real_name + "\"");	//收款方真实姓名  如果本参数不为空，则会校验该账户在支付宝登记的实名是否与收款方真实姓名一致。
		}
		if(remark != null){
			params.append(",\"remark\":\"" + remark + "\"");					//转账备注	且转账金额达到（大于等于）50000元，remark不能为空。
		}
		params.append("}");
		
		request.setBizContent(params.toString());
		AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
		
		System.out.println(request);
		if(response.isSuccess()){
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
		}
		gwr.setCode(response.getCode());
		gwr.setMsg(response.getMsg());
		gwr.setSubCode(response.getSubCode());
		gwr.setSubMsg(response.getSubMsg());
		
		return gwr;
	}
	
	/**
	 * 
	 * @author yangshaoping 2017年5月26日 下午3:59:46
	 * @param out_biz_no		商户转账唯一订单号。发起转账来源方定义的转账单据ID，用于将转账回执通知给来源方。 
	 * @param payee_account		收款方账户。与payee_type配合使用。
	 * @param amount			转账金额，单位：元。
	 * @return
	 * @throws AlipayApiException
	 */
	public static GatWayResult gateWay(String out_biz_no, String payee_account, double amount) throws AlipayApiException{
		
		return gateWay(out_biz_no, "ALIPAY_LOGONID", payee_account, amount, "伟航创达科技有限公司", null, null);
	}
	
}
