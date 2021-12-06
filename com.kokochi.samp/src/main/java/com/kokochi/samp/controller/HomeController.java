package com.kokochi.samp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kokochi.samp.DTO.Key;
import com.kokochi.samp.domain.*;
import com.kokochi.samp.mapper.UserTwitchMapper;
import com.kokochi.samp.queryAPI.GetToken;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.service.ClipTwitchService;
import com.kokochi.samp.service.VideoTwitchService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Clips;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.Video;
import com.kokochi.samp.service.ManagedService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	private TwitchKeyService key;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserTwitchMapper userTwitchMapper;

	@Autowired
	private VideoTwitchService videoTwitchService;

	@Autowired
	private ClipTwitchService clipTwitchService;
	
	@Autowired
	private ManagedService managed_service;
	
	private GetStream streamGetter = new GetStream();
	private GetVideo videoGetter = new GetVideo();
	private GetClips clipGetter = new GetClips();
	private GetToken tokenGetter = new GetToken();
	private Key twitchKey = new Key();		// 키값이 저장된 객체

	// / 홈 매핑
	@RequestMapping(value="/")
	public String home(Model model) throws Exception { // 
		log.info("/ - 메인경로 이동");
		return "homes";
	}

	// /home/request/getLiveVideo POST - 라이브 비디오 가져오기
	@RequestMapping(value="/home/request/getLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getLiveVideo(HttpServletRequest req) throws Exception {
		log.info("/home/request/getLiveVideo - 라이브 비디오 가져오기 :: ");
		JSONArray res_arr = new JSONArray();
		try {
			// 선언부
			HttpSession session = req.getSession();
			String client_id = twitchKey.getClientId();
			String client_secret = twitchKey.getCleintSecret();
			String app_access_token = key.read("App_Access_Token").getKeyValue();
//			log.info("TEST :: getLiveVideo :: 토큰 선언 :: " + client_id +" " +client_secret +" " + app_access_token);


			// 라이브 스트리머 리스트 가져오기
			List<Stream> headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
			if(headslide_list == null) {
				app_access_token = tokenGetter.requestAppAccessToken(client_id, client_secret);
				key.modify(new TwitchKeyVO("App_Access_Token", app_access_token));
				session.setAttribute("App_Access_Token", app_access_token);
				headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
			}// 토큰이 무효라면, 토큰 재발급 후, 딱 한번만 재실행 되도록함. 실행해도 실패한 경우에는, 에러를 반환


			// 리스트 썸네일 설정하기
			for(int i=0;i<headslide_list.size();i++) {
//			log.info("TEST :: headslide_list :: " + headslide_list.get(i).toString());
				headslide_list.get(i).setThumbnail_url(headslide_list.get(i).getThumbnail_url().replace("{width}", "400").replace("{height}", "250"));
				JSONObject res_ob = headslide_list.get(i).StreamToJSON();
				res_arr.add(res_ob);
			}
			if(res_arr.size() <= 0) return null;
//			log.info("TEST :: headslide_list :: " + res_arr.toJSONString());
			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	// /home/request/getMyRecentVideo POST - 나의 관리목록 최신 다시보기 가져오기
	@RequestMapping(value="/home/request/getMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyRecentVideo - 나의 관리목록 최신 다시보기 가져오기 " + body);
		try {
			JSONParser parser = new JSONParser();
			JSONArray res_arr = new JSONArray();
			Key keyTwitch = new Key();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// 사용자의 팔로우 관리목록을 가져온다
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());

				// 팔로우 관리목록에 해당하는 사용자의 비디오 값을 가져온다
/*				String client_id = keyTwitch.getClientId();
				for (ManagedFollowVO managedFollowVO : follow_list) {
					List<VideoTwitchVO> service_video = service_video = videoGetter.getRecentVideo(client_id, key.read("App_Access_Token").getKeyValue(),
							"?user_id="+managedFollowVO.getTo_user()+"&first=100");
					if(service_video != null) {
						for (VideoTwitchVO videoTwitchVO : service_video) {
//							System.out.println("TEST :: 가져온 비디오 데이터 :: " + videoTwitchVO);
							VideoTwitchVO dbSearch = videoTwitchService.read(videoTwitchVO);
							if(dbSearch == null) { // 비디오값이 DB에 들어있지 않는 동안 트위치 API에서 데이터를 계속 가져와서 DB에 추가한다
								videoTwitchService.create(videoTwitchVO);
							} else break;
						}
					}
				}*/

				// DB추가가 끝나면, DB에서 최신순으로 조회한다.
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				VideoTwitchVO searchVTO = new VideoTwitchVO();
				searchVTO.setUser_id(user.getId());
				searchVTO.setLimit(12);
				if(!body.equals("none")) searchVTO.setCreated_at(format.parse(body));
				List<VideoTwitchVO> videoTwitchVOS = videoTwitchService.readRecentFollowList(searchVTO);
				for (VideoTwitchVO videoTwitchVO : videoTwitchVOS) {
					if(videoTwitchVO != null)  {
						videoTwitchVO.setThumbnail_url(videoTwitchVO.getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
						videoTwitchVO.setPoints(videoTwitchVO.getCreated_at());
						JSONObject res_ob = videoTwitchVO.parseToJSON();
						res_arr.add(res_ob);
					}
				}
//			log.info("TEST :: getMyRecentVideo :: " + service_video.size());
				if(res_arr.size() <= 0) return null;
			}
//		log.info("TEST :: getMyRecentVideo :: " + res_arr.toJSONString());
			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	// /home/request/refreshMyRecentVideo POST - 나의 관리목록 최신 다시보기 새로고침
	@RequestMapping(value="/home/request/refreshMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/refreshMyRecentVideo - 나의 관리목록 최신 다시보기 새로고침 " + body);
		JSONObject res = new JSONObject();
		try {
			Key keyTwitch = new Key();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// 사용자의 팔로우 관리목록을 가져온다
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());
				String client_id = keyTwitch.getClientId();
				String app_access_token = key.read("App_Access_Token").getKeyValue();
				for (ManagedFollowVO managedFollowVO : follow_list) {

					String userId = managedFollowVO.getTo_user();
					List<VideoTwitchVO> videos = videoGetter.getRecentVideo(client_id,app_access_token ,"?user_id="+userId+"&first=100");
					if(videos != null) {
						List<VideoTwitchVO> addVideos = new ArrayList<>();
						List<String> delVideos = new ArrayList<>();
						VideoTwitchVO findv = new VideoTwitchVO();
						findv.setUser_id(userId);
						findv.setPage(1000000000);
						findv.setIndex(0);
						List<VideoTwitchVO> vos = videoTwitchService.readList(findv);
						Collections.sort(videos,(a, b) -> {return a.getId().compareTo(b.getId());});
						Collections.sort(vos,(a,b) -> {return a.getId().compareTo(b.getId());});
						int left = 0;
						int right = 0;
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
					// 다시보기 데이터 가져오기
				}

				res.put("msg", "success");
			}
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return res.toString();
		}
	}

	// /home/request/getMyLiveVideo POST - 나의 관리목록 라이브 가져오기
	@RequestMapping(value="/home/request/getMyLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyLiveVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyLiveVideo - 나의 관리목록 라이브 가져오기 " + body);
		try {
			JSONArray res_arr = new JSONArray();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				// 인증값 초기화
				UserDTO user = (UserDTO) principal;
				String client_id = twitchKey.getClientId();

				// 관리목록 리스트 가져오기
				List<UserTwitchVO> managedList = userTwitchMapper.readManagedList(user.getId());

				for(UserTwitchVO utv : managedList) {
//					log.info("user :: " + client_id+" "+user.getOauth_token()+" "+follow_list.get(i).getTo_user());
					// 각 팔로우 리스트의 라이브 데이터 가져오기
					Stream s = streamGetter.getLiveStream(client_id, user.getOauth_token(), utv.getId(), "");

					if(s != null) {
//					log.info("service_getLive :: " + s.toString() +" " + i +" " + follow_list.size());
						// 라이브 중 이라면 사용자에게 보여주기 위한 데이터 세팅

						s.setThumbnail_url(s.getThumbnail_url().replace("{width}", "300").replace("{height}", "200"));
						s.setProfile_image_url(utv.getProfile_image_url());
						JSONObject res_ob = s.StreamToVideo().parseToJSONObject();
						res_arr.add(res_ob);
					}
				}


//			log.info("video format :: " + res_arr.toJSONString());
				if(res_arr.size() <= 0) return null;
			}
			return res_arr.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	// /home/request/refreshMyRecentVideo POST - 나의 관리목록 최신 다시보기 새로고침
	@RequestMapping(value="/home/request/refreshMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/refreshMyRecentVideo - 나의 관리목록 최신 다시보기 새로고침 " + body);
		JSONObject res = new JSONObject();
		try {
			Key keyTwitch = new Key();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// 사용자의 팔로우 관리목록을 가져온다
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());
				String client_id = keyTwitch.getClientId();
				String app_access_token = key.read("App_Access_Token").getKeyValue();
				for (ManagedFollowVO managedFollowVO : follow_list) {

					String userId = managedFollowVO.getTo_user();
					List<VideoTwitchVO> videos = videoGetter.getRecentVideo(client_id,app_access_token ,"?user_id="+userId+"&first=100");
					if(videos != null) {
						List<VideoTwitchVO> addVideos = new ArrayList<>();
						List<String> delVideos = new ArrayList<>();
						VideoTwitchVO findv = new VideoTwitchVO();
						findv.setUser_id(userId);
						findv.setPage(1000000000);
						findv.setIndex(0);
						List<VideoTwitchVO> vos = videoTwitchService.readList(findv);
						Collections.sort(videos,(a, b) -> {return a.getId().compareTo(b.getId());});
						Collections.sort(vos,(a,b) -> {return a.getId().compareTo(b.getId());});
						int left = 0;
						int right = 0;
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
					// 다시보기 데이터 가져오기
				}

				res.put("msg", "success");
			}
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return res.toString();
		}
	}

	// /home/request/getMyClipVideo POST - 나의 관리목록 인기클립 가져오기
	@RequestMapping(value="/home/request/getMyClipVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyClipVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyClipVideo - 나의 관리목록 클립 가져오기 " + body);

		try {
			JSONArray res_arr = new JSONArray();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				// 인증값 초기화
				Key keyTwitch = new Key();
				UserDTO user = (UserDTO) principal;
				String client_id = keyTwitch.getClientId();

				// 팔로우 데이터 가져오기
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());

				// DB추가가 끝나면, DB에서 최신순으로 조회한다.
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				ClipTwitchVO searchVTO = new ClipTwitchVO();
				searchVTO.setUser_id(user.getId());
				searchVTO.setLimit(12);
				if(!body.equals("none")) searchVTO.setPoints(Long.parseLong(body));
				List<ClipTwitchVO> clip_list = clipTwitchService.readRecentFollowList(searchVTO);
				for (ClipTwitchVO clipTwitchVO : clip_list) {
					if(clipTwitchVO != null)  {
						clipTwitchVO.setThumbnail_url(clipTwitchVO.getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
						JSONObject res_ob = clipTwitchVO.clipsToJSON();
						res_arr.add(res_ob);
					}
				}
				if(res_arr.size() <= 0) return null;
//				log.info(res_arr.toJSONString());
			}
			return res_arr.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	// /home/request/refreshMyClipVideo POST - 나의 관리목록 인기클립 새로고침
	@RequestMapping(value="/home/request/refreshMyClipVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshMyClipVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/refreshMyRecentVideo - 나의 관리목록 최신 다시보기 새로고침 " + body);
		JSONObject res = new JSONObject();
		try {
			Key keyTwitch = new Key();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// 사용자의 팔로우 관리목록을 가져온다
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());
				String client_id = keyTwitch.getClientId();
				String app_access_token = key.read("App_Access_Token").getKeyValue();
				for (ManagedFollowVO managedFollowVO : follow_list) {

					String userId = managedFollowVO.getTo_user();
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
						int left = 0;
						int right = 0;
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
							}
						}
						while(left < clips.size()) addClips.add(clips.get(left++));
						while(right < cos.size()) delClips.add(cos.get(right++).getId());
						if(addClips.size() > 0) clipTwitchService.createList(addClips);
						if(delClips.size() > 0) clipTwitchService.deleteList(String.join(",",delClips));
					}
					// 클립 데이터 가져오기
				}

				res.put("msg", "success");
			}
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return res.toString();
		}
	}

}
