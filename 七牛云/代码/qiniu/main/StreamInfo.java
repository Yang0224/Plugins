package com.utils.qiniu.main;

/**
 * 流信息
 * <p></p>
 * @author YangShaoPing 2017年5月9日 下午5:32:15
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月9日
 * @modify by reason:{方法名}:{原因}
 */
public final class StreamInfo {
    private String hub;
    private String key;
    // the disabled until when, 0 means no disabled, -1 means disabled forever.
    private long disabledTill;

    // codec profiles
    private String[] converts;

    StreamInfo(String hub, String key) {
        this.hub = hub;
        this.key = key;
    }

    void setMeta(String hub, String key) {
        this.key = key;
        this.hub = hub;
    }

    public String getHub() {
        return hub;
    }

    public long getDisabledTill() {
        return disabledTill;
    }

    public String getKey() {
        return key;
    }

    public String[] getConverts() {
        return converts;
    }
}
