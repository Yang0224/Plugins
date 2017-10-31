package com.refund;

import java.text.DecimalFormat;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.refund.config.RefundConfig;
import com.refund.info.RefundResult;

public final class AliRefund {
	
	private static final Logger logger = LoggerFactory.getLogger(AliRefund.class);
	
	public static void main(String[] args) {
		try {
			System.out.println(refund("341495079659722", 1.00, "asd").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 退款
	 * @author yangshaoping 2017年5月18日 下午4:03:33
	 * @param orderNo	订单支付时传入的商户订单号
	 * @param price		需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
	 * @param reason 	退款的原因说明
	 * @return
	 * @throws Exception
	 */
	public static RefundResult refund(String orderNo, double price, String reason) {
		
		RefundResult result = new RefundResult();
		try {
			
			String refund_amount = new DecimalFormat("######0.00").format(price);		//金额保留两位小数
			String request_no = randomStr(10);		//随机字符串   标识一次退款请求
			
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", RefundConfig.APP_ID, 
					RefundConfig.PRIVATE_KEY, "json","utf-8", RefundConfig.PUBLIC_KEY, "RSA2");
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			
			StringBuffer params = new StringBuffer();
			params.append("{");
			params.append("\"out_trade_no\":\"" + orderNo + "\",");		//订单支付时传入的商户订单号,不能和 trade_no同时为空。
			//params.append("\"trade_no\":\"2014112611001004680073956707\",");		//支付宝交易号，和商户订单号不能同时为空
			params.append("\"refund_amount\": " + refund_amount + ",");		//需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
			params.append("\"refund_reason\":\"" + reason + "\",");		//退款的原因说明
			params.append("\"out_request_no\":\"" + request_no  + "\",");		//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
			params.append("\"operator_id\":\"OP001\",");		//商户的操作员编号
			params.append("\"store_id\":\"NJ_S_001\",");		//商户的门店编号
			params.append("\"terminal_id\":\"NJ_T_001\"");		//商户的终端编号
			params.append("}");
			
			request.setBizContent(params.toString());
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			
			if(response.isSuccess()){
				System.out.println("调用成功");
				logger.info("=====================>  调用成功");
			} else {
				System.out.println("调用失败");
				logger.info("=====================>  调用失败");
			}
			result.setCode(response.getCode());
			result.setMsg(response.getMsg());
			result.setSubCode(response.getSubCode());
			result.setSubMsg(response.getSubMsg());
		} catch (Exception e) {
			
			result.setCode("-1");
			result.setMsg("Function Error");
			result.setSubCode("ACQ.TRADE_STATUS_ERROR");
			result.setSubMsg("方法异常，请求退款参数使用不正确、公钥私钥长度不正确..");
		}
		return result;
		
	}
	
	/**
	 * 生成随机字符串
	 * @author yangshaoping 2017年5月18日 上午9:53:52
	 * @param size
	 * @return
	 */
	private static String randomStr(int size){
		String str = "1234567890ABCDEFGHIJKLMNOPQRSTVNWXYZ";
		String result = "";
		
		for (int i = 0; i < size; i++) {
			result += str.charAt(new Random().nextInt(str.length()));
		}
		return result;
	}
}
