package com.sternerlearn;

public class SharedData 
{
	private static SharedData instance = null;
	
	private Account mAccount;
	private Student mStudent;
	private Teacher mTeacher;
	private Parent  mParent;
	
	public Course mCurrentCourse;
	
	public SharedData()
	{
		
	}
	
	public static SharedData getInstance()
	{
		if( instance == null )
		{
			instance = new SharedData();
		}
		return instance;
	}
	
	public void setAccount( Account aAccount )
	{
		mAccount = aAccount;
	}
	
	public Account getAccount()
	{
		return mAccount;
	}
	
	public void setStudent( Student aStudent )
	{
		mStudent = aStudent;
	}
	
	public Student getStudent()
	{
		return mStudent;
	}
	
	public void setTeacher( Teacher teach )
	{
		mTeacher = teach;
	}
	
	public Teacher getTeacher()
	{
		return mTeacher;
	}
	
	public void setParent( Parent aParent )
	{
		mParent = aParent;
	}
	
	public Parent getParent()
	{
		return mParent;
	}
}
