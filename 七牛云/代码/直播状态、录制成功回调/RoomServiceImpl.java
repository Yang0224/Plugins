package com.whcd.app.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.memory.platform.core.service.impl.BaseServiceImpl;
import com.utils.CommonUtils;
import com.utils.huanxin.Messages;
import com.utils.qiniu.config.QiniuConfig;
import com.whcd.app.dao.IRoomDao;
import com.whcd.app.model.Room;
import com.whcd.app.service.IRoomService;
import com.whcd.app.web.controller.BasicController;

@Service("roomService")
public class RoomServiceImpl extends BaseServiceImpl<Object> implements IRoomService{

	private static final Logger log = LoggerFactory.getLogger(BasicController.class);
	
	@Autowired
	@Qualifier("roomDao")
	private IRoomDao roomDao;

	@Override
	public boolean liveStatusCallback(InputStream is) throws Exception {
		
		String obj = CommonUtils.convertStreamToString(is);		//解析对象流
		
		//解析json对象
		JSONObject jo = new JSONObject(obj);
		JSONObject data = jo.getJSONObject("data");
		String id = data.getString("id");
		
		/**
		 * connected, 即流由断开状态变为推流状态
		 * disconnected, 断开
		 */
		String status = data.getString("status");	
		//String status = "connected";
		
		String userId = id.split(QiniuConfig.HUB)[1].replace(".", "");
		//String userId = "600005";
		
		Date now = new Date();		//当前时间
		
		//获取最近的一条直播
		Room room = roomDao.getByHql("from Room where userId='" + userId + "' and status <> 2 order by beginTime desc");
		if(room == null){
			return false;
		}
		
		log.error("房间ID =================>> " + room.getRoomId());
		log.error("room =================>> " + room.toString());
		
		if("connected".equals(status)){		//推流
			log.error("开始推流 ===================>> ");
			
			if(room.getPushTime() == null){
				
				log.error("第一次推流 ===================>> 设置开始推流时间 ");
			}else if(room.getBreakTime() != null){
				
				log.error("断流后恢复推流 ===================>>  ");
				Long breakTime = room.getBreakTime().getTime();		//断流时间戳
				Long nowTime = now.getTime();				//当前时间戳
				if((nowTime-breakTime) > 30000){		//如果超过三十秒
					
					log.error("恢复推流时间超过30秒，开始关闭房间 ===================>> ");
				}else{
					
					log.error("恢复推流时间未超过30秒，重置断流时间======================>> ");
				}
			}
			
		}else if("disconnected".equals(status)){		//断流
			
			log.error("直播断流 ===================>> ");
		}else{
			return false;
		}
		return true;
	}

	@Override
	public boolean videoCallback(InputStream is) throws Exception {
		
		String obj = CommonUtils.convertStreamToString(is);		//解析对象流
		log.info("videoCallback ==>> " + obj);
		
		//解析json对象
		JSONObject jo = new JSONObject(obj);
		JSONObject items = (JSONObject) jo.getJSONArray("items").getJSONObject(0);
		String key = items.getString("key");
		
		//根据key获取直播信息
		Room room = roomDao.getByHql("from Room where interimReplayUrl=? ", key);
		if(room == null){
			return false;
		}
		room.setReplayUrl(key);
		room.setInterimReplayUrl(null);
		
		update(room);
		
		return true;
	}
	
}
