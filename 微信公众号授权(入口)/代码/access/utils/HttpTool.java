package com.utils.wx.access.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.json.JSONObject;

import com.memory.platform.common.util.MySSLSocketFactory;

@SuppressWarnings("deprecation")
public class HttpTool {
	public static String is2Str(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	public static String doPosts(String url, NameValuePair[] nameValuePairs) {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		if (nameValuePairs != null) {
			post.setRequestBody(nameValuePairs);
		}

		try {
			client.executeMethod(post);
			return is2Str(post.getResponseBodyAsStream());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	public static JSONObject is2Json(String is) throws IOException {
		return new JSONObject(is);
	}
	

	public static JSONObject doPosts(String url, String params) throws UnsupportedEncodingException {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");
		if (params != null) {
//			post.setRequestBody(params);
			InputStream in = new ByteArrayInputStream(params.getBytes("utf-8"));
			post.setRequestBody(in);
		}

		try {
			client.executeMethod(post);
			return is2Json(is2Str(post.getResponseBodyAsStream()));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}
	
	public static Object doPosts(String url) throws UnsupportedEncodingException {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");

		try {
			client.executeMethod(post);
			return is2Str(post.getResponseBodyAsStream());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	public static JSONObject doPostsWithToken(String url, String params, String token) throws UnsupportedEncodingException {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");
		if (token != null) {
			post.setRequestHeader("Authorization", "Bearer " + token);
		}
		if (params != null) {
//			post.setRequestBody(params);
			InputStream in = new ByteArrayInputStream(params.getBytes("utf-8"));
			post.setRequestBody(in);
		}

		try {
			client.executeMethod(post);
			return is2Json(is2Str(post.getResponseBodyAsStream()));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	public static int doPosts(String url, String params, String token) throws UnsupportedEncodingException {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");
		if (token != null) {
			post.setRequestHeader("Authorization", "Bearer " + token);
		}
		if (params != null) {
//			post.setRequestBody(params);
			InputStream in = new ByteArrayInputStream(params.getBytes("utf-8"));
			post.setRequestBody(in);
		}

		try {
			client.executeMethod(post);
			return post.getStatusCode();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return 0;
	}

	public static int doDelete(String url, String token) {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		DeleteMethod delete = new DeleteMethod(url);
		if (token != null) {
			delete.setRequestHeader("Authorization", "Bearer " + token);
		}

		try {
			client.executeMethod(delete);
			return delete.getStatusCode();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			delete.releaseConnection();
		}
		return 0;
	}

	public static int doPuts(String url, String params, String token) throws UnsupportedEncodingException {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		PutMethod put = new PutMethod(url);
		if (token != null) {
			put.setRequestHeader("Authorization", "Bearer " + token);
		}
		if (params != null) {
//			put.setRequestBody(params);
			InputStream in = new ByteArrayInputStream(params.getBytes("utf-8"));
			put.setRequestBody(in);
		}

		try {
			client.executeMethod(put);
			return put.getStatusCode();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			put.releaseConnection();
		}
		return 0;
	}

	public static JSONObject doGet(String url, String token) {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);

		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);
		get.setRequestHeader("Content-Type", "application/json");
		if (token != null) {
			get.setRequestHeader("Authorization", "Bearer " + token);
		}

		try {
			client.executeMethod(get);
			return is2Json(is2Str(get.getResponseBodyAsStream()));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return null;
	}
	
	public static String doPost(String url, NameValuePair[] nameValuePairs) {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		if (nameValuePairs != null) {
			post.setRequestBody(nameValuePairs);
		}

		try {
			client.executeMethod(post);
			return is2Str(post.getResponseBodyAsStream());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	public static JSONObject doDelete2(String url, String token) {
		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		HttpClient client = new HttpClient();
		DeleteMethod delete = new DeleteMethod(url);
		delete.setRequestHeader("Content-Type", "application/json");
		if (token != null) {
			delete.setRequestHeader("Authorization", "Bearer " + token);
		}

		try {
			client.executeMethod(delete);
			return is2Json(is2Str(delete.getResponseBodyAsStream()));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			delete.releaseConnection();
		}
		return null;
	}
}