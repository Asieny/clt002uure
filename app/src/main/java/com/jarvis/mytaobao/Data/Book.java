package com.jarvis.mytaobao.Data;

public class Book {
	private int image;
	private String name;
	private String address;
	private String time;

	public Book(int image, String name, String address, String time) {
		super();
		this.image = image;
		this.name = name;
		this.address = address;
		this.time = time;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
