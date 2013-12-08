package com.tywholland.poeevents;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * This class holds the event data, and has the tags needed to parse this object
 * from the JSON object
 */
public class PoEEvent implements Serializable {
	private static final long serialVersionUID = -5563611290181261636L;
	public static final String TAG_EVENT_NAME = "id";
	public static final String TAG_WEB_LINK = "url";
	public static final String TAG_REGISTER_TIME = "registerAt";
	public static final String TAG_START_TIME = "startAt";
	public static final String TAG_END_TIME = "endAt";
	public static final String TAG_UPDATED = "updated";
	public static final String TAG_CLASSIFICATION = "shortName";
	public static final String TAG_NORMAL_NAME = "normalName";
	public static final String TAG_ALERT = "alert";
	public static final int CLASSIFICATION_CHAR_LOCATION = 3;

	@SerializedName(TAG_EVENT_NAME)
	private String name;
	@SerializedName(TAG_WEB_LINK)
	private String webLink;
	// These should be some sort of time
	@SerializedName(TAG_REGISTER_TIME)
	private String registerTime;
	@SerializedName(TAG_START_TIME)
	private String startTime;
	@SerializedName(TAG_END_TIME)
	private String endTime;
	private int updated;
	// 2 Hour Solo
	private String normalName;
	// S05S093 = Signature
	private String classification;
	private int alert;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public String getNormalName() {
		return normalName;
	}

	public void setNormalName(String normalName) {
		this.normalName = normalName;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public int getAlert() {
		return alert;
	}

	public void setAlert(int alert) {
		this.alert = alert;
	}
}
