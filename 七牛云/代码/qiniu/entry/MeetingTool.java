package com.utils.qiniu.entry;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.qiniu.config.QiniuConfig;
import com.utils.qiniu.main.Client;
import com.utils.qiniu.main.Meeting;
import com.utils.qiniu.main.PiliException;

/**
 * 会议室工具类
 * <p></p>
 * @author YangShaoPing 2017年6月5日 上午11:03:07
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年6月5日
 * @modify by reason:{方法名}:{原因}
 */
public class MeetingTool {
	
	private static final Logger logger = LoggerFactory.getLogger(MeetingTool.class);
	
	private static Client client;
	private static Meeting meeting;
	
	private static Client getClient() {
		if(client == null){
			client = new Client(QiniuConfig.ACCESSKEY, QiniuConfig.SECREKEY);
		}
		return client;
	}
	
	private static Meeting getMeeting() {
		if(meeting == null){
			meeting = getClient().newMeeting();
		}
		return meeting;
	}
	
    /**
     * 创建会议室房间（连麦房间）
     * @author yangshaoping 2017年6月5日 上午10:31:51
     * @param roomName		创建的房间名称，可选，最大长度64位
     * @param ownerId		创建房间的所有者
     * @return
     */
    public static boolean createRoom(String roomName, String ownerId){
        try {
            String rname =  getMeeting().createRoom(ownerId,roomName);

            if(!rname.equals(roomName)){
            	logger.error("Error roomName:[" + roomName + "],rname:[" + rname + "]");
            	return false;
            }
            return true;
        } catch (PiliException e){
            e.printStackTrace();
            logger.error("连麦异常");
        }
        return false;
    }
    
    /**
     * 删除会议室房间
     * @author yangshaoping 2017年6月5日 上午10:56:52
     * @param roomName
     * @param ownerId
     * @return
     */
    public static boolean deleteRoom(String roomName){
        try {
            getMeeting().deleteRoom(roomName);

            return true;
        } catch (PiliException e){
            e.printStackTrace();
            logger.error("删除连麦房间异常");
        }
        return false;
    }
    
    /**
     * 移除会议室成员
     * @author yangshaoping 2017年6月5日 上午10:58:36
     * @param roomName
     * @param userId
     * @return
     */
    public static boolean rejectUser(String roomName, String userId){
        try {
            getMeeting().rejectUser(roomName, userId);

            return true;
        } catch (PiliException e){
            e.printStackTrace();
            logger.error("移除连麦用户异常");
        }
        return false;
    }
    
    /**
     * 获取roomToken  进行连麦
     * @author yangshaoping 2017年6月5日 上午10:46:43
     * @param roomName		房间名称
     * @param joinUserId	请求加入房间的用户ID
     * @param perm			该用户的房间管理权限，"admin"或"user"
     * @param hours			roomToken过期时间 单位：小时
     * @return
     */
    public static String roomToken(String roomName, String joinUserId, String perm, int hours) {
    	try {
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.HOUR, hours);
    		
            return getMeeting().roomToken(roomName, joinUserId, perm, calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error: " + e.toString());
        }
    	return null;
    }
    
}
