package com.kokochi.samp.queryAPI.domain;

public class Stream {
	
	private String id;
	private String user_id;
	private String user_login;
	private String user_name;
	private String game_id;
	private String game_name;
	private String type;
	private String title;
	private int viewer_count;
	private String started_at;
	private String language;
	private String thumbnail_url;
	private String tag_ids;
	private boolean is_mature;
	private String pagination;
	
	
	
	
	public Stream() {
	}

	public Stream(String id, String user_id, String user_login, String user_name, String game_id, String game_name,
			String type, String title, int viewer_count, String started_at, String language, String thumbnail_url,
			String tag_ids, boolean is_mature, String pagination) {
		this.id = id;
		this.user_id = user_id;
		this.user_login = user_login;
		this.user_name = user_name;
		this.game_id = game_id;
		this.game_name = game_name;
		this.type = type;
		this.title = title;
		this.viewer_count = viewer_count;
		this.started_at = started_at;
		this.language = language;
		this.thumbnail_url = thumbnail_url;
		this.tag_ids = tag_ids;
		this.is_mature = is_mature;
		this.pagination = pagination;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_login() {
		return user_login;
	}
	public void setUser_login(String user_login) {
		this.user_login = user_login;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getGame_id() {
		return game_id;
	}
	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}
	public String getGame_name() {
		return game_name;
	}
	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getViewer_count() {
		return viewer_count;
	}
	public void setViewer_count(int viewer_count) {
		this.viewer_count = viewer_count;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getThumbnail_url() {
		return thumbnail_url;
	}
	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}
	public String getTag_ids() {
		return tag_ids;
	}
	public void setTag_ids(String tag_ids) {
		this.tag_ids = tag_ids;
	}
	public boolean isIs_mature() {
		return is_mature;
	}
	public void setIs_mature(boolean is_mature) {
		this.is_mature = is_mature;
	}
	public String getPagination() {
		return pagination;
	}
	public void setPagination(String pagination) {
		this.pagination = pagination;
	}
	
	

}
