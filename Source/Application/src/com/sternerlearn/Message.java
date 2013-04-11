package com.sternerlearn;

public class Message {

	
	private String sender;
	private String message;
	private String time;
	
	
	public Message( String aSender, String aMessage, String aTime )
	{
		sender = aSender;
		message = aMessage;
		time = aTime.replaceFirst("T", " ");
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getDate()
	{
		return time;
	}
}
