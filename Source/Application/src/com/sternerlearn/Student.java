package com.sternerlearn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;


public class Student 
{
	public Account mAccount;
	public boolean mValid = false;
	
	public HashMap<Course, ArrayList<Grade>> mCourses = new HashMap<Course, ArrayList<Grade>>();
	
	// DEBUG
	public String mResponse;

	public Student( Account aAccount )
	{
		mAccount = aAccount;
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
		   			
		   			mCourses.put( new Course( id, staffId, name ), new ArrayList<Grade>() );
		   		}
    		}
    	}
    	catch(Exception ex)
    	{
    		mResponse = ex.toString();
    		mValid = false;
		}				

	}
	
	public ArrayList<Assignment> getAssignments( Course course )
	{
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("aClassID", course.mId));
		
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
    	/* Call web service */
    	try 
    	{		
    		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_ASSIGNMENTS, properties);
    	
    		SoapObject response = (SoapObject)envelope.getResponse();

    		// It's a list of classes
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
	
	Timestamp hackDate( String date )
	{
		// Annoyingly the string format for the Java times isn't the same as our SQL data
		// Hack it up so it accepts it
		String hackDate = date;
		hackDate = hackDate.replaceFirst("T", " ");		
		Timestamp time = Timestamp.valueOf(hackDate); 
		return time;
	}
	
	public void updateGrades()
	{
		if( mCourses.size() == 0 )
		{
			updateClasses();
		}
		
		Set<Course> courses = mCourses.keySet();
		
		for( Course c : courses )
		{
			// First get the assignments for this class
			ArrayList<Assignment> assignments = getAssignments( c );
			
			// Then get the grades for this class
			
			List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
			properties.add(new PropertyWrapper("aStudentID", mAccount.mId));
			properties.add(new PropertyWrapper("aClassID", c.mId));
			
			ArrayList<Grade> grades = new ArrayList<Grade>();
			
	    	/* Call web service */
	    	try 
	    	{		
	    		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.STUDENT_URL, Types.STUDENT_GET_GRADES, properties);
	    	
	    		SoapObject response = (SoapObject)envelope.getResponse();
	
	    		// It's a list of classes
	    		for( int i = 0; i < response.getPropertyCount(); i++ )
	    		{
		    		SoapObject arr = (SoapObject)response.getProperty(i);
		    		
			   		if( arr.hasProperty("mGradeID") &&
			   			arr.hasProperty("mAssignmentID") &&
			   			arr.hasProperty("mStudentID") &&
			   			arr.hasProperty("mPointsEarned") &&
			   			arr.hasProperty("mDateSubmitted") )
			   		{
			   			int gradeId = java.lang.Integer.parseInt( arr.getProperty("mGradeID").toString() );
			   			int assignId = java.lang.Integer.parseInt( arr.getProperty("mAssignmentID").toString() );
			   			double pointsEarned = java.lang.Double.parseDouble( arr.getProperty("mPointsEarned").toString() );
			   			Timestamp time = hackDate( arr.getProperty("mDateSubmitted").toString() );
			   			
			   			// Find the assignment that corresponds with this grade
			   			Assignment ours = null;
			   			for( Assignment a : assignments )
			   			{
			   				if( a.mId == assignId )
			   				{
			   					ours = a;
			   					break;
			   				}
			   			}
			   			assignments.remove(ours);
			   			
			   			grades.add( new Grade( ours, gradeId, pointsEarned, time ) );
			   		
			   		}
	    		}
	    	}
	    	catch(Exception ex) {}	
	    	
	    	mCourses.put( c, grades );
		}
		
	}
}
