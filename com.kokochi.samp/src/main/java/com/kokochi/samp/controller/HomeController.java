package com.kokochi.samp.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.kokochi.samp.domain.*;
import com.kokochi.samp.mapper.UserTwitchMapper;
import com.kokochi.samp.queryAPI.GetToken;
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

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Stream;
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

	// / ํ ๋งคํ
	@RequestMapping(value="/")
	public String home(Model model) throws Exception { // 
		log.info("/ - ๋ฉ์ธ๊ฒฝ๋ก ์ด๋");
		return "homes";
	}

	// /home/request/getLiveVideo POST - ๋ผ์ด๋ธ ๋น๋์ค ๊ฐ์?ธ์ค๊ธฐ
	@RequestMapping(value="/home/request/getLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getLiveVideo(HttpServletRequest req) throws Exception {
		log.info("/home/request/getLiveVideo - ๋ผ์ด๋ธ ๋น๋์ค ๊ฐ์?ธ์ค๊ธฐ :: ");
		JSONArray res_arr = new JSONArray();
		try {
			// ์?์ธ๋ถ
			HttpSession session = req.getSession();
			String client_id = key.read("client_id").getKeyValue();
			String client_secret = key.read("client_secret").getKeyValue();
			String app_access_token = key.read("App_Access_Token").getKeyValue();
//			log.info("TEST :: getLiveVideo :: ํ?ํฐ ์?์ธ :: " + client_id +" " +client_secret +" " + app_access_token);


			// ๋ผ์ด๋ธ ์คํธ๋ฆฌ๋จธ ๋ฆฌ์คํธ ๊ฐ์?ธ์ค๊ธฐ
			List<Stream> headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
			if(headslide_list == null) {
				app_access_token = tokenGetter.requestAppAccessToken(client_id, client_secret);
				key.modify(new TwitchKeyVO("App_Access_Token", app_access_token));
				session.setAttribute("App_Access_Token", app_access_token);
				headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
			}// ํ?ํฐ์ด ๋ฌดํจ๋ผ๋ฉด, ํ?ํฐ ์ฌ๋ฐ๊ธ ํ, ๋ฑ ํ๋ฒ๋ง ์ฌ์คํ ๋๋๋กํจ. ์คํํด๋ ์คํจํ ๊ฒฝ์ฐ์๋, ์๋ฌ๋ฅผ ๋ฐํ


			// ๋ฆฌ์คํธ ์ธ๋ค์ผ ์ค์?ํ๊ธฐ
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

	// /home/request/getMyRecentVideo POST - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ต์? ๋ค์๋ณด๊ธฐ ๊ฐ์?ธ์ค๊ธฐ
	@RequestMapping(value="/home/request/getMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyRecentVideo - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ต์? ๋ค์๋ณด๊ธฐ ๊ฐ์?ธ์ค๊ธฐ " + body);
		try {
			JSONParser parser = new JSONParser();
			JSONArray res_arr = new JSONArray();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// ์ฌ์ฉ์์ ํ๋ก์ฐ ๊ด๋ฆฌ๋ชฉ๋ก์ ๊ฐ์?ธ์จ๋ค
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());

				// ํ๋ก์ฐ ๊ด๋ฆฌ๋ชฉ๋ก์ ํด๋นํ๋ ์ฌ์ฉ์์ ๋น๋์ค ๊ฐ์ ๊ฐ์?ธ์จ๋ค
/*				String client_id = keyTwitch.getClientId();
				for (ManagedFollowVO managedFollowVO : follow_list) {
					List<VideoTwitchVO> service_video = service_video = videoGetter.getRecentVideo(client_id, key.read("App_Access_Token").getKeyValue(),
							"?user_id="+managedFollowVO.getTo_user()+"&first=100");
					if(service_video != null) {
						for (VideoTwitchVO videoTwitchVO : service_video) {
//							System.out.println("TEST :: ๊ฐ์?ธ์จ ๋น๋์ค ๋ฐ์ดํฐ :: " + videoTwitchVO);
							VideoTwitchVO dbSearch = videoTwitchService.read(videoTwitchVO);
							if(dbSearch == null) { // ๋น๋์ค๊ฐ์ด DB์ ๋ค์ด์์ง ์๋ ๋์ ํธ์์น API์์ ๋ฐ์ดํฐ๋ฅผ ๊ณ์ ๊ฐ์?ธ์์ DB์ ์ถ๊ฐํ๋ค
								videoTwitchService.create(videoTwitchVO);
							} else break;
						}
					}
				}*/

				// DB์ถ๊ฐ๊ฐ ๋๋๋ฉด, DB์์ ์ต์?์์ผ๋ก ์กฐํํ๋ค.
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

	// /home/request/refreshMyRecentVideo POST - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ต์? ๋ค์๋ณด๊ธฐ ์๋ก๊ณ?์นจ
	@RequestMapping(value="/home/request/refreshMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/refreshMyRecentVideo - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ต์? ๋ค์๋ณด๊ธฐ ์๋ก๊ณ?์นจ " + body);
		JSONObject res = new JSONObject();
		try {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// ์ฌ์ฉ์์ ํ๋ก์ฐ ๊ด๋ฆฌ๋ชฉ๋ก์ ๊ฐ์?ธ์จ๋ค
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());
				String client_id = key.read("client_id").getKeyValue();
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
								// tav๊ฐ ๋ ์์ผ๋ฉด insert
								// dv๊ฐ ๋ ์์ผ๋ฉด dv๋ฅผ delete
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
					// ๋ค์๋ณด๊ธฐ ๋ฐ์ดํฐ ๊ฐ์?ธ์ค๊ธฐ
				}

				res.put("msg", "success");
			}
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return res.toString();
		}
	}

	// /home/request/getMyLiveVideo POST - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ๋ผ์ด๋ธ ๊ฐ์?ธ์ค๊ธฐ
	@RequestMapping(value="/home/request/getMyLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyLiveVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyLiveVideo - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ๋ผ์ด๋ธ ๊ฐ์?ธ์ค๊ธฐ " + body);
		try {
			JSONArray res_arr = new JSONArray();

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				// ์ธ์ฆ๊ฐ ์ด๊ธฐํ
				UserDTO user = (UserDTO) principal;
				String client_id = key.read("client_id").getKeyValue();

				// ๊ด๋ฆฌ๋ชฉ๋ก ๋ฆฌ์คํธ ๊ฐ์?ธ์ค๊ธฐ
				List<UserTwitchVO> managedList = userTwitchMapper.readManagedList(user.getId());

				for(UserTwitchVO utv : managedList) {
//					log.info("user :: " + client_id+" "+user.getOauth_token()+" "+follow_list.get(i).getTo_user());
					// ๊ฐ ํ๋ก์ฐ ๋ฆฌ์คํธ์ ๋ผ์ด๋ธ ๋ฐ์ดํฐ ๊ฐ์?ธ์ค๊ธฐ
					Stream s = streamGetter.getLiveStream(client_id, user.getOauth_token(), utv.getId(), "");

					if(s != null) {
//					log.info("service_getLive :: " + s.toString() +" " + i +" " + follow_list.size());
						// ๋ผ์ด๋ธ ์ค ์ด๋ผ๋ฉด ์ฌ์ฉ์์๊ฒ ๋ณด์ฌ์ฃผ๊ธฐ ์ํ ๋ฐ์ดํฐ ์ธํ

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


	// /home/request/getMyClipVideo POST - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ธ๊ธฐํด๋ฆฝ ๊ฐ์?ธ์ค๊ธฐ
	@RequestMapping(value="/home/request/getMyClipVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyClipVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyClipVideo - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ํด๋ฆฝ ๊ฐ์?ธ์ค๊ธฐ " + body);

		try {
			JSONArray res_arr = new JSONArray();
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				// ์ธ์ฆ๊ฐ ์ด๊ธฐํ
				UserDTO user = (UserDTO) principal;
				String client_id = key.read("client_id").getKeyValue();

				// ํ๋ก์ฐ ๋ฐ์ดํฐ ๊ฐ์?ธ์ค๊ธฐ
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());

				// DB์ถ๊ฐ๊ฐ ๋๋๋ฉด, DB์์ ์ต์?์์ผ๋ก ์กฐํํ๋ค.
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

	// /home/request/refreshMyClipVideo POST - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ธ๊ธฐํด๋ฆฝ ์๋ก๊ณ?์นจ
	@RequestMapping(value="/home/request/refreshMyClipVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String refreshMyClipVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/refreshMyRecentVideo - ๋์ ๊ด๋ฆฌ๋ชฉ๋ก ์ต์? ๋ค์๋ณด๊ธฐ ์๋ก๊ณ?์นจ " + body);
		JSONObject res = new JSONObject();
		try {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;

				// ์ฌ์ฉ์์ ํ๋ก์ฐ ๊ด๋ฆฌ๋ชฉ๋ก์ ๊ฐ์?ธ์จ๋ค
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getId());
				String client_id = key.read("client_id").getKeyValue();
				String app_access_token = key.read("App_Access_Token").getKeyValue();
				for (ManagedFollowVO managedFollowVO : follow_list) {
					String userId = managedFollowVO.getTo_user();
					List<ClipTwitchVO> clips = clipGetter.getClips(client_id, app_access_token, "broadcaster_id="+userId+"&first=100");
					if(clips != null) {
						List<ClipTwitchVO> addClips = new ArrayList<>();
						ClipTwitchVO findc = new ClipTwitchVO();
						findc.setBroadcaster_id(userId);
						findc.setPage(1000000);
						findc.setIndex(0);
						List<ClipTwitchVO> cos = clipTwitchService.readList(findc);
						HashSet<String> cosSet = new HashSet<>();
						for (ClipTwitchVO co : cos) {
							cosSet.add(co.getId());
						}
						for (ClipTwitchVO co : clips) {
							if(!cosSet.contains(co.getId())) addClips.add(co);
							else clipTwitchService.update(co);
						}
						if(addClips.size() > 0) clipTwitchService.createList(addClips);
					}
					// ํด๋ฆฝ ๋ฐ์ดํฐ ๊ฐ์?ธ์ค๊ธฐ
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
