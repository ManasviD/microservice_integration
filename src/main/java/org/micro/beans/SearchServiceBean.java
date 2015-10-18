package org.micro.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchServiceBean {
	
	private int count;
	private Item[]item;
	

public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	

public Item[] getItem() {
		return item;
	}


	public void setItem(Item[] item) {
		this.item = item;
	}




public static class Item {
	
	private String id;
	private String state;
	private String user_id;
	private String description;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}

}
