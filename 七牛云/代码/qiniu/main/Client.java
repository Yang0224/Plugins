package com.utils.qiniu.main;


public final class Client {
    public RPC cli;

    public Client(String accessKey, String secretKey) {
        this.cli = new RPC(new Mac(accessKey, secretKey));
    }

    /**
     * 生成http推流地址
     * @author yangshaoping 2017年5月9日 下午2:03:44
     * @param domain	直播空间绑定的 RTMP 推流域名
     * @param hub		直播空间名
     * @param streamKey	流名
     * @param expireAfterSeconds	推流地址的过期时间	时间戳
     * @return
     */
    public String RTMPPublishURL(String domain, String hub, String streamKey, int expireAfterSeconds) {
        long expire = System.currentTimeMillis() / 1000 + expireAfterSeconds;
        String path = String.format("/%s/%s?e=%d", hub, streamKey, expire);
        String token;
        try {
            token = this.cli.getMac().sign(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return String.format("rtmp://%s%s&token=%s", domain, path, token);
    }

    /**
     * 生成rtmp拉流地址
     * @author yangshaoping 2017年5月9日 下午2:05:14
     * @param domain	直播空间绑定的 RTMP 推流域名
     * @param hub		直播空间名
     * @param streamKey	流名
     * @return
     */
    public String RTMPPlayURL(String domain, String hub, String streamKey) {
        return String.format("rtmp://%s/%s/%s", domain, hub, streamKey);
    }

    /**
     * 生成hls拉流地址
     * @author yangshaoping 2017年5月9日 下午2:06:28
     * @param domain
     * @param hub
     * @param streamKey
     * @return
     */
    public String HLSPlayURL(String domain, String hub, String streamKey) {
        return String.format("http://%s/%s/%s.m3u8", domain, hub, streamKey);
    }

    /**
     * 生成hdl拉流地址
     * @author yangshaoping 2017年5月9日 下午2:06:44
     * @param domain
     * @param hub
     * @param streamKey
     * @return
     */
    public String HDLPlayURL(String domain, String hub, String streamKey) {
        return String.format("http://%s/%s/%s.flv", domain, hub, streamKey);
    }

    /**
     * 生成直播封面的地址
     * @author yangshaoping 2017年5月9日 下午2:07:27
     * @param domain
     * @param hub
     * @param streamKey
     * @return
     */
    public String SnapshotPlayURL(String domain, String hub, String streamKey) {
        return String.format("http://%s/%s/%s.jpg", domain, hub, streamKey);
    }

    
    public Hub newHub(String hub) {
        return new Hub(this.cli, hub);
    }
    
    public Meeting newMeeting() {
        return new Meeting(this.cli);
    }
}









