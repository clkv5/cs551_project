package com.sternerlearn;

import java.util.ArrayList;

public class Course 
{
	public int mId;
	public int mStaffId;
	public String mClassName;
	public ArrayList<Assignment> mAssignments = new ArrayList<Assignment>();
	
	public Course( int aId, int aStaffId, String aName )
	{
		mId = aId;
		mStaffId = aStaffId;
		mClassName = aName;
	}
	
}
