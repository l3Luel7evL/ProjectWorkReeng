package com.energent.bean;

public class Message {
	private String msg;

	protected Message() {}
	public Message(String msg) {this.msg = msg;}
	
	public String getMsg() {return msg;}
	public void setMsg(String msg) {
		this.msg = msg;
		
	}	
}