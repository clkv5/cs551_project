package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class Student 
{
	public List<Course> mClasses;
	public Account mAccount;
	public boolean mValid = false;
	
	// DEBUG
	public String mResponse;

	public Student( Account aAccount )
	{
		mAccount = aAccount;
		mClasses = new ArrayList<Course>();
		updateClasses();
	}
	
	public void updateClasses()
	{
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aStudentID", mAccount.mId));
		
    	/* Call web service */
    	try 
    	{		
    		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, "getClasses", properties);
    	
	
    		SoapObject response = (SoapObject)envelope.getResponse();
    		mResponse = response.toString() + " " + response.getPropertyCount();

    		// It's a list of classes
    		for( int i = 0; i < response.getPropertyCount(); i++ )
    		{
	    		SoapObject arr = (SoapObject)response.getProperty(i);
	    		
		   		if( !arr.hasProperty("mCourseID") ||
		   			!arr.hasProperty("mStaffID") ||
		   			!arr.hasProperty("mClassName") )
		   		{
		   			mValid = false;
		   		}
		   		else
		   		{
		   			mValid = true;
		   			
		   			int id = java.lang.Integer.parseInt( arr.getProperty("mCourseID").toString() );
		   			int staffId = java.lang.Integer.parseInt( arr.getProperty("mStaffID").toString() );
		   			String name = arr.getProperty("mClassName").toString();
		   			
		   			mClasses.add( new Course( id, staffId, name ) );
		   		}
    		}
    	}
    	catch(Exception ex)
    	{
    		mResponse = ex.toString();
    		mValid = false;
		}				

	}
}
