package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.kokochi.samp.domain.*;
import com.kokochi.samp.security.UserDetailService;
import com.kokochi.samp.service.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.converter.LanguageConverter;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetFollow;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Channel;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/detail")
@Slf4j
public class DetailController {
	
	@Autowired
	private TwitchKeyService key;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ManagedService followService;

	@Autowired
	private VideoTwitchService videoTwitchService;

	@Autowired
	private ClipTwitchService clipTwitchService;

	
	private final GetStream streamGetter = new GetStream();
	private final GetFollow followGetter = new GetFollow();
	private final GetClips clipGetter = new GetClips();
	private final GetVideo videoGetter = new GetVideo();

	// /detail GET - 스트리머 상세보기 페이지
	@RequestMapping(method=RequestMethod.GET)
	public String detail(Model model, @RequestParam("streams")String streams) throws Exception { // 메인 home 화면 매핑
		log.info("/detail - 스트리머 상세보기 페이지 :: " + streams);

		String client_id = key.read("client_id").getKeyValue();
		String app_access_token = key.read("app_access_token").getKeyValue();
		
		LanguageConverter langConverter = new LanguageConverter();
		
		TwitchUser stream = streamGetter.getUser(client_id, app_access_token, "id="+streams);
		int stream_total = followGetter.getFollowedTotal(client_id, app_access_token, "to_id="+streams);
		Channel stream_channel = streamGetter.getChannelInfo(client_id, app_access_token, "broadcaster_id="+streams);
		stream_channel.setBroadcaster_language(
				langConverter.LanguageConvert(stream_channel.getBroadcaster_language()));
		if(stream == null) return"detail/findError";
		
		model.addAttribute("stream", stream);
		model.addAttribute("stream_total", stream_total);
		model.addAttribute("stream_channel", stream_channel);
		return "detail/detail";
	}

	// /detail/request/live POST - 라이브 데이터 가져오기
	@RequestMapping(value="/request/live", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getLiveDataFromStream(@RequestBody String body) throws Exception {
		log.info("/detail/request/live - 라이브 데이터 가져오기 :: " + body);

		String client_id = key.read("client_id").getKeyValue();
		String app_access_token = key.read("App_Access_Token").getKeyValue();
		Stream stream = streamGetter.getLiveStream(client_id, app_access_token, body, "");
		if(stream == null) {
			JSONObject res = new JSONObject();
			res.put("title", "방송중이 아님");
			res.put("user_login", "");
			res.put("thumbnail_url", "/resources/assets/img/default_image.jpg");
			return res.toJSONString();
		}
		stream.setThumbnail_url(stream.getThumbnail_url().replace("{width}", "1920").replace("{height}", "1080"));
		
		JSONObject res = stream.StreamToJSON();
		return res.toJSONString();
	}

	// /detail/request/replay POST - 다시보기 데이터 가져오기
	@RequestMapping(value="/request/replay", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getReplayDataFromStream(@RequestBody String body) throws Exception {
		log.info("/detail/request/replay - 다시보기 데이터 가져오기 :: " + body);
		JSONParser parser = new JSONParser();
		if(body.equals("") || body == null) return "error";
		JSONObject body_json = (JSONObject) parser.parse(body);
		
		String user_id = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			user_id = user.getUser_id();
		}	// 로그인 상태라면, user_id가 로그인값으로 변경됨.

		VideoTwitchVO find = new VideoTwitchVO();
		find.setUser_id(body_json.get("login").toString());
		find.setIndex(Integer.parseInt(body_json.get("next").toString()) * find.getPage());
		List<VideoTwitchVO> voList = videoTwitchService.readList(find);

		JSONArray res_arr = new JSONArray();
		for (VideoTwitchVO twitchVO : voList) {
			twitchVO.setThumbnail_url(twitchVO.getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
			JSONObject j = twitchVO.parseToJSON();
			res_arr.add(j);
		}
		JSONObject next = new JSONObject();
		next.put("next", Integer.parseInt(body_json.get("next").toString())+1);
		res_arr.add(next);

		return res_arr.toJSONString();
	}
	
	// /detail/request/clips POST - 클립 데이터 가져오기
	@RequestMapping(value="/request/clips", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getClipsDataFromStream(@RequestBody String body) throws Exception {
		log.info("/detail/request/clips - 클립 데이터 가져오기 :: " + body);
		JSONParser parser = new JSONParser();
		if(body.equals("") || body == null) return "error";
		JSONObject body_json = (JSONObject) parser.parse(body);

		ClipTwitchVO find = new ClipTwitchVO();
		find.setBroadcaster_id(body_json.get("login").toString());
		find.setIndex(Integer.parseInt(body_json.get("next").toString()) * find.getPage());
		List<ClipTwitchVO> voList = clipTwitchService.readList(find);
		JSONArray res_arr = new JSONArray();
		for (ClipTwitchVO clipTwitchVO : voList) {
			clipTwitchVO.setThumbnail_url(clipTwitchVO.getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
			JSONObject j = clipTwitchVO.clipsToJSON();
			res_arr.add(j);
		}
		JSONObject next = new JSONObject();
		next.put("next", Integer.parseInt(body_json.get("next").toString())+1);
		res_arr.add(next);

		return res_arr.toJSONString();
	}

	// /detail/request/relative POST - 연관 스트리머 데이터 가져오기
	@RequestMapping(value="/request/relative", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getRelativeDataFromStream(@RequestBody String body) throws Exception {
		JSONArray res_arr = new JSONArray();
		try{
			log.info("/detail/request/relative - 연관 스트리머 데이터 가져오기 :: " + body);

			List<UserFollowVO> followVOList = userService.readUserFollowList(new UserFollowVO());
			HashMap<String, List<UserFollowVO>> followMap = new HashMap<>();
			for (UserFollowVO userFollowVO : followVOList) {
				if(!followMap.containsKey(userFollowVO.getFrom_id())) {
					List<UserFollowVO> l = new ArrayList<>();
					l.add(userFollowVO);
					followMap.put(userFollowVO.getFrom_id(), l);
				} else {
					followMap.get(userFollowVO.getFrom_id()).add(userFollowVO);
				}
			}

			List<UserTwitchVO> userList = userService.readUserTwitchList(new UserTwitchVO());
			HashMap<String, UserTwitchVO> userMap = new HashMap<>();
			for (UserTwitchVO ut : userList) userMap.put(ut.getId(), ut);

			List<UserFollowVO> sTof = followMap.get(body);
//			System.out.println("TEST :: / 팔로우 리스트 :: " + sTof.size());
			HashMap<String, Integer> map = new HashMap<>();
			if(sTof != null) {
				for (UserFollowVO f : sTof) {
					// 스트리머가 팔로우 한 대상 리스트 => +10점
					if (map.containsKey(f.getTo_id())) map.replace(f.getTo_id(), map.get(f.getTo_id()) + 10);
					else map.put(f.getTo_id(), 10);

					List<UserFollowVO> fTof = followMap.get(f.getTo_id());
					if (fTof != null) {
						for (UserFollowVO ff : fTof) {
							int addFlag = 0;
							// 팔로우한 대상이 스트리머를 팔로우 했으면 +8점
							if (ff.getTo_id().equals(body)) {
								map.replace(f.getTo_id(), map.get(f.getTo_id()) + 8);
								addFlag = 2;
							}

							// 팔로우한 대상이 팔로우 한 리스트 => +1점 (팔팔에게만)
							if (map.containsKey(ff.getTo_id())) map.replace(ff.getTo_id(), map.get(ff.getTo_id()) + 1 + addFlag);
							else map.put(ff.getTo_id(), 1);



/*							List<UserFollowVO> fTos = followMap.get(ff.getTo_id());
							if (fTos != null) {
								for (UserFollowVO fTo : fTos) {
									if (fTo.getTo_id().equals(body)) {
										map.replace(f.getTo_id(), map.get(f.getTo_id()) + 4);
										map.replace(ff.getTo_id(), map.get(ff.getTo_id()) + 4);
									}
								}
							}*/
							// 팔팔이 스트리머를 팔로우 했는지 확인 => +4점 (팔과 팔팔 함께 더함)
						}
					}
				}
			}

			ArrayList<String> tempList = new ArrayList<>();
			for (String s : map.keySet()) if(!s.equals(body)) tempList.add(s);
			Collections.sort(tempList, (a,b) -> {return map.get(b) - map.get(a);});
			for (String s : tempList) {
				UserTwitchVO userTwitchVO = userMap.get(s);
//				System.out.println("TEST :: / 연관 스트리머 데이터 :: " + userTwitchVO.getDisplay_name()+"  점수 ::"+ map.get(s));
				res_arr.add(userTwitchVO.parseToJSON());
			}
			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res_arr.toJSONString();
	}
	
	// /detail/request/refresh POST - 트위치 사용자 데이터 새로고침
	@RequestMapping(value="/request/refresh", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getTwitchUserDataRefresh(@RequestBody String userId) throws Exception {
		try {
			log.info("/detail/request/refresh - 트위치 사용자 데이터 가져오기 :: " + userId);
			JSONParser parser = new JSONParser();
			JSONArray res_arr = new JSONArray();

			String client_id = key.read("client_id").getKeyValue();
			String app_access_token = key.read("App_Access_Token").getKeyValue();
			int left, right;

			// 사용자 본인 데이터 가져오기
			TwitchUser user = new GetStream().getUser(client_id, app_access_token , "id=" + userId);
			UserTwitchVO userTwitchVO = user.toUserTwitchVO();
			userService.addUserTwitch(userTwitchVO);

			List<VideoTwitchVO> videos = videoGetter.getRecentVideo(client_id,app_access_token ,"?user_id="+userId+"&first=100");
			if(videos != null) {
				List<VideoTwitchVO> addVideos = new ArrayList<>();
				List<String> delVideos = new ArrayList<>();
				VideoTwitchVO findv = new VideoTwitchVO();
				findv.setUser_id(userId);
				findv.setPage(1000000000);
				findv.setIndex(0);
				List<VideoTwitchVO> vos = videoTwitchService.readList(findv);
				Collections.sort(videos,(a,b) -> {return a.getId().compareTo(b.getId());});
				Collections.sort(vos,(a,b) -> {return a.getId().compareTo(b.getId());});
				left = 0;
				right = 0;
				while(left < videos.size() && right < vos.size()) {
					VideoTwitchVO tav = videos.get(left);
					VideoTwitchVO dv = vos.get(right);

					if(!tav.getId().equals(dv.getId())) {
						// tav가 더 작으면 insert
						// dv가 더 작으면 dv를 delete
						if(tav.getId().compareTo(dv.getId()) < 0) {
							addVideos.add(tav);
							left++;
						} else {
							delVideos.add(dv.getId());
							right++;
						}
					} else {
						left++;
						right++;
					}
				}
				while(left < videos.size()) addVideos.add(videos.get(left++));
				while(right < vos.size()) delVideos.add(vos.get(right++).getId());
				if(addVideos.size() > 0) videoTwitchService.createList(addVideos);
				if(delVideos.size() > 0) videoTwitchService.deleteList(String.join(",",delVideos));
			}
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 다시보기 데이터 가져오기 완료");
			// 다시보기 데이터 가져오기


			List<ClipTwitchVO> clips = clipGetter.getClipsAll(client_id, app_access_token, "broadcaster_id="+userId+"&first=100");
			if(clips != null) {
				List<ClipTwitchVO> addClips = new ArrayList<>();
				List<String> delClips = new ArrayList<>();
				ClipTwitchVO findc = new ClipTwitchVO();
				findc.setBroadcaster_id(userId);
				findc.setPage(1000000000);
				findc.setIndex(0);
				List<ClipTwitchVO> cos = clipTwitchService.readList(findc);
				Collections.sort(clips,(a,b) -> {return a.getId().compareTo(b.getId());});
				Collections.sort(cos,(a,b) -> {return a.getId().compareTo(b.getId());});
				log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: clips :: " +clips.size());
				left = 0;
				right = 0;
				while(left < clips.size() && right < cos.size()) {
					ClipTwitchVO tac = clips.get(left);
					ClipTwitchVO dc = cos.get(right);
					if(!tac.getId().equals(dc.getId())) {
						// tav가 더 작으면 insert
						// dv가 더 작으면 dv를 delete
						if(tac.getId().compareTo(dc.getId()) < 0) {
							addClips.add(tac);
							left++;
						} else {
							delClips.add(dc.getId());
							right++;
						}
					} else {
						left++;
						right++;
						// db에 있는 값이라면 클립 데이터를 update
						clipTwitchService.update(tac);
					}
				}
				while(left < clips.size()) addClips.add(clips.get(left++));
				while(right < cos.size()) delClips.add(cos.get(right++).getId());
				if(addClips.size() > 0) clipTwitchService.createList(addClips);
				if(delClips.size() > 0) clipTwitchService.deleteList(String.join(",",delClips));
			}
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 클립 데이터 가져오기 완료");
			// 클립 데이터 가져오기




			ArrayList<UserFollowVO> fromFollows = followGetter.getAllFollowedList(client_id, app_access_token, "from_id=" + userId);
			if(fromFollows != null) {
				List<UserFollowVO> addFollows = new ArrayList<>();
				List<String> delFollows = new ArrayList<>();
				UserFollowVO findu = new UserFollowVO();
				findu.setFrom_id(userId);
				List<UserFollowVO> ffs = userService.readUserFollowList(findu);
				Collections.sort(fromFollows, (a,b) -> {return a.getFollowed_at().compareTo(b.getFollowed_at());});
				Collections.sort(ffs, (a,b) -> {return a.getFollowed_at().compareTo(b.getFollowed_at());});
				left = 0;
				right = 0;
				while(left < fromFollows.size() && right < ffs.size()) {
					UserFollowVO taf = fromFollows.get(left);
					UserFollowVO df = ffs.get(right);

					if(!taf.getTo_id().equals(df.getTo_id())) {
						// tav가 더 작으면 insert
						// dv가 더 작으면 dv를 delete
						if(taf.getFollowed_at().compareTo(df.getFollowed_at()) <= 0) {
//						userService.addUserFollow(taf);
							addFollows.add(taf);
							left++;
						} else {
//						userService.deleteUserFollow(df.getId());
							delFollows.add(df.getId());
							right++;
						}
					} else {
						left++;
						right++;
					}
				}
				while(left < fromFollows.size()) addFollows.add(fromFollows.get(left++));
				while(right < ffs.size()) delFollows.add(ffs.get(right++).getId());

				int cnt = 0;
				if(addFollows.size() > 0) userService.addUserFollowList(addFollows);
				if(delFollows.size() > 0) userService.deleteUserFollowList(delFollows);
			}
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 팔로우 데이터 가져오기");
			// 팔로우 데이터 가져오기

			System.out.println("TEST :: 가져온 유저 정보 :: " + user);
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 데이터 새로고침 완료");
			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


/*	// /detail/request/refresh POST - 트위치 사용자 데이터 새로고침 - 각각의 개체수 따로 INSERT, DELETE
	@RequestMapping(value="/request/refresh", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getTwitchUserDataRefresh(@RequestBody String userId) throws Exception {
		try {
			log.info("/detail/request/refresh - 트위치 사용자 데이터 가져오기 :: " + userId);
			JSONParser parser = new JSONParser();
			JSONArray res_arr = new JSONArray();

			String client_id = key.read("client_id").getKeyValue();
			String app_access_token = key.read("App_Access_Token").getKeyValue();
			int left, right;

			List<VideoTwitchVO> videos = videoGetter.getRecentVideo(client_id,app_access_token ,"?user_id="+userId+"&first=100");
			VideoTwitchVO findv = new VideoTwitchVO();
			findv.setUser_id(userId);
			findv.setPage(1000000000);
			findv.setIndex(0);
			List<VideoTwitchVO> vos = videoTwitchService.readList(findv);
			Collections.sort(videos,(a,b) -> {return b.getCreated_at().compareTo(a.getCreated_at());});
			Collections.sort(vos,(a,b) -> {return b.getCreated_at().compareTo(a.getCreated_at());});
			left = 0;
			right = 0;
			while(left < videos.size() && right < vos.size()) {
				VideoTwitchVO tav = videos.get(left);
				VideoTwitchVO dv = vos.get(right);

				if(!tav.getId().equals(dv.getId())) {
					// tav가 더 작으면 insert
					// dv가 더 작으면 dv를 delete
					if(tav.getCreated_at().compareTo(dv.getCreated_at()) <= 0) {
						videoTwitchService.create(tav);
						left++;
					} else {
						videoTwitchService.deleteById(dv.getId());
						right++;
					}
				} else {
					left++;
					right++;
				}
			}
			while(left < videos.size()) videoTwitchService.create(videos.get(left++));
			while(right < vos.size()) videoTwitchService.deleteById(vos.get(right++).getId());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 다시보기 데이터 가져오기 완료");
			// 다시보기 데이터 가져오기


			List<ClipTwitchVO> clips = clipGetter.getClipsAll(client_id, app_access_token, "broadcaster_id="+userId+"&first=100");
			ClipTwitchVO findc = new ClipTwitchVO();
			findc.setBroadcaster_id(userId);
			findc.setPage(1000000000);
			findc.setIndex(0);
			List<ClipTwitchVO> cos = clipTwitchService.readList(findc);
			Collections.sort(clips,(a,b) -> {return b.getCreated_at().compareTo(a.getCreated_at());});
			Collections.sort(cos,(a,b) -> {return b.getCreated_at().compareTo(a.getCreated_at());});
			left = 0;
			right = 0;
			while(left < clips.size() && right < cos.size()) {
				ClipTwitchVO tac = clips.get(left);
				ClipTwitchVO dc = cos.get(right);

				if(!tac.getId().equals(dc.getId())) {
					// tav가 더 작으면 insert
					// dv가 더 작으면 dv를 delete
					if(tac.getCreated_at().compareTo(dc.getCreated_at()) <= 0) {
						clipTwitchService.create(tac);
						left++;
					} else {
						clipTwitchService.deleteById(dc.getId());
						right++;
					}
				} else {
					left++;
					right++;
				}
			}
			while(left < clips.size()) clipTwitchService.create(clips.get(left++));
			while(right < cos.size()) clipTwitchService.deleteById(cos.get(right++).getId());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 클립 데이터 가져오기 완료");
			// 클립 데이터 가져오기


			ArrayList<UserFollowVO> fromFollows = followGetter.getAllFollowedList(client_id, app_access_token, "from_id=" + userId);
			UserFollowVO findu = new UserFollowVO();
			findu.setFrom_id(userId);
			List<UserFollowVO> ffs = userService.readUserFollowList(findu);
			Collections.sort(fromFollows, (a,b) -> {return b.getFollowed_at().compareTo(a.getFollowed_at());});
			Collections.sort(ffs, (a,b) -> {return b.getFollowed_at().compareTo(a.getFollowed_at());});
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: fromFollows :: " +fromFollows.size());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: ffs :: " +ffs.size());
			left = 0;
			right = 0;
			while(left < fromFollows.size() && right < ffs.size()) {
				UserFollowVO taf = fromFollows.get(left);
				UserFollowVO df = ffs.get(right);
				System.out.println("TEST :: taf :: " + taf+" "+left);
				System.out.println("TEST :: df :: " + df+" "+right);

				if(!taf.getTo_id().equals(df.getTo_id())) {
					// tav가 더 작으면 insert
					// dv가 더 작으면 dv를 delete
					if(taf.getFollowed_at().compareTo(df.getFollowed_at()) <= 0) {
						userService.addUserFollow(taf);
						left++;
					} else {
						userService.deleteUserFollow(df.getId());
						right++;
					}
				} else {
					left++;
					right++;
				}
			}
			while(left < fromFollows.size()) userService.addUserFollow(fromFollows.get(left++));
			while(right < ffs.size()) userService.deleteUserFollow(ffs.get(right++).getId());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 팔로우 데이터 가져오기");
			// 팔로우 데이터 가져오기

*//*			ArrayList<UserFollowVO> fromFollows;
			List<ManagedFollowVO> managedFollowVOS;
			UserFollowVO findu;
			List<UserFollowVO> ffs;

			fromFollows = followGetter.getAllFollowedList(client_id, app_access_token, "to_id=" + userId);
			findu = new UserFollowVO();
			findu.setTo_id(userId);
			ffs = userService.readUserFollowList(findu);
			Collections.sort(fromFollows, (a,b) -> {return b.getFollowed_at().compareTo(a.getFollowed_at());});
			Collections.sort(ffs, (a,b) -> {return b.getFollowed_at().compareTo(a.getFollowed_at());});
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: fromFollows :: " +fromFollows.size());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: ffs :: " +ffs.size());
			left = 0;
			right = 0;
			while(left < fromFollows.size() && right < ffs.size()) {
				UserFollowVO taf = fromFollows.get(left);
				UserFollowVO df = ffs.get(right);

				if(!taf.getFrom_id().equals(df.getFrom_id())) {
					// tav가 더 작으면 insert
					// dv가 더 작으면 dv를 delete
					if(taf.getFollowed_at().compareTo(df.getFollowed_at()) <= 0) {
						userService.addUserFollow(taf);
						left++;
					} else {
						userService.deleteUserFollow(df.getId());
						right++;
					}
				} else {
					left++;
					right++;
				}
			}
			while(left < fromFollows.size()) userService.addUserFollow(fromFollows.get(left++));
			while(right < ffs.size()) userService.deleteUserFollow(ffs.get(right++).getId());
			log.info("/detail /request/refresh :: getTwitchUserDataRefresh :: 스트리머를 팔로우한 사용자 데이터 가져오기");
			// 스트리머를 팔로우한 사용자 데이터 가져오기*//*

			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}*/
}
