package com.utils.qiniu.main;

import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**‘
 * 请求鉴权
 * <p></p>
 * @author YangShaoPing 2017年5月10日 上午9:30:46
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月10日
 * @modify by reason:{方法名}:{原因}
 */
public final class RPC {
    private Mac mac;
    private OkHttpClient okHttpClient;

    public RPC(Mac mac) {
        this.mac = mac;
        this.okHttpClient = new OkHttpClient();
    }

    public Mac getMac() {
        return mac;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public String callWithJson(String urlStr, String json) throws Exception {
        String contentType = "application/json";
        URL url = new URL(urlStr);
        byte[] body = json.getBytes("UTF-8");
        String macToken = mac.signRequest(url, "POST", body, contentType);
        RequestBody rBody = RequestBody.create(MediaType.parse(contentType), body);
        Request request = new Request.Builder()
                .url(url)
                .post(rBody)
                .header("User-Agent", Config.APIUserAgent)
                .addHeader("Authorization", "Qiniu " + macToken)
                .addHeader("Content-Type", contentType)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new PiliException(response);
        }
    }

    public String callWithGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        String macToken = mac.signRequest(url, "GET", null, null);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", Config.APIUserAgent)
                .addHeader("Authorization", "Qiniu " + macToken)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new PiliException(response);
        }
    }
    
    public String callWithDelete(String urlStr) throws Exception{
        URL url = new URL(urlStr);
        String macToken = mac.signRequest(url, "DELETE", null,null);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .header("User-Agent", Config.APIUserAgent)
                .addHeader("Authorization", "Qiniu " + macToken)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new PiliException(response);
        }
    }
}
