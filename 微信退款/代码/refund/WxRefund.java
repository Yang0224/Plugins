package com.utils.wx.refund;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.utils.wx.refund.config.RefundConfig;
import com.utils.wx.refund.info.RefundResult;
import com.utils.wx.refund.utils.ClientCustomSSL;
import com.utils.wx.refund.utils.RequestHandler;
import com.utils.wx.refund.utils.TenpayUtil;

/**
 * 微信退款
 * <p></p>
 * @author YangShaoPing 2017年5月18日 上午10:57:10
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月18日
 * @modify by reason:{方法名}:{原因}
 */
public class WxRefund {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		/*String str = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><err_code_des><![CDATA[交易未结算资金不足，请使用可用余额退款]]></err_code_des></xml>";
		
		parse(str);*/
		
		try {
			System.out.println(refund("281495099723002", 1.00));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 微信退款
	 * @author yangshaoping 2017年5月18日 上午11:18:56
	 * @param orderNo	订单号
	 * @param price		订单价格、总价
	 * @param refundPrice	退款金额
	 */
	private static RefundResult wechatRefund(String orderNo, double price, double refundPrice){
		
		RefundResult result = new RefundResult();
		
		String out_refund_no = UUID.randomUUID().toString().substring(0, 32);	//退款单号，随机生成 
		int total_fee = (int) (price * 100);			//订单的总金额,以分为单位
		int refund_fee = (int) (refundPrice * 100);		//退款金额，以分为单位
		String nonce_str = TenpayUtil.random(10);		//随机字符串
		String appid = RefundConfig.APP_ID;				//商户平台对应平台的APPID 	例如：开放平台、公众平台的APPID
		String appsecret = RefundConfig.APP_SECRET;		//和appid对应的secret
		String mch_id =  RefundConfig.MCH_ID;			//商户平台的商户号
		String op_user_id = RefundConfig.MCH_ID;		//操作员帐号, 默认为商户号
		String partnerkey = RefundConfig.PARTNER_KEY;	//API密钥   商户平台 ==>> API安全 ==> API密钥 ==> 设置密钥(设置之后的那个值就是partnerkey，32位)
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);			
		packageParams.put("mch_id", mch_id);		
		packageParams.put("nonce_str", nonce_str);				//随机字符串
		packageParams.put("out_trade_no", orderNo);				//商户系统内部订单号   支付时定义的订单号，非微信订单号
		//packageParams.put("transaction_id", out_trade_no);	//微信生成的订单号，在支付通知中有返回
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee+"");
		packageParams.put("refund_fee", refund_fee+"");
		packageParams.put("op_user_id", op_user_id);
	
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);
		
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + 
				"<appid>" + appid + "</appid>" + 
				"<mch_id>" + mch_id + "</mch_id>" + 
				"<nonce_str>" + nonce_str + "</nonce_str>" + 
				"<sign><![CDATA[" + sign + "]]></sign>"	+ 
				"<out_trade_no>" + orderNo + "</out_trade_no>"	+ 
				"<out_refund_no>" + out_refund_no + "</out_refund_no>" + 
				"<total_fee>" + total_fee + "</total_fee>" + 
				"<refund_fee>" + refund_fee + "</refund_fee>" + 
				"<op_user_id>" + op_user_id + "</op_user_id>" + 
				"</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		try {
			String refundResult= ClientCustomSSL.doRefund(createOrderURL, xml);
			
			return parse(refundResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 订单退款		默认为退全款
	 * @author yangshaoping 2017年5月18日 上午11:21:46
	 * @param orderNo		订单编号
	 * @param price	 		订单金额 = 退款金额
	 * @return
	 * @throws Exception
	 */
	public static RefundResult refund(String orderNo, double price) throws Exception{
		//默认为退全款     如有其他要求，请自行修改
		return wechatRefund(orderNo, price, price);
	}
	
	/**
	 * 解析xml
	 * @author yangshaoping 2017年5月18日 下午6:41:29
	 * @param protocolXML
	 */
	public static RefundResult parse(String protocolXML) {
		
		RefundResult re = new RefundResult();
        try {   
             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
             DocumentBuilder builder = factory.newDocumentBuilder();   
             Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));   
             
             Element root = doc.getDocumentElement();
             
             NodeList codes = root.getElementsByTagName("return_code");
             NodeList msgs = root.getElementsByTagName("return_msg");
             NodeList errcodes = root.getElementsByTagName("err_code");
             NodeList des = root.getElementsByTagName("err_code_des");
             
             re.setRetrueCode(codes.item(0).getFirstChild().getNodeValue());
             if(msgs != null && msgs.getLength() > 0){
            	 re.setReturnMsg(msgs.item(0).getFirstChild().getNodeValue());
             }
             if(des != null && des.getLength() > 0){
            	 re.setErrCodeDes(des.item(0).getFirstChild().getNodeValue());
             }
             if(errcodes != null && errcodes.getLength() > 0){
            	 re.setErrCode(errcodes.item(0).getFirstChild().getNodeValue());
             }
             
         } catch (Exception e) {   
             e.printStackTrace();
             
             re.setRetrueCode("FAIL");
             re.setReturnMsg("ERROR");
             re.setErrCode("FAIL");
             re.setErrCodeDes("xml解析错误");
         }
         return re;
     }   
}
