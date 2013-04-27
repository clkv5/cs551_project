package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class Teacher 
{
	public Account mAccount;
	
	public ArrayList<Course> mCourses = new ArrayList<Course>();

	public final Semaphore mLock = new Semaphore(1, true );
	
	public Teacher( Account acct )
	{
		mAccount = acct;
	}
	
	public ArrayList<Course> getCourses() throws InterruptedException
	{
		ArrayList<Course> tmp;
		mLock.acquire();
		tmp = mCourses;
		mLock.release();
		return tmp;
	}
	
	public void updateClasses() throws InterruptedException
	{
		mLock.acquire();
		mCourses.clear();
		
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aTeacherID", mAccount.mId));
		
    	/* Call web service */
    	try 
    	{		
    		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_TAUGHT_CLASSES, properties);
    		SoapObject response = (SoapObject)envelope.getResponse();

    		// It's a list of classes
    		for( int i = 0; i < response.getPropertyCount(); i++ )
    		{
	    		SoapObject arr = (SoapObject)response.getProperty(i);
	    		
		   		if( arr.hasProperty("mCourseID") &&
		   			arr.hasProperty("mStaffID") &&
		   			arr.hasProperty("mClassName") )
		   		{
		   			
		   			int id = java.lang.Integer.parseInt( arr.getProperty("mCourseID").toString() );
		   			int staffId = java.lang.Integer.parseInt( arr.getProperty("mStaffID").toString() );
		   			String name = arr.getProperty("mClassName").toString();
		   			
		   			mCourses.add( new Course( id, staffId, name ) );
		   		}
    		}
    	}
    	catch(Exception ex) {}		
    	
    	mLock.release();
	}
	
	public SoapSerializationEnvelope addClass(String className)
	{
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aTeacherID", mAccount.mId));
		properties.add(new PropertyWrapper("aPassword", mAccount.mPassword));
		properties.add(new PropertyWrapper("aClassName", className));
		
		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_ADD_CLASS, properties);
		
		// Call updateClasses to refresh our class list
		try
		{
			updateClasses();
		}
		catch( Exception ex ) {}
		
		return envelope;
	}
}
