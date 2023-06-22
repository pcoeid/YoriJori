package com.yorijori.foodcode.service;

import java.util.List;

import com.yorijori.foodcode.jpa.entity.UserInfo;

public interface MemberService {
	public UserInfo login(String user_id, String userPassword);
	public UserInfo loginKakao(String kakaoID);
	public boolean nicknamecheck(String nickName);   
	public boolean idcheck(String userId);   
	public void save(UserInfo userinfodto);
	long userCount(String role);
	List<UserInfo> selectListByPageAndSort(int pageNo, int pagePerCount, String sortType);
}