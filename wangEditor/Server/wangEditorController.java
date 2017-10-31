package com.whcd.manage.web.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.memory.platform.common.util.DateUtil;
import com.utils.image.ImageHelper;

@Controller
@RequestMapping(value = "/manage/wangEditor")
public class wangEditorController {
	
	private static final Logger logger = LoggerFactory.getLogger(wangEditorController.class);
	
	/**
	 * wangEditor图片上传 
	 * 
	 * @author yangshaoping 2017年5月27日 下午4:27:17
	 * @param request
	 * @param response
	 * 
	 * 返回字符串 图片访问路径或者错误信息，
	 * 此处必须有response.getWriter().writer('');
	 * 如果直接  return "...";	返回给前端的内容为 "..." ,多了引号，wangEditor解析失败
	 */
	@RequestMapping(value="/wangEditorUpload",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void wangEditorUpload(HttpServletRequest request, HttpServletResponse response){

		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mRequest.getFile("wangEditorH5File");
			
			response.setContentType("textml;charset=UTF-8");
	        request.setCharacterEncoding("UTF-8");
	        
	        long filesize = file.getSize();
			if(filesize/1024/1024 > 200){
				pw.write("图片不能大于5M,上传失败");
			}else if(filesize/1024 < 5){
				pw.write("图片不能小于5k,上传失败");
			}
			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
		
			Date now = new Date();
			String datefolder = "/" + DateUtil.dateToString(now, "yyyy") + DateUtil.dateToString(now, "MM") + DateUtil.dateToString(now, "dd");		// 日期命名的文件夹
			String webParentPath = new File(request.getSession().getServletContext().getRealPath("/")).getParent().replace("\\", "/");		// 当前WEB环境的上层目录
			
			String realPath = webParentPath + "/flb_editor" + datefolder + "/" + fileName;		// 文件压缩到服务器的真实路径
			String IP = "http://" + request.getLocalAddr() + ":" + request.getLocalPort();
			String filePath = IP + "/flb_editor" + datefolder + "/" + fileName;

			File create = new File(realPath);		//文件不存在创建
			//判断目标文件所在的目录是否存在  
	        if(!create.getParentFile().exists()) {  
	            //如果目标文件所在的目录不存在，则创建父目录  
	            if(!create.getParentFile().mkdirs()) {
	                System.out.println("创建目标文件所在目录失败！");  
	            	pw.write("创建目标文件所在目录失败！");
	            }
	        }
			
			CommonsMultipartFile cf= (CommonsMultipartFile)file;
	        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
	        File f = fi.getStoreLocation();
			boolean upload =  ImageHelper.scaleImage(f, realPath, 0.8 , "jpg");
			if(!upload){
				pw.write("图片压缩失败");
			}
			
			pw.write(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error on deploy process, because of file input stream", e);
			pw.write("系统异常");
		}
	}
	
}
