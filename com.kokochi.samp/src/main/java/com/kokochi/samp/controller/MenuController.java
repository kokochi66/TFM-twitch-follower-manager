package com.kokochi.samp.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import com.kokochi.samp.domain.ClipTwitchVO;
import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.domain.UserTwitchVO;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.security.UserDetailService;
import com.kokochi.samp.service.UserService;
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
	private UserService userService;
	
	@Autowired
	private ManagedService follow_service;

	@Autowired
	private ManagedService managedService;

	private final GetStream streamGetter = new GetStream();
	private final GetFollow followGetter = new GetFollow();
	private final GetClips clipGetter = new GetClips();
	private final GetVideo videoGetter = new GetVideo();

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
			String app_access_token = key.read("App_Access_Token").getKeyValue();

			// 트위치 API가 아닌, DB에서 팔로우 목록을 가져온다.
			List<UserTwitchVO> userTwitchList = userService.readUserTwitchFollowList(user.getTwitch_user_id());
			
			model.addAttribute("follow_list", userTwitchList);
			
			return "menu/managefollow";
		} else {
			// 오류가 나면 메인화면으로 돌아간다.
			return "redirect:/";
		}
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

	// /menu/clipShorts GET :: 트위치 클립 쇼츠
	@RequestMapping(value="/clipShorts", method = RequestMethod.GET)
	public void clipShorts() { // 메인 home 화면 매핑
		log.info("/menu/replayvideo - ReplayVideo Mappin");
	}

	// /menu/clipShorts/next POST :: 트위치 클립 쇼츠 영상 가져오기
	@RequestMapping(value="/request/clipShorts/get", method = RequestMethod.POST)
	@ResponseBody
	public String nextClipShorts(@RequestBody String toUser) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 추가 " + toUser);
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;
				String client_id = key.read("client_id").getKeyValue();
				String app_access_token = key.read("App_Access_Token").getKeyValue();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				Date nowDate = new Date();
				nowDate.setDate(nowDate.getDate() - 7);

/*				1. 이미 본 클립은 가져오지 않음 (본 클립, 보지않은 클립 flag를 추가 - 테이블 추가)
				2. 관리목록에 있는 스트리머 최우선 (점수가 높게 배정됨 -> 배수)
				3. 팔로우 스트리머 차선 ( 그다음 추가 점수 배정)
				4. 나머지 스트리머들 조회 (점수의 배수 없음)
				5. 빠른 넘기기를 위해 무조건 데이터베이스에 있는 클립 데이터만 사용함 (새로고침, API 쿼리과정 일체 거치지 않음)
				6. 한번 쿼리시에 영상 5개 추가, 일정 넘어갈때마다 추가로 5개가 계속 추가됨. (최초는 10개)
				7. 클립넘기기/스트리머넘기기 플래그를 추가로 가짐. 해당 클립과 스트리머는 더이상 쇼츠에 나타나지 않음
				* */

				UserFollowVO userFollowVO = new UserFollowVO();
				userFollowVO.setFrom_id(user.getTwitch_user_id());
				List<UserFollowVO> userFollowVOS = userService.readUserFollowList(userFollowVO);
				// 사용자의 팔로우 목록 가져오기


				List<ManagedFollowVO> managedFollowList = managedService.listFollow(user.getUser_id());
				Set<String> managedSet = new HashSet<>();
				for (ManagedFollowVO managedFollowVO : managedFollowList) {
					managedSet.add(managedFollowVO.getTo_user());
				}
				// 사용자의 관심 스트리머 목록 가져오기

				// 사용자의 이미 본 클립 목록 가져오기

				List<ClipTwitchVO> clips = new ArrayList<>();
				for (UserFollowVO followVO : userFollowVOS) {
					List<ClipTwitchVO> clipList = clipGetter.getClips(client_id, app_access_token, "broadcaster_id="+followVO.getTo_id()+"&first=100&started_at="+format.format(nowDate));
					for (ClipTwitchVO clipTwitchVO : clipList) {
						if(managedSet.contains(followVO.getTo_id())) {
							clipTwitchVO.setView_count((int)(clipTwitchVO.getView_count() * 1.5));
						}
						clips.add(clipTwitchVO);
					}
					// 최신 일주일 클립을 모두 합치기 (합치면서, 관심목록 스트리머라면, viewCount를 1.5배수, 이미 본 클립이라면 제외)
				}
				// 팔로우 목록에 따라 스트리머 최신 일주일 클립 가져오기

				Collections.sort(clips, (a,b) -> {
					return b.getView_count() - a.getView_count();
				});
				// viewCount/일자수^2 로 정렬하여 사용자에게 보여줌.

				return "success";
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "error";
	}

}
