package com.sternerlearn;

import java.sql.Timestamp;

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
}
