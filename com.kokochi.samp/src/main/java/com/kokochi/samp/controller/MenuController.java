package com.kokochi.samp.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.kokochi.samp.domain.*;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.security.UserDetailService;
import com.kokochi.samp.service.ClipTwitchClipShortsBanService;
import com.kokochi.samp.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kokochi.samp.DTO.UserDTO;
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

	@Autowired
	private ClipTwitchClipShortsBanService clipTwitchClipShortsBanService;

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

	// /menu/clipShorts/get POST :: 트위치 클립 쇼츠 영상 가져오기
	@RequestMapping(value="/request/clipShorts/get", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String nextClipShorts(@RequestBody String toUser) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 추가 " + toUser);
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;
				String client_id = key.read("client_id").getKeyValue();
				String app_access_token = key.read("App_Access_Token").getKeyValue();

				LocalDateTime nowDate = LocalDateTime.now();
				nowDate = nowDate.minusDays(7);
				String nowDateStr = nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

				UserFollowVO userFollowVO = new UserFollowVO();
				userFollowVO.setFrom_id(user.getTwitch_user_id());
				List<UserFollowVO> userFollowVOS = userService.readUserFollowList(userFollowVO);
				// 사용자의 팔로우 목록 가져오기


				List<ManagedFollowVO> managedFollowList = managedService.listFollow(user.getId());
				Set<String> managedSet = new HashSet<>();
				for (ManagedFollowVO managedFollowVO : managedFollowList) {
					managedSet.add(managedFollowVO.getTo_user());
				}
				// 사용자의 관심 스트리머 목록 가져오기

				ClipTwitchShortsBanVO findClipBanVO = new ClipTwitchShortsBanVO();
				findClipBanVO.setUser_id(user.getId());
				List<ClipTwitchShortsBanVO> clipTwitchShortsBanVOS = clipTwitchClipShortsBanService.readClipTwitchShortsBanList(findClipBanVO);
				Set<String> clipBanSet = new HashSet<>();
				for (ClipTwitchShortsBanVO clipTwitchShortsBanVO : clipTwitchShortsBanVOS) {
					clipBanSet.add(clipTwitchShortsBanVO.getBan_clip());
				}
				// 사용자의 이미 본 클립 목록 가져오기

				List<ClipTwitchVO> clipList = new ArrayList<>();
				List<ClipTwitchVO> resClipList = new ArrayList<>();
				if(userFollowVOS != null) {
					List<String> streamList = new ArrayList<>();
					for (UserFollowVO followVO : userFollowVOS) {
						streamList.add(followVO.getTo_id());
					}

					clipList = clipGetter.getClipsStreams(client_id, app_access_token, streamList, "first=100&started_at=" + nowDateStr);
					if(clipList != null) {
						for (ClipTwitchVO clipTwitchVO : clipList) {
//							log.info("clipTwitchVO :: " + clipTwitchVO);
							if (managedSet.contains(clipTwitchVO.getBroadcaster_id())) {
								clipTwitchVO.setView_count((int) (clipTwitchVO.getView_count() * 3));
							}
							if(!clipBanSet.contains(clipTwitchVO.getId())) resClipList.add(clipTwitchVO);
						}
					}
				}
				// 팔로우 목록에 따라 스트리머 최신 일주일 클립 가져오기
				Collections.sort(resClipList, (a,b) -> {
					long aBet = ChronoUnit.DAYS.between(a.getCreated_at(), LocalDateTime.now());
					aBet *= aBet;
					aBet += 1;
					long bBet = ChronoUnit.DAYS.between(b.getCreated_at(), LocalDateTime.now());
					bBet *= bBet;
					bBet += 1;
					return (int)((b.getView_count()/bBet) - (a.getView_count()/aBet));
				});
				// viewCount/일자수^2 로 정렬하여 사용자에게 보여줌.

				JSONArray resArr = new JSONArray();
				for (ClipTwitchVO clip : resClipList) {
					JSONObject jsonObject = clip.clipsToJSON();
					resArr.add(jsonObject);
				}
				return resArr.toJSONString();
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "error";
	}

	// /request/clipShorts/ban POST :: 트위치 클립 쇼츠 이미 본 영상 체크하기
	@RequestMapping(value="/request/clipShorts/ban", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String banClipShorts(@RequestBody String clipId) throws Exception {
		log.info("/request/clipShorts/ban - 클립쇼츠 밴 " + clipId);
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				ClipTwitchShortsBanVO clipTwitchShortsBanVO = new ClipTwitchShortsBanVO();
				clipTwitchShortsBanVO.setUser_id(user.getId());
				clipTwitchShortsBanVO.setBan_clip(clipId);
				clipTwitchClipShortsBanService.createClipTwitchShortsBan(clipTwitchShortsBanVO);
				return "success";
			}
		} catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "error";
	}

}
