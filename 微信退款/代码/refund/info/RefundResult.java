package com.utils.wx.refund.info;

/**
 * 微信退款返回
 * <p></p>
 * @author YangShaoPing 2017年5月18日 下午5:49:26
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月18日
 * @modify by reason:{方法名}:{原因}
 */
public final class RefundResult {
	
	private String retrueCode;		//返回状态码  SUCCESS  or  FAIL
	
	private String returnMsg;		//返回信息  result_code 为 SUCCESS时，返回为空

	private String resultCode;		//SUCCESS/FAIL  SUCCESS退款申请接收成功，结果通过退款查询接口查询 

	private String errCode;			//错误代码
	
	private String errCodeDes;		//错误代码描述
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getRetrueCode() {
		return retrueCode;
	}

	public void setRetrueCode(String retrueCode) {
		this.retrueCode = retrueCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	@Override
	public String toString() {
		return "RefundResult [retrueCode=" + retrueCode + ", returnMsg="
				+ returnMsg + ", resultCode=" + resultCode + ", errCode="
				+ errCode + ", errCodeDes=" + errCodeDes + "]";
	}

	
	//...其余信息如要使用 请自行添加   https://pay.weixin.qq.com/wiki/doc/api/wap.php?chapter=9_4
	
}
