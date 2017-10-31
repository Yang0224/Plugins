package com.utils.ali.getway.info;

public final class GatWayResult {
	
	private String code;		//网关返回码	
	
	private String msg;			//网关返回码描述
	
	private String subCode;		//业务返回码
	
	private String subMsg;		//业务返回码描述
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSubMsg() {
		return subMsg;
	}

	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}
	
	
}
