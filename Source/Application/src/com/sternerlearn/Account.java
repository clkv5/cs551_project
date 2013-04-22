package com.sternerlearn;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public class Account 
{
	
	public int mId;
	public int mAccountType;
	public int mLinkedId;
	public String mEmailAddress;
	public String mPassword;
	public String mRealName;
	public boolean mValid;
	
	// DEBUG
	public String mResponse = "BLAH BLAH";
	
	public Account()
	{
		mValid = false;
	}

	public Account( String aEmail, String aPassword )
	{
		login( aEmail, aPassword );
	}
	
	public Account( Account other )
	{
		mId = other.mId;
		mAccountType = other.mAccountType;
		mLinkedId = other.mLinkedId;
		mEmailAddress = other.mEmailAddress;
		mPassword = other.mPassword;
		mRealName = other.mRealName;
		mValid = other.mValid;
	}
	
	public Account
		(
		String aEmail,
		String aPassword,
		String aPassRepeat,
		String aName,
		int aAccountType
		)
	{
		register( aEmail, aPassword, aPassRepeat, aName, aAccountType );
	}
	
	void getAccount( SoapSerializationEnvelope envelope )
	{
    	/* Call web service */
    	try 
    	{	
    		SoapObject response = (SoapObject)envelope.getResponse();
    		mResponse = response.toString();

    		// It's a list of accounts, but there should only be 1
    		if(response.hasProperty("Account") )
    		{
	    		SoapObject arr = (SoapObject)response.getProperty("Account");
	    		
		   		if( !arr.hasProperty("id") ||
		   			!arr.hasProperty("accountType") ||
		   			!arr.hasProperty("linkedID") ||
		   			!arr.hasProperty("emailAddress") ||
		   			!arr.hasProperty("password") ||
		   			!arr.hasProperty("realName") )
		   		{
		   			mValid = false;
		   		}
		   		else
		   		{
		   			mValid = true;
		   			
		   			mId = java.lang.Integer.parseInt( arr.getProperty("id").toString() );
		   			mAccountType = java.lang.Integer.parseInt( arr.getProperty("accountType").toString() );
		   			mLinkedId = java.lang.Integer.parseInt( arr.getProperty("linkedID").toString() );
		   			mEmailAddress = arr.getProperty("emailAddress").toString();
		   			mPassword = arr.getProperty("password").toString();
		   			mRealName = arr.getProperty("realName").toString();
		   		}
    		}
    		else
    		{
    			mResponse = "NO ACCOUNT RETURNED??";
    			mValid = false;
    		}
    	}
    	catch(Exception ex)
    	{
    		// Either it's not a valid username and password, or we failed
    		mResponse = ex.toString();
    		mValid = false;
		}				
	}	
	
	void register
		(
		String aEmail,
		String aPassword,
		String aPassRepeat,
		String aName,
		int aAccountType
		)
	{
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("email", aEmail));
		properties.add(new PropertyWrapper("password", aPassword));
		properties.add(new PropertyWrapper("passwordRepeat", aPassRepeat));
		properties.add(new PropertyWrapper("realName", aName));
		properties.add(new PropertyWrapper("type", aAccountType));
		
		SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.ACCOUNT_URL, "Register", properties);
    	
    	getAccount( envelope );

	}
	
	void login( String aEmail, String aPassword )
	{
		mResponse = aEmail + " " + aPassword + " / ";
		
		List<PropertyWrapper> properties = new ArrayList<PropertyWrapper>();
		properties.add(new PropertyWrapper("email", aEmail));
		properties.add(new PropertyWrapper("password", aPassword));

        SoapSerializationEnvelope envelope = WebServiceWrapper.getInstance().call(Types.ACCOUNT_URL, "Login", properties);
    	
    	getAccount( envelope );
	}
}
