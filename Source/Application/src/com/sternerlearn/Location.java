package com.sternerlearn;

public class Location {

	private float latitude;
	private float longitude;
	private String time;
	
	public Location( float aLat, float aLon, String aTime )
	{
		latitude = aLat;
		longitude = aLon;
		time = aTime.replaceFirst("T", " ");
	}
	
	public float getLatitude()
	{
		return latitude;
	}
	
	public float getLongitude()
	{
		return longitude;
	}
	
	public String getDate()
	{
		return time;
	}
}
