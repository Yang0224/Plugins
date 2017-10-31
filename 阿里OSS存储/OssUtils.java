package com.utils.ali.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;

public class OssUtils {
	
	private static final String EDP_POINT = "";					//OSS外网域名
	private static final String ACCESS_KEY_ID = "";				//AccessKey 获取方法文档中有说明
	private static final String ACCESS_KEY_ID_SCRECT = "";		//ScrectKey 获取方法文档中有说明
	private static final String BUCKET_NAME = "";				//空间名  创建空间时自定义的名称
	
	public static void main(String[] args) throws FileNotFoundException {
		//System.out.println(putObject("E:\\Administrator\\桌面\\picture\\0bd162d9f2d3572c21a659248d13632763d0c386.jpg"));
		System.out.println(ImageSample("0bd162d9f2d3572c21a659248d13632763d0c386.jpg", "image/resize,w_10", 50));
		
	}
	
	/**
	 * 图片处理后的访问路径
	 * @author yangshaoping 2017年5月20日 下午5:02:43
	 * @param key		
	 * @param style		图片处理样式		https://help.aliyun.com/document_detail/44688.html?spm=5176.doc44687.6.932.taR4en
	 * @param monutes	过期时间	  分钟数
	 * @return
	 */
	public static String ImageSample(String key, String style, int monutes) {
		
		OSSClient ossClient = new OSSClient(EDP_POINT, ACCESS_KEY_ID, ACCESS_KEY_ID_SCRECT);
	    // 过期时间
	    Date expiration = new Date(new Date().getTime() + 1000 * 60 * monutes);
	    
	    GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(BUCKET_NAME, key, HttpMethod.GET);
	    req.setExpiration(expiration);		//设置过期时间
	    req.setProcess(style);			//设置图片处理样式
	    URL signedUrl = ossClient.generatePresignedUrl(req);
		
		ossClient.shutdown();
		
	    return signedUrl.toString();
	}
	
	/**
	 * 根据key获取图片路径
	 * @author yangshaoping 2017年5月20日 下午3:22:51
	 * @param key
	 * @return
	 */
	public static String getResourceUrl(String key) {
		OSSClient client = new OSSClient(EDP_POINT, ACCESS_KEY_ID, ACCESS_KEY_ID_SCRECT);
		Date expiration = new Date(new Date().getTime() + 36000 * 1000);	// URL过期时间设置为10个小时
		URL url = client.generatePresignedUrl(BUCKET_NAME, key, expiration);
		try {
			return url.toURI().toString();
		} catch (URISyntaxException e) {
			return null;
		} finally {
			client.shutdown();
		}
	}
	
	/**
	 * 上传文件  根据图片绝对路径上传（本地测试使用）
	 * @author yangshaoping 2017年5月20日 下午5:08:39
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String putObject(String filePath) throws FileNotFoundException {
		
		// 初始化OSSClient
		OSSClient client = new OSSClient(EDP_POINT, ACCESS_KEY_ID, ACCESS_KEY_ID_SCRECT);
		
		// 获取指定文件的输入流
		File file = new File(filePath);
		InputStream content = new FileInputStream(file);
		
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		
		// 必须设置ContentLength
		meta.setContentLength(file.length());
		
		// 上传Object.
		client.putObject(BUCKET_NAME, file.getName(), content, meta);
		
		// 打印ETag
//		System.out.println(result.getETag());
//		System.out.println(getResourceUrl(file.getName()));
		
		client.shutdown();

		return file.getName();
	}
	
	/**
	 * 上传文件
	 * @author yangshaoping 2017年5月20日 下午3:23:35
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> uploadObject(MultipartFile file) throws Exception {
		
		// 初始化OSSClient
		OSSClient client = new OSSClient(EDP_POINT, ACCESS_KEY_ID, ACCESS_KEY_ID_SCRECT);
		
		// 获取指定文件的输入流
		InputStream content = file.getInputStream();
		
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		
		// 必须设置ContentLength
		meta.setContentLength(file.getSize());
		
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		String newFileName = UUID.randomUUID().toString().replaceAll("-", "")+"."+fileType;

		
		// 上传Object.
		client.putObject(BUCKET_NAME, newFileName, content, meta);
		
		Map<String, Object> date = new HashMap<String, Object>();
		date.put("key", newFileName);
		date.put("url", getResourceUrl(newFileName));
		// 打印ETag
//		System.out.println(result.getETag());
//		System.out.println(getResourceUrl(newFileName));
		client.shutdown();

		return date;
	}
	
	
	
}
