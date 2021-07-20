package com.kokochi.samp.queryAPI.domain;

public class TwitchUser {
	
	private String broadcaster_type;
	private String description;
	private String display_name;
	private String id;
	private String login;
	private String offline_image_url;
	private String profile_image_url;
	private String type;
	private int view_count;
	private String email;
	private String created_at;
	
	
	public String getBroadcaster_type() {
		return broadcaster_type;
	}
	public void setBroadcaster_type(String broadcaster_type) {
		this.broadcaster_type = broadcaster_type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getOffline_image_url() {
		return offline_image_url;
	}
	public void setOffline_image_url(String offline_image_url) {
		this.offline_image_url = offline_image_url;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getView_count() {
		return view_count;
	}
	public void setView_count(int view_count) {
		this.view_count = view_count;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "TwitchUser [broadcaster_type=" + broadcaster_type + ", description=" + description + ", display_name="
				+ display_name + ", id=" + id + ", login=" + login + ", offline_image_url=" + offline_image_url
				+ ", profile_image_url=" + profile_image_url + ", type=" + type + ", view_count=" + view_count
				+ ", email=" + email + ", created_at=" + created_at + "]";
	}
}
