package com.kokochi.samp.controller;

import java.util.List;
import java.util.UUID;

import com.kokochi.samp.domain.UserTwitchVO;
import com.kokochi.samp.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.queryAPI.GetFollow;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.innerProcess.PostQuery;
import com.kokochi.samp.service.ManagedService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/menu")
@Slf4j
public class MenuController {
	
	@Autowired
	private TwitchKeyService key;

	@Autowired
	private UserDetailService userService;
	
	@Autowired
	private ManagedService follow_service;
	
	private GetStream streamGenerator = new GetStream();
	private GetFollow followGetter = new GetFollow();
	private PostQuery postQuery = new PostQuery();

	// /menu/setting GET :: 메뉴 설정 화면
	@RequestMapping(value="/setting", method = RequestMethod.GET)
	public String menuSetting(Model model) { // 메인 home 화면 매핑
		log.info("/menu/setting - 메뉴화면");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			model.addAttribute("setting_twich_user_login", user.getTwitch_user_login());
			model.addAttribute("setting_user_id", user.getUser_id());
			model.addAttribute("setting_user_email", user.getUser_email());
		}
		return "menu/setting";
	}

	// /menu/managefollow GET :: 팔로우 채널 관리 화면
	@RequestMapping(value="/managefollow", method = RequestMethod.GET)
	public String menuFollow(Model model) throws Exception { // 메인 home 화면 매핑
		log.info("/menu/managefollow - ManagaFollow Mapping");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			// 로그인한 사용자의 팔로우 목록 가져오기


			UserDTO user = (UserDTO) principal;
//			postQuery.initManagedFollow(user.getTwitch_user_id(), user.getUser_id());		// 팔로우 목록 초기화/동기화
			String app_access_token = key.read("App_Access_Token").getKeyValue();
			
/*			List<TwitchUser> follow_list =  followGetter.getFollowedList(twitckKey.getClientId(), app_access_token, "from_id="+user.getTwitch_user_id()+"&", "first=40&");
			// db에 팔로우하려는 사용자의 데이터가 없으면 추가해준다.
			if(follow_list == null) {
				app_access_token = new GetToken().requestAppAccessToken(twitckKey.getClientId(), twitckKey.getCleintSecret());
				key.modify(new TwitchKeyVO("App_Access_Token", app_access_token));
				follow_list =  followGetter.getFollowedList(twitckKey.getClientId(), app_access_token, "from_id="+user.getTwitch_user_id()+"&", "first=40&");
			}// 토큰이 무효라면, 토큰 재발급 후, 딱 한번만 재실행 되도록함. 실행해도 실패한 경우에는, 에러를 반환*/

			// 트위치 API가 아닌, DB에서 팔로우 목록을 가져온다.
			List<UserTwitchVO> userTwitchList = userService.readUserTwitchFollowList(user.getTwitch_user_id());

/*			for(int i=0;i<userTwitchList.size();i++) {
				ManagedFollowVO managedFollowVO = new ManagedFollowVO();
				managedFollowVO.setUserId(user.getUser_id());
				managedFollowVO.setToUser(follow_list.get(i).getId());
				follow_list.get(i).setManaged(follow_service.isManagedFollow(managedFollowVO));
				if(follow_list.get(i).isManaged()) {
					follow_list.add(0, follow_list.remove(i)); // 관리체크된 값들은 맨위로 올라오도록 리스트 위치를 조정해준다.
				}
			}*/
			
			model.addAttribute("follow_list", userTwitchList);
			
			return "menu/managefollow";
		} else {
			// 오류가 나면 메인화면으로 돌아간다.
			return "redirect:/";
		}
	}

	// /menu/clipShorts GET :: 트위치 클립 쇼츠
	@RequestMapping(value="/clipShorts", method = RequestMethod.GET)
	public void clipShorts() { // 메인 home 화면 매핑
		log.info("/menu/replayvideo - ReplayVideo Mappin");
	}
	
	// /menu/request/managedfollow/remove POST :: 팔로우 관리목록 추가
	@RequestMapping(value="/request/managedfollow/add", method = RequestMethod.POST)
	@ResponseBody
	public String addfollowed_request(@RequestBody String toUser) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 추가 " + toUser);
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;
//				log.info("/managedfollow/add - 로그인 객체 " + user);
				ManagedFollowVO managedFollowVO = new ManagedFollowVO();
				managedFollowVO.setUser_id(user.getId());
				managedFollowVO.setTo_user(toUser);
				if(follow_service.getManagedFollow(managedFollowVO) != null) return "error";		// 이미 관리목록에 들어있는 데이터라면 오류반환

				managedFollowVO.setId(UUID.randomUUID().toString());
//				log.info("/managedfollow/add - 세팅객체 " + managedFollowVO);
				follow_service.createFollow(managedFollowVO);
				return "success";
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "error";
	}

	// /menu/request/managedfollow/remove POST :: 팔로우 관리목록 제거
	@RequestMapping(value="/request/managedfollow/remove", method = RequestMethod.POST)
	@ResponseBody
	public String removefollowed_request(@RequestBody String managed_id) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 제거 " + managed_id);
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				ManagedFollowVO search = new ManagedFollowVO();
				search.setId(managed_id);
				ManagedFollowVO managedFollow = follow_service.getManagedFollow(search);
//				log.info("/managedfollow/add - managedFollow " + managedFollow);
//				log.info("/managedfollow/add - user " + user);

				if(user.getId().equals(managedFollow.getUser_id())) {
					follow_service.removeFollow(managed_id);
					return "success";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "error";
	}
}
