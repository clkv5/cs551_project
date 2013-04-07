package com.sternerlearn;

public class SharedData 
{
	private static SharedData instance = null;
	
	private Account mAccount;
	private Student mStudent;
	
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
}
