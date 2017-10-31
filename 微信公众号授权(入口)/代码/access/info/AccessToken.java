package com.utils.wx.access.info;

import java.io.Serializable;

/**
 * 微信授权 返回数据
 * <p></p>
 * @author YangShaoPing 2016年8月16日 下午2:26:15
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2016年8月16日
 * @modify by reason:{方法名}:{原因}
 */
public class AccessToken implements Serializable{
    
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -7799518757633171389L;

	/**
	 * 错误信息
	 */
	private String errmsg;
	
	private Integer errcode;
	
	/**
	 * 应用授权作用域，
	 * snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
	 * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
	 */
	private String scope;
	
	private String openid;
	
	private Integer expires_in;
	
	private String refresh_token;
	
	private String access_token;

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "WeixinResult [errmsg=" + errmsg + ", errcode=" + errcode
				+ ", scope=" + scope + ", openid=" + openid + ", expires_in="
				+ expires_in + ", refresh_token=" + refresh_token
				+ ", access_token=" + access_token + "]";
	}
}