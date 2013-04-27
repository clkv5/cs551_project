package com.sternerlearn;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Infraction 
{
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, String> sTypes = new HashMap<Integer, String>();
	
	public String mString;
	public String mDescription;
	public Timestamp mTime;
	
	public Infraction( Integer type, String desc, Timestamp time )
	{
		if( sTypes.size() == 0 )
		{
			sTypes.put( 0, "Tardy" );
			sTypes.put( 1, "Absent" );
			sTypes.put( 2, "Detention" );
			sTypes.put( 3, "Suspended" );
			sTypes.put( 4, "Expelled" );
		}
		
		mString = sTypes.get(type);
		mDescription = desc;
		mTime = time;
		
	};
	
	
}
