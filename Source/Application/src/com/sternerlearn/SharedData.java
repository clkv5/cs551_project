package com.sternerlearn;

public class SharedData 
{
	private static SharedData instance = null;
	
	private Account mAccount;
	
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
}
