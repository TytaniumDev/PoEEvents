package com.example.poeevents;

import com.google.gson.annotations.SerializedName;

public class PoEEventRule {
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";

	@SerializedName(TAG_NAME)
	private String name;
	@SerializedName(TAG_DESCRIPTION)
	private String description;

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
}
