package com.sternerlearn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class Parent 
{
	public Account mAccount;
	
	public ArrayList<Course> mCourses = new ArrayList<Course>();
	
	public Parent( Account acct )
	{
		mAccount = acct;
	}


}
