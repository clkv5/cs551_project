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
    
    static final String ACCOUNT_URL = "http://170.224.169.101/Iteration2/AccountService.asmx";
    static final String STUDENT_URL = "http://170.224.169.101/Iteration2/StudentDataService.asmx";
    static final String PARENT_URL = "http://170.224.169.101/Iteration2/ParentalManagementService.asmx";
}