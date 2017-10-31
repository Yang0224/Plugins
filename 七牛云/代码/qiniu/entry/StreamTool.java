package com.utils.qiniu.entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.qiniu.config.QiniuConfig;
import com.utils.qiniu.main.Client;
import com.utils.qiniu.main.Hub;
import com.utils.qiniu.main.PiliException;
import com.utils.qiniu.main.Stream;

/**
 * 直播流工具类
 * <p></p>
 * @author YangShaoPing 2017年6月5日 上午11:02:53
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年6月5日
 * @modify by reason:{方法名}:{原因}
 */
public class StreamTool {
	
	private static final Logger logger = LoggerFactory.getLogger(StreamTool.class);
	
	/*URL
	 RTMP推流地址: client.RTMPPublishURL(domain, HUB, streamKey, expireAfterDays)
	 RTMP直播地址: RTMPPlayURL(domain, HUB, streamKey)
	 HLS直播地址: HLSPlayURL(domain, HUB, streamKey)
	 HDL直播地址: HDLPlayURL(domain, HUB, streamKey)
	 直播封面地址: SnapshotPlayURL(domain, HUB, streamKey)
	HUB
	 创建流: HUB.create(streamKey)
	 查询流: HUB.get(streamKey)
	 列出流: HUB.list(prefix, limit, marker)
	 列出正在直播的流: HUB.listLive(prefix, limit, marker)
	 批量查询直播实时信息: HUB.batchLiveStatus(streamTitles)
	Stream
	 流信息: stream.info()
	 禁用流: stream.disable() / stream.disable(disabledTill)
	 解禁流: stream.enable()
	 查询直播状态: stream.liveStatus()
	 保存直播回放: stream.save(key, start, end) / stream.save(saveOptions)
	 保存直播截图: stream.snapshot(snapshotOptions) zzz
	 更改流的实时转码规格: stream.updateConverts(profiles)
	 查询直播历史: stream.historyRecord(start, end)*/
	
	private static final String RTMP_PUSH = "pili-publish.app.okznz.net";			//rtmp推流域名
	private static final String RTMP_PULL = "pili-live-rtmp.app.okznz.net";		//rtml拉流域名
	private static final String HLS_PULL = "pili-live-hls.app.okznz.net";			//hls拉流域名
	//private static final String TS_PULL = "pili-media.app.okznz.net";			//TS 切片拉流域名
	private static final String HDL_PULL = "pili-live-hdl.app.okznz.net";			//hdl拉流域名
	private static final String SNAPSHOT = "pili-live-snapshot.app.okznz.net";		//直播封面域名
	
	private static Client client;
	private static Hub hub;
	
	private static Client getClient() {
		if(client == null){
			client = new Client(QiniuConfig.ACCESSKEY, QiniuConfig.SECREKEY);
		}
		return client;
	}
	
	private static Hub getHub() {
		if(hub == null){
			hub = new Hub(getClient().cli, QiniuConfig.HUB);
		}
		return hub;
	}
	
	/**
	 * 创建流
	 * @author yangshaoping 2017年5月23日 下午1:41:56
	 * @param streamKey
	 * @param minutes
	 * @return
	 */
	public static boolean createStream(String stream) {
		try {
			Stream s = getHub().create(stream);
			if(s != null){
				return true;
			}
		} catch (PiliException e) {
			logger.info("流创建失败  Exception e = " + e);
			e.printStackTrace();
		}
		return false;
    }
	
	/**
     * 生成rtmp推流地址
     * @author yangshaoping 2017年5月9日 下午2:03:44
     * @param streamKey	流名
     * @param minutes	推流地址的过期时间	分钟数
     * @return
     */
    public static String RTMPPublishURL(String streamKey, int minutes) {
    	return getClient().RTMPPublishURL(RTMP_PUSH, QiniuConfig.HUB, streamKey, minutes * 1000 * 60);
    }

    /**
     * 生成rtmp拉流地址
     * @author yangshaoping 2017年5月9日 下午2:05:14
     * @param streamKey	流名
     * @return
     */
    public static String RTMPPlayURL(String streamKey) {
        return String.format("rtmp://%s/%s/%s", RTMP_PULL, QiniuConfig.HUB, streamKey);
    }

    /**
     * 生成hls拉流地址
     * @author yangshaoping 2017年5月9日 下午2:06:28
     * @param domain
     * @param HUB
     * @param streamKey
     * @return
     */
    public static String HLSPlayURL(String streamKey) {
        return String.format("http://%s/%s/%s.m3u8", HLS_PULL, QiniuConfig.HUB, streamKey);
    }

    /**
     * 生成hdl拉流地址
     * @author yangshaoping 2017年5月9日 下午2:06:44
     * @param domain
     * @param HUB
     * @param streamKey
     * @return
     */
    public static String HDLPlayURL(String streamKey) {
        return String.format("http://%s/%s/%s.flv", HDL_PULL, QiniuConfig.HUB, streamKey);
    }

    /**
     * 生成直播封面的地址
     * @author yangshaoping 2017年5月9日 下午2:07:27
     * @param domain
     * @param HUB
     * @param streamKey
     * @return
     */
    public static String SnapshotPlayURL(String streamKey) {
        return String.format("http://%s/%s/%s.jpg", SNAPSHOT, QiniuConfig.HUB, streamKey);
    }
    
}
