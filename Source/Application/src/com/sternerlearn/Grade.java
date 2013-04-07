package com.sternerlearn;

import java.sql.Timestamp;
import java.text.DecimalFormat;

public class Grade 
{
	private Assignment mAssignment;
	
	private int mId;
	private double mPointsReceived;
	private Timestamp mDateSub;
	
	// Get grade with a student ID and assignment ID, each individual grade also has an assignment ID
	
	public Grade( Assignment aAssignment, int id, double pts, Timestamp date )
	{
		mAssignment = aAssignment;
		
		mId = id;
		mPointsReceived = pts;
		mDateSub = date;
	}
	
	public String getAssignmentName()
	{
		String ret = mAssignment.mName;
		
		return ret;
	}
	
	public String getPointsString()
	{
		String ret = new DecimalFormat("#").format(mPointsReceived) + " / " + new DecimalFormat("#").format(mAssignment.mPoints);
		return ret;
	}
	
}
