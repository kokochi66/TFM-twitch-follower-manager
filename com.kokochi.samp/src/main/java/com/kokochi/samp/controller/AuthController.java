package com.kokochi.samp.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kokochi.samp.domain.MemberVO;
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
	private TwitchKeyService key;
	
	@Autowired
	private UserDetailService detail;
	
	private GetToken tokenGenerator = new GetToken();
	private GetStream streamGenereator = new GetStream();
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(Model model, String errMsg) { // 메인 home 화면 매핑
		log.info("/auth/login - Login Mapping");
		
		if(errMsg != null) {
			if(errMsg.equals("UsernameNotFound")) model.addAttribute("errMsg", "존재하지 않는 사용자입니다.");
			else if(errMsg.equals("BadCredential")) model.addAttribute("errMsg", "아이디나 비밀번호가 틀립니다.");
			else if(errMsg.equals("isLocked")) model.addAttribute("errMsg", "정지당한 아이디입니다.");
			else if(errMsg.equals("isDisabled")) model.addAttribute("errMsg", "사용이 불가능한 아이디 입니다.");
			else if(errMsg.equals("AccountExpired")) model.addAttribute("errMsg", "만료된 아이디 입니다.");
			else if(errMsg.equals("CredentialsExpired")) model.addAttribute("errMsg", "비밀번호가 만료되었습니다.");
		}

	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void loginPage(Model model) {
		log.info("login POST Mapping");
		
		
	}
	
	@RequestMapping(value="/login/gettoken", method=RequestMethod.GET)
	public String loginTwitch() throws Exception {
		
		// 트위치 아이디를 인증하여 연동하는 계정을 생성하거나, 일반 아이디를 생성하는 두가지 선택지를 주어야함.
		// 우선 기본 디폴트로 트위치 아이디를 인증하여 연동하는 계정을 생성하는 것을 구현함.
		String uri = "https://id.twitch.tv/oauth2/authorize?";
		String client_id = "client_id="+key.read("client_id").getKeyValue()+"&";
		String redirect_uri = "redirect_uri=http://localhost:8080/auth/login/oauth2/code/twitch&";
		String response_type = "response_type=code&";
		String scope = "scope=user:read:follows&";
		String state = "state=/auth/login/twitch";
		
		uri += client_id + redirect_uri + response_type + scope + state;
		
		// state값을 이용해서 반환 페이지를 확인해준다.
		return "redirect:" +uri;
	}
	
	@RequestMapping(value="/login/twitch", method=RequestMethod.GET)
	public String OAuthTokenProcess(Model model,String OAuthToken, String AuthenticatedUser) throws Exception {
		log.info("/login/twitch - 트위치 로그인");
		model.addAttribute("user_id", "OAuth2_authentication");
		model.addAttribute("user_pwd", OAuthToken);
		
		return "auth/hiddenLogin";
	}
	
	@RequestMapping(value="/register/gettoken", method=RequestMethod.GET)
	public String register() throws Exception {
		
		// 트위치 아이디를 인증하여 연동하는 계정을 생성하거나, 일반 아이디를 생성하는 두가지 선택지를 주어야함.
		// 우선 기본 디폴트로 트위치 아이디를 인증하여 연동하는 계정을 생성하는 것을 구현함.
		String uri = "https://id.twitch.tv/oauth2/authorize?";
		String client_id = "client_id="+key.read("client_id").getKeyValue()+"&";
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
		log.info("/login/oauth2/code/twitch - 트위치 토큰 받아오기 :: " + code +" "+ scope +" "+ state);
		
		String client_id = key.read("client_id").getKeyValue();
		String client_secret = key.read("client_secret").getKeyValue();
		JSONObject oauthToken = tokenGenerator.GetOauth2AuthorizeToken(client_id, client_secret, code);
		
		String OAuth_token = "Bearer " +oauthToken.get("access_token");
		TwitchUser user = streamGenereator.getUser(client_id, OAuth_token, "");
		if(user == null) return "redirect:/";
//		log.info("/login/oauth2/code/twitch :: " + user.toString());
		
		// 회원가입 시 값에서 로그인 아이디 값을 그대로 전달해주면, form에서 임의로 값을 수정하여 디폴트값이 변형이 일어날 수 있기 때문에,
		// 토큰값을 전달해주어서 로그인값은 표시만 해주고, 실제 인증과 데이터베이스 등록은 토큰을 이용해서 적용하도록 해준다.
		rttr.addAttribute("AuthenticatedUser", user.getLogin());
		rttr.addAttribute("OAuthToken", OAuth_token);
		
		// 전달해주는 상태값을 곧 redirect 경로로 사용함.
		return "redirect:"+state;
	}
	
	@RequestMapping(value="/registerForm", method=RequestMethod.GET)
	public void registerForm(Model model,String OAuthToken, String AuthenticatedUser) throws Exception {
		log.info("/auth/registerForm GET - 회원가입 폼");
		model.addAttribute("AuthenticatedUser", AuthenticatedUser);
		model.addAttribute("OAuthToken", OAuthToken);
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPro(Model model, MemberVO memberVO, String authtoken) throws Exception {
		log.info("/auth/register POST - 회원가입 처리");
		
		String client_id = key.read("client_id").getKeyValue();
		TwitchUser user = streamGenereator.getUser(client_id, authtoken, "");
		memberVO.setTwitch_user_id(user.getLogin());
		
		log.info("회원가입 사용자 - " + memberVO.toString());
		
		detail.userRegister(memberVO);
		return "redirect:/auth/login";
	}
	

}
