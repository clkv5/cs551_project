package com.sternerlearn;

public class Types {
	
	public enum AccountType
	{
		
		PARENT,
		STUDENT,
		STAFF,
		INVALID
		
	}
	
    static final String NAMESPACE = "http://tempuri.org/";
    
    static final String ACCOUNT_URL = "http://170.224.169.101/Iteration2/AccountService.asmx/";
    static final String   ACCOUNT_LOGIN = "Login";
    static final String   ACCOUNT_REGISTER = "Register";
    static final String   ACCOUNT_LINK = "LinkAccounts";
    
    static final String STUDENT_URL = "http://170.224.169.101/Iteration2/StudentDataService.asmx/";
    static final String   STUDENT_ADD_ASSIGNMENT = "addAssignment";
    static final String   STUDENT_ADD_CLASS = "addAssignment";
    static final String   STUDENT_ADD_GRADE = "addAssignment";
    static final String   STUDENT_ADD_INFRACTION = "addAssignment";
    static final String   STUDENT_ADD_STUDENT_TO_CLASS = "addAssignment";
    static final String   STUDENT_GET_ASSIGNMENTS = "getAssignments";
    static final String   STUDENT_GET_CLASSES = "getClasses";
    static final String   STUDENT_GET_GRADES = "getGrades";
    static final String   STUDENT_GET_INFRACTIONS = "getInfractions";
    static final String   STUDENT_GET_PARENT_ID = "getParentID";
    static final String   STUDENT_GET_STUDENT_ID = "getStudentID";
    
    
    static final String PARENT_URL = "http://170.224.169.101/Iteration2/ParentalManagementService.asmx/";
    static final String   PARENT_ADD_LOCATION = "addLocation";
    static final String   PARENT_GET_LOCATIONS = "getLocations";
    static final String   PARENT_ADD_MESSAGE = "addMessage";
    static final String   PARENT_GET_MESSAGES = "getMessages";
    
}