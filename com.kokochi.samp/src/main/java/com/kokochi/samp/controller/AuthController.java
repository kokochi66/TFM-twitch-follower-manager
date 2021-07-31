package com.kokochi.samp.controller;

import java.util.Locale;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetToken;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.security.UserDetailService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/auth")
@Slf4j
public class AuthController {
	
	@Autowired
	TwitchKeyService key;
	
	@Autowired
	UserDetailService detail;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(Locale locale, Model model) { // 메인 home 화면 매핑
		log.info("/auth/login - Login Mapping :: Locale = "+ locale);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void loginPage(Model model) {
		log.info("login POST Mapping");
	}
	
	@RequestMapping(value="/register/gettoken", method=RequestMethod.GET)
	public String register(Model model) throws Exception {
		log.info("/auth/regitser GET - 회원가입 페이지로 이동");
		
		// 트위치 아이디를 인증하여 연동하는 계정을 생성하거나, 일반 아이디를 생성하는 두가지 선택지를 주어야함.
		// 우선 기본 디폴트로 트위치 아이디를 인증하여 연동하는 계정을 생성하는 것을 구현함.
		String uri = "https://id.twitch.tv/oauth2/authorize?";
		String client_id = "client_id="+key.read("client_id").getKey_value()+"&";
		String redirect_uri = "redirect_uri=http://localhost:8080/auth/login/oauth2/code/twitch&";
		String response_type = "response_type=code&";
		String scope = "scope=user:read:follows user:read:subscriptions user:read:email channel:manage:videos&";
		String state = "state=/auth/registerForm&";
		
		uri += client_id + redirect_uri + response_type + scope + state;
		
		// state값을 이용해서 반환 페이지를 확인해준다.
		return "redirect:" +uri;
	}

	
	@RequestMapping(value="/login/oauth2/code/twitch", method=RequestMethod.GET)
	public String OauthTwitch(RedirectAttributes rttr, String code, String scope, String state) throws Exception {
		log.info("/auth/login/oauth2/code/twitch - OAuth 토큰얻기");
		
		String client_id = key.read("client_id").getKey_value();
		String client_secret = key.read("client_secret").getKey_value();
		GetToken tokenGenerator = new GetToken();
		JSONObject oauthToken = tokenGenerator.GetOauth2AuthorizeToken(client_id, client_secret, code);
		
		String OAuth_token = "Bearer " +oauthToken.get("access_token");
		GetStream streamGenereator = new GetStream();
		TwitchUser user = streamGenereator.getUser(client_id, OAuth_token, "");
		
		// 회원가입 시 값에서 로그인 아이디 값을 그대로 전달해주면, form에서 임의로 값을 수정하여 디폴트값이 변형이 일어날 수 있기 때문에,
		// 토큰값을 전달해주어서 로그인값은 표시만 해주고, 실제 인증과 데이터베이스 등록은 토큰을 이용해서 적용하도록 해준다.
		rttr.addFlashAttribute("AuthenticatedUser", user.getLogin());
		rttr.addFlashAttribute("OAuthToken", OAuth_token);
		
		// 전달해주는 상태값을 곧 redirect 경로로 사용함.
		return "redirect:"+state;
	}
	
	@RequestMapping(value="/registerForm", method=RequestMethod.GET)
	public void registerForm(Model model) throws Exception {
		log.info("/auth/registerForm GET - 회원가입 폼");
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPro(Model model, Member member, String authtoken) throws Exception {
		log.info("/auth/register POST - 회원가입 처리 => " + member.toString() +" "+ authtoken);
		detail.userRegister(member);
		return "redirect:/auth/login";
	}
	

}
