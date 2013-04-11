package com.sternerlearn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class Assignment 
{
	public int mId;
	public int mCourseID;
	public String mName;
	public String mDesc;
	public double mPoints;
	public Timestamp mDueDate;
	public boolean mTest;

	public Assignment
		(
		int id,
		int courseId,
		String name,
		String desc,
		double points,
		Timestamp date,
		boolean test
		)
	{
		mId = id;
		mCourseID = courseId;
		mName = name;
		mDesc = desc;
		mPoints = points;
		mDueDate = date;
		mTest = test;
	}

	public static ArrayList<Assignment> getAssignments( Course course )
	{
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aClassID", course.mId));
		
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
    	/* Call web service */
    	try 
    	{		
    		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_ASSIGNMENTS, properties);
    	
    		SoapObject response = (SoapObject)envelope.getResponse();

    		// It's a list of assignments
    		for( int i = 0; i < response.getPropertyCount(); i++ )
    		{
	    		SoapObject arr = (SoapObject)response.getProperty(i);
	    		
		   		if( arr.hasProperty("mAssignment") &&
		   			arr.hasProperty("mCourseID") &&
		   			arr.hasProperty("mName") &&
		   			arr.hasProperty("mDescription") &&
		   			arr.hasProperty("mPoints") &&
		   			arr.hasProperty("mDueDate") &&
		   			arr.hasProperty("mIsTest") )
		   		{
		   			int assignId = java.lang.Integer.parseInt( arr.getProperty("mAssignment").toString() );
		   			int courseId = java.lang.Integer.parseInt( arr.getProperty("mCourseID").toString() );
		   			String name = arr.getProperty("mName").toString();
		   			String desc = arr.getProperty("mDescription").toString();
		   			double points = java.lang.Double.parseDouble( arr.getProperty("mPoints").toString() );
		   			Timestamp time = hackDate( arr.getProperty("mDueDate").toString() );
		   			boolean test = arr.getProperty("mIsTest").toString() == "1" ? true : false;
		   			
		   			assignments.add( new Assignment( assignId, courseId, name, desc, points, time, test ) );
		   		}
    		}
    	}
    	catch(Exception ex) {}
    	
    	return assignments;
		
	}	
	
	static Timestamp hackDate( String date )
	{
		// Annoyingly the string format for the Java times isn't the same as our SQL data
		// Hack it up so it accepts it
		String hackDate = date;
		hackDate = hackDate.replaceFirst("T", " ");		
		Timestamp time = Timestamp.valueOf(hackDate); 
		return time;
	}	
}
