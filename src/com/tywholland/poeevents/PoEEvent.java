package com.tywholland.poeevents;

import com.google.gson.annotations.SerializedName;

/**
 * This class holds the event data, and has the tags needed to parse this object
 * from the JSON object
 */
public class PoEEvent {
	public static final String TAG_EVENT_NAME = "id";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_WEB_LINK = "url";
	public static final String TAG_REGISTER_TIME = "registerAt";
	public static final String TAG_START_TIME = "startAt";
	public static final String TAG_END_TIME = "endAt";

	@SerializedName(TAG_EVENT_NAME)
	private String name;
	@SerializedName(TAG_DESCRIPTION)
	private String description;
	@SerializedName(TAG_WEB_LINK)
	private String webLink;
	// These should be some sort of time
	@SerializedName(TAG_REGISTER_TIME)
	private String registerTime;
	@SerializedName(TAG_START_TIME)
	private String startTime;
	@SerializedName(TAG_END_TIME)
	private String endTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
