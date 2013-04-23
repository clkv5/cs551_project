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
    
    static final String ACCOUNT_URL = "http://170.224.169.101/SternerLearn/AccountService.asmx";
    static final String   ACCOUNT_LOGIN = "Login";
    static final String   ACCOUNT_REGISTER = "Register";
    static final String   ACCOUNT_LINK = "LinkAccounts";
    static final String   ACCOUNT_GET_LINK = "getLinkedAccount";
    
    static final String STUDENT_URL = "http://170.224.169.101/SternerLearn/StudentDataService.asmx";
    static final String   STUDENT_ADD_ASSIGNMENT = "addAssignment";
    static final String   STUDENT_ADD_CLASS = "addClass";
    static final String   STUDENT_ADD_GRADE = "addGrade";
    static final String   STUDENT_ADD_INFRACTION = "addInfraction";
    static final String   STUDENT_ADD_STUDENT_TO_CLASS = "addStudentToClass";
    static final String   STUDENT_GET_ASSIGNMENTS = "getAssignments";
    static final String   STUDENT_GET_CLASSES = "getClasses";
    static final String   STUDENT_GET_GRADES = "getGrades";
    static final String   STUDENT_GET_INFRACTIONS = "getInfractions";
    static final String   STUDENT_GET_PARENT_ID = "getParentID";
    static final String   STUDENT_GET_STUDENT_ID = "getStudentID";
    static final String   STUDENT_GET_TAUGHT_CLASSES = "getTaughtClasses";
    static final String   STUDENT_GET_STUDENT = "getStudent";
    
    static final String PARENT_URL = "http://170.224.169.101/SternerLearn/ParentalManagementService.asmx";
    static final String   PARENT_ADD_LOCATION = "addLocation";
    static final String   PARENT_GET_LOCATIONS = "getLocations";
    static final String   PARENT_ADD_MESSAGE = "addMessage";
    static final String   PARENT_GET_MESSAGES = "getMessages";
    
    static final String PREFS_FILE = "MY_PREFERENCES_FILE_NAME";
    
    static final String LOGIN_KEY = "LOGIN_KEY";
    static final String PW_KEY = "PASSWORD_KEY";
    
    static final String STUDENT_LOGIN_KEY = "STUDENT_LOGIN_KEY";
    static final String STUDENT_PW_KEY = "STUDENT_PASSWORD_KEY";
    
}