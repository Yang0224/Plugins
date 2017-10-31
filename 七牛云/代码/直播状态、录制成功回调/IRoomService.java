package com.whcd.app.service;

import java.io.InputStream;

import com.memory.platform.core.service.IBaseService;

public interface IRoomService extends IBaseService<Object>{

	/**
	 * 直播状态回调
	 * @author yangshaoping 2017年6月5日 下午4:38:14
	 * @param is
	 * @throws Exception
	 */
	public abstract boolean liveStatusCallback(InputStream is) throws Exception;
	
	/**
	 * 直播录制回调
	 * @author yangshaoping 2017年7月21日 下午5:14:11
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public abstract boolean videoCallback(InputStream is) throws Exception;
}
