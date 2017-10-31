package com.utils.qiniu.entry;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.utils.qiniu.config.QiniuConfig;
import com.utils.qiniu.main.Client;
import com.utils.qiniu.main.Config;
import com.utils.qiniu.main.PiliException;
import com.utils.qiniu.main.utils.UrlSafeBase64;

@Slf4j
public class FileTool {
	
	private static Client client;
	
	private static Client getClient() {
		if(client == null){
			client = new Client(QiniuConfig.ACCESSKEY, QiniuConfig.SECREKEY);
		}
		return client;
	}
	
	/**
     * 保存直播录像
     * @author yangshaoping 2017年5月23日 下午5:21:48
     * @param streamKey		流名
     * @return
     */
    public static JSONObject saveVideo(String streamKey, Date start, Date end){
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("format", "mp4");
    	params.put("notify", QiniuConfig.VIDEO_NOTIFY);
    	params.put("start", start.getTime()/1000);
    	params.put("end", end.getTime()/1000);
    	//params.put("fname", CommonUtils.getUUID());
    	return new JSONObject(saveVideo(streamKey, params));
    }
    
    /**
     * 保存直播录像
     * @author yangshaoping 2017年5月23日 下午5:19:54
     * @param streamKey		流名
     * @param opts		参数 
     *  fname	保存的文件名，可选，不指定系统会随机生成。
	 *	start	整数，可选，Unix 时间戳，要保存的直播的起始时间，不指定或 0 值表示从第一次直播开始。
	 *	end		整数，可选，Unix 时间戳，要保存的直播的结束时间，不指定或 0 值表示当前时间。
	 *	format	保存的文件格式，可选，默认为m3u8，如果指定其他格式，则保存动作为异步模式。详细信息可以参考 转码 的api
	 *	pipeline	异步模式时，dora的私有队列，可选，不指定则使用公共队列
	 *	notify		异步模式时，保存成功回调通知地址，可选，不指定则不通知
	 *	expireDays	更改ts文件的过期时间，可选，默认为永久保存 -1表示不更改ts文件的生命周期，正值表示修改ts文件的生命周期为expireDays
     * @return
     * @throws PiliException 
     */
    private static String saveVideo(String streamKey, Map<String, Object> opts){
    	
    	String ekey = UrlSafeBase64.encodeToString(streamKey);		//流转码
        String baseUrl = String.format("%s%s/v2/hubs/%s/streams/%s", Config.APIHTTPScheme, Config.APIHost, QiniuConfig.HUB, ekey);
    	
    	 String path = baseUrl + "/saveas";
         String json = new Gson().toJson(opts);

         try {
             String resp = getClient().cli.callWithJson(path, json);
             
             return resp;
         } catch (PiliException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
    }
    
    /**
     * 上传文件
     * @author yangshaoping 2017年6月27日 下午2:07:19
     * @param file
     */
    public static Map<String, Object> uploadFile(MultipartFile file) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	//构造一个带指定Zone对象的配置类
    	Configuration cfg = new Configuration(Zone.zone1());
    	//...其他参数参考类注释
    	UploadManager uploadManager = new UploadManager(cfg);
    	//...生成上传凭证，然后准备上传
    	String accessKey = QiniuConfig.ACCESSKEY;
    	String secretKey = QiniuConfig.SECREKEY;
    	String bucket = QiniuConfig.BUCKET;
    	
    	//生成随机文件名
    	String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		String key = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileType;
    	
    	Auth auth = Auth.create(accessKey, secretKey);
    	String upToken = auth.uploadToken(bucket);
    	try {
    		/**
    		 * MultipartFile ==>> File
    		 */
    		CommonsMultipartFile cf = (CommonsMultipartFile)file;   
            DiskFileItem fi = (DiskFileItem) cf.getFileItem();  
            File f = fi.getStoreLocation();  
    		
    	    Response response = uploadManager.put(f, key, upToken);
    	    //解析上传成功的结果
    	    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
    		System.out.println(putRet.key);
    		System.out.println(putRet.hash);
    		
    		result.put("key", putRet.key);
    		result.put("hash", putRet.hash);
    		result.put("url", getFilePath(putRet.key));
    		
    	} catch (QiniuException ex) {
    	    Response r = ex.response;
    	    System.err.println(r.toString());
    	    try {
    	        System.err.println(r.bodyString());
    	        log.info(r.bodyString());
    	    } catch (QiniuException ex2) {
    	        log.error(ex2.toString());
    	    }
    	}
    	return result;
    }
    
    /**
     * 获取文件完整路径
     * @author yangshaoping 2017年6月5日 下午1:48:36
     * @param fileKey
     * @return
     */
    public static String getFilePath(String fileKey){
		try {
	    	String domainOfBucket = QiniuConfig.DOMAIN;
	    	String fileName = URLEncoder.encode(fileKey, "utf-8");
			String finalUrl = String.format("%s/%s", domainOfBucket, fileName);
	    	
	    	return finalUrl;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(fileKey + " 转码错误" + e);
		}
    	return null;
    }
    
    /**
     * 删除文件
     * @author yangshaoping 2017年6月5日 下午1:41:55
     * @param fileKey
     * @return
     */
    public static boolean removeFile(String fileKey){
    	//构造一个带指定Zone对象的配置类
    	Configuration cfg = new Configuration(Zone.zone0());
    	//...其他参数参考类注释
    	String accessKey = QiniuConfig.ACCESSKEY;
    	String secretKey = QiniuConfig.SECREKEY;
    	String bucket = QiniuConfig.BUCKET;
    	Auth auth = Auth.create(accessKey, secretKey);
    	BucketManager bucketManager = new BucketManager(auth, cfg);
    	try {
    		
    		bucketManager.delete(bucket, fileKey);
    		
    		return true;
    	} catch (QiniuException ex) {
    	    //如果遇到异常，说明删除失败
    	    log.error("文件删除失败" + ex.response.toString());
    	}
		return false;
    }
    
    public static void main(String[] args) {
    	//uploadFile(new File("E:\\Administrator\\桌面\\1B956C763D9217802F31DDBA89F2A602.jpg"));
    	//System.out.println(removeFile("cfaf6e295b814518831055ff37d0aa55.mp4"));
    	System.out.println(getFilePath("3e519dfb2f5445b798bed246e128f433.mp4"));
	}
    
}
