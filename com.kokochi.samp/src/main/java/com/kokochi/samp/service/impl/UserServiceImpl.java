package com.kokochi.samp.service.impl;

import com.kokochi.samp.domain.*;
import com.kokochi.samp.mapper.TwitchKeyMapper;
import com.kokochi.samp.mapper.UserFollowMapper;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.mapper.UserTwitchMapper;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private TwitchKeyMapper twitchKeyMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private UserTwitchMapper userTwitchMapper;

    @Autowired
    private TwitchKeyMapper keyMapper;

    @Override
    public MemberVO readUser(String user_id) throws Exception {
        return mapper.readUser(user_id);
    }

    @Override
    public MemberVO readUserByTwitchId(String twitch_user_id) throws Exception {
        return mapper.readUserByTwitchId(twitch_user_id);
    }

    @Override
    public List<MemberAuthVO> readAuth(String user_id) throws Exception {
        return mapper.readAuthList(user_id);
    }

    @Override
    public void create(MemberVO memberVO) throws Exception {
        mapper.create(memberVO);
    }

    @Override
    public void delete(String user_id) throws Exception {
        mapper.delete(user_id);
    }

    @Override
    public void update(MemberVO memberVO) throws Exception {
        mapper.update(memberVO);
    }

    @Override
    public List<MemberVO> userList() throws Exception {
        return mapper.userList();
    }

    @Override
    public void addAuth(MemberAuthVO auth) throws Exception {
        mapper.addAuth(auth);
    }

    @Override
    public void delAuth(MemberAuthVO auth) throws Exception {
        mapper.delAuth(auth);
    }

    public void addUserFollow(UserFollowVO userFollowVO) throws Exception {
        UserTwitchVO read = new UserTwitchVO();
        read.setId(userFollowVO.getFrom_id());
        UserTwitchVO temp = readUserTwitch(read);

        String client_id = twitchKeyMapper.read("client_id").getKeyValue();
        String app_access_token = twitchKeyMapper.read("App_Access_Token").getKeyValue();

        // db??? ?????????????????? ???????????? ???????????? ????????? ???????????????.
        if(temp == null) {
            TwitchUser user = new GetStream().getUser(client_id, app_access_token , "id=" + userFollowVO.getFrom_id());
            if(user == null) {
                user = new TwitchUser();
                user.setId(userFollowVO.getFrom_id());
                user.setLogin(userFollowVO.getFrom_login());
                user.setDisplay_name(userFollowVO.getFrom_name());
            }// ????????? ????????????, ?????? ????????? ???, ??? ????????? ????????? ????????????. ???????????? ????????? ????????????, ????????? ??????
            UserTwitchVO userTwitchVO = user.toUserTwitchVO();
            addUserTwitch(userTwitchVO);

        }

        read.setId(userFollowVO.getTo_id());
        temp = readUserTwitch(read);
        // db??? ????????? ?????? ???????????? ????????? ???????????????.
        if(temp == null) {
            TwitchUser user = new GetStream().getUser(client_id, app_access_token, "id=" + userFollowVO.getTo_id());
            if(user == null) {
                user = new TwitchUser();
                user.setId(userFollowVO.getTo_id());
                user.setLogin(userFollowVO.getTo_login());
                user.setDisplay_name(userFollowVO.getTo_name());
            }// ????????? ????????????, ?????? ????????? ???, ??? ????????? ????????? ????????????. ???????????? ????????? ????????????, ????????? ??????
            UserTwitchVO userTwitchVO = user.toUserTwitchVO();
            addUserTwitch(userTwitchVO);
        }

        // ??????????????? ????????? ???????????? DB??? ????????????.
        userFollowVO.setId(UUID.randomUUID().toString());
        userFollowMapper.create(userFollowVO);
    }


    public void addUserFollowList(List<UserFollowVO> list) throws Exception {
        List<UserTwitchVO> utList = new ArrayList<>();
        List<UserFollowVO> ufList = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        List<UserTwitchVO> userList = readUserTwitchList(new UserTwitchVO());
        HashMap<String, UserTwitchVO> userMap = new HashMap<>();
        for (UserTwitchVO ut : userList) userMap.put(ut.getId(), ut);
        String app_access_token = twitchKeyMapper.read("App_Access_Token").getKeyValue();
        String client_id = twitchKeyMapper.read("client_id").getKeyValue();

        int cnt = 0;
        for (UserFollowVO userFollowVO : list) {
            cnt++;

            // db??? ?????????????????? ???????????? ???????????? ????????? ???????????????.
            if(!userMap.containsKey(userFollowVO.getFrom_id()) && !set.contains(userFollowVO.getFrom_id())) {
                TwitchUser user = new GetStream().getUser(client_id, app_access_token , "id=" + userFollowVO.getFrom_id());
                if(user == null) {
                    user = new TwitchUser();
                    user.setId(userFollowVO.getFrom_id());
                    user.setLogin(userFollowVO.getFrom_login());
                    user.setDisplay_name(userFollowVO.getFrom_name());
                }// ????????? ????????????, ?????? ????????? ???, ??? ????????? ????????? ????????????. ???????????? ????????? ????????????, ????????? ??????
                UserTwitchVO userTwitchVO = user.toUserTwitchVO();
                utList.add(userTwitchVO);
                set.add(user.getId());
            }

			/*read.setId(userFollowVO.getTo_id());
			temp = readUserTwitch(read);*/
            // db??? ????????? ?????? ???????????? ????????? ???????????????.
            if(!userMap.containsKey(userFollowVO.getTo_id()) && !set.contains(userFollowVO.getTo_id())) {
                TwitchUser user = new GetStream().getUser(client_id, app_access_token, "id=" + userFollowVO.getTo_id());
                if(user == null) {
                    user = new TwitchUser();
                    user.setId(userFollowVO.getTo_id());
                    user.setLogin(userFollowVO.getTo_login());
                    user.setDisplay_name(userFollowVO.getTo_name());
                }// ????????? ????????????, ?????? ????????? ???, ??? ????????? ????????? ????????????. ???????????? ????????? ????????????, ????????? ??????
                UserTwitchVO userTwitchVO = user.toUserTwitchVO();
                utList.add(userTwitchVO);
                set.add(user.getId());
            }

            // ??????????????? ????????? ???????????? DB??? ????????????.
            userFollowVO.setId(UUID.randomUUID().toString());
            ufList.add(userFollowVO);
        }
        if(utList.size() > 0) userTwitchMapper.createList(utList);
        if(ufList.size() > 0) userFollowMapper.createList(ufList);

    }

    @Transactional(rollbackFor = Exception.class)
    public void userRegister(MemberVO memberVO) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        memberVO.setUser_pwd(encoder.encode(memberVO.getUser_pwd()));
        mapper.create(memberVO);

        MemberAuthVO auth = new MemberAuthVO();
        auth.setId(UUID.randomUUID().toString());
        auth.setUser_id(memberVO.getId());
        auth.setAuthority("ROLE_MEMBER");
        addAuth(auth);
    }

    public void userDelete(String user_id) throws Exception {
        mapper.delete(user_id);
    }

    public void userUpdate(MemberVO memberVO) throws Exception {
        mapper.update(memberVO);
    }


    public TwitchKeyVO getKey(String key_name) throws Exception {
        return keyMapper.read(key_name);
    }


    public UserTwitchVO readUserTwitch(UserTwitchVO userTwitchVO) throws Exception {
        return userTwitchMapper.read(userTwitchVO);
    }

    public List<UserTwitchVO> readUserTwitchList(UserTwitchVO userTwitchVO) throws Exception {
        return userTwitchMapper.readList(userTwitchVO);
    }


    // ?????? ???????????? ????????? ?????? ????????????
    public List<UserTwitchVO> readUserTwitchFollowList(String from_id) throws Exception {
        return userTwitchMapper.readFollowList(from_id);
    }

    public void addUserTwitch(UserTwitchVO userTwitchVO) throws Exception {
        UserTwitchVO temp = readUserTwitch(userTwitchVO);
        if(temp == null) userTwitchMapper.create(userTwitchVO);
        else updateUserTwitch(userTwitchVO);
        // ?????? ????????? ????????? ?????? ????????????, ?????? ????????? ?????? ?????? ???????????????.
    }

    public void deleteUserTwitchById(String id) throws Exception {
        userTwitchMapper.deleteById(id);
    }

    public void deleteUserTwitchByLogin(String login) throws Exception {
        userTwitchMapper.deleteByLogin(login);
    }

    public void updateUserTwitch(UserTwitchVO userTwitchVO) throws Exception {
        userTwitchMapper.update(userTwitchVO);
    }


    public UserFollowVO readUserFollow(UserFollowVO userFollowVO) throws Exception {
        return userFollowMapper.read(userFollowVO);
    }

    public List<UserFollowVO> readUserFollowList(UserFollowVO userFollowVO) throws Exception {
        return userFollowMapper.readList(userFollowVO);
    }

    public void deleteUserFollow(String id) throws Exception {
        userFollowMapper.deleteById(id);
    }

    public void deleteUserFollowList(List<String> list) throws Exception {
        String ids = String.join(",",list);
        userFollowMapper.deleteById(ids);
    }
}
