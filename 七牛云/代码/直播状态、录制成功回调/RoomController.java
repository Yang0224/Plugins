package com.whcd.app.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whcd.app.service.IRoomService;

@Controller
@RequestMapping(value = "/appservice/room")
@Slf4j
public class RoomController {
	
	@Autowired
	@Qualifier("roomService")
	private IRoomService roomService;
	
	/**
	 * 直播回调
	 * @author yangshaoping 2017年6月30日 下午5:32:19
	 * @param request
	 * @param response
	 */
	@RequestMapping("/liveStatusCallback")
	@ResponseBody
	public void liveStatusCallback(HttpServletRequest request,HttpServletResponse response) {
		
		log.info("liveStatusCallback =========================>> begin");
		try {
			
			boolean b = roomService.liveStatusCallback(request.getInputStream());
			if(b){
				response.setStatus(200);
			}else{
				response.setStatus(400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception : " + e);
		}
	}
	
	/**
	 * 直播录像回调
	 * @author yangshaoping 2017年7月21日 下午5:13:44
	 * @param request
	 * @param response
	 */
	@RequestMapping("/videoCallback")
	@ResponseBody
	public void videoCallback(HttpServletRequest request,HttpServletResponse response) {
		
		log.info("videoCallback =========================>> begin");
		try {
			
			boolean b = roomService.videoCallback(request.getInputStream());
			if(b){
				response.setStatus(200);
			}else{
				response.setStatus(400);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception : " + e);
		}
	}
}
