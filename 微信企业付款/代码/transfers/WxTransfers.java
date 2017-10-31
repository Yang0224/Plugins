package com.utils.wx.transfers;

import java.io.IOException;
import java.io.StringReader;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lijing.wechatpay.conn.PaymentTools;
import com.utils.wx.transfers.config.TransfersConfig;
import com.utils.wx.transfers.info.TransfersResult;
import com.utils.wx.transfers.utils.ClientCustomSSL;
import com.utils.wx.transfers.utils.RequestHandler;
import com.utils.wx.transfers.utils.TenpayUtil;

/**
 * 微信企业付款
 * <p></p>
 * @author YangShaoPing 2017年5月31日 下午4:15:36
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月31日
 * @modify by reason:{方法名}:{原因}
 */
public class WxTransfers {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		try {
			System.out.println(transfers("12345645787545", "ovU4ow0xXKi-2Cor137j645cZ0E0", 100, "退款"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 企业向个人转账
	 * @author yangshaoping 2017年5月31日 下午4:15:10
	 * @param orderNo		商户订单号，需保持唯一性
	 * @param openid		公众号中用户对应的openid，微信授权（入口）是获取
	 * @param amount		金额  以分为单位，最低1元
	 * @param desc			企业付款描述信息
	 * @return
	 */
	public static TransfersResult transfers(String orderNo, String openid, int amount, String desc){
		
		String appid = TransfersConfig.APP_ID;				//商户平台对应平台的APPID 	例如：开放平台、公众平台的APPID
		String appsecret = TransfersConfig.APP_SECRET;		//和appid对应的secret
		String partnerkey = TransfersConfig.PARTNER_KEY;	//API密钥   商户平台 ==>> API安全 ==> API密钥 ==> 设置密钥(设置之后的那个值就是partnerkey，32位)
		String mch_id =  TransfersConfig.MCH_ID;			//商户平台的商户号
		
		String nonce_str = TenpayUtil.random(10);		//随机字符串
		String ip = PaymentTools.getServerIP();		//调用接口的机器Ip地址
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("mch_appid", appid);			
		packageParams.put("mchid", mch_id);		
		packageParams.put("nonce_str", nonce_str);				//随机字符串
		packageParams.put("partner_trade_no", orderNo);				//商户订单号，需保持唯一性
		packageParams.put("openid", openid);
		packageParams.put("check_name", "NO_CHECK");			//校验用户姓名选项  NO_CHECK：不校验真实姓名    FORCE_CHECK：强校验真实姓名
		//packageParams.put("re_user_name", username);			//收款用户真实姓名。  如果check_name设置为FORCE_CHECK，则必填用户真实姓名
		packageParams.put("amount", String.valueOf(amount));
		packageParams.put("desc", desc);
		packageParams.put("spbill_create_ip", ip);
	
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);
		
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + 
				"<mch_appid>" + appid + "</mch_appid>" + 
				"<mchid>" + mch_id + "</mchid>" + 
				"<nonce_str>" + nonce_str + "</nonce_str>" + 
				"<sign><![CDATA[" + sign + "]]></sign>"	+ 
				"<partner_trade_no>" + orderNo + "</partner_trade_no>"	+ 
				"<openid>" + openid + "</openid>" + 
				"<check_name>NO_CHECK</check_name>" + 
				"<amount>" + amount + "</amount>" + 
				"<desc>" + desc + "</desc>" + 
				"<spbill_create_ip>" + ip + "</spbill_create_ip>" + 
				"</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		try {
			String refundResult= ClientCustomSSL.doTransfers(createOrderURL, xml);
			
			return parse(refundResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析xml
	 * @author yangshaoping 2017年5月18日 下午6:41:29
	 * @param protocolXML
	 */
	public static TransfersResult parse(String protocolXML) {
		
		TransfersResult re = new TransfersResult();
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
