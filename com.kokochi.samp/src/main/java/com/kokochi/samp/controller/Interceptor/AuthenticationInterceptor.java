package com.kokochi.samp.controller.Interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kokochi.samp.service.TwitchKeyService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kokochi.samp.DTO.UserDTO;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Resource(name = "twitchKeyService")
	TwitchKeyService twitchKeyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}
	
	// 컨트롤러가 수행되기 전에 수행되는 인터셉터
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal.toString().equals("anonymousUser")) {
			request.setAttribute("user_nickname", "null");

		} else {
			UserDTO user = (UserDTO) principal;
			String user_nickname = user.getUser_nickname();
			request.setAttribute("user_nickname", user_nickname);
		}
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	
}
