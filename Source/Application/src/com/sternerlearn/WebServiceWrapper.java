package com.sternerlearn;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceWrapper 
{
	private static WebServiceWrapper instance = null;
	
	public WebServiceWrapper()
	{
		
	}
	
	public static WebServiceWrapper getInstance()
	{
		if( instance == null )
		{
			instance = new WebServiceWrapper();
		}
		return instance;
	}	
	
	public SoapSerializationEnvelope call
		(
		String aUrl,
		String aMethod,
		List<PropertyWrapper> aProperties
		)
	{
    	
    	/* Initialize transfer protocol */
    	HttpTransportSE transport = new HttpTransportSE(aUrl);
    	transport.debug = true;
    	SoapObject soapObject  = new SoapObject(Types.NAMESPACE, aMethod); 
    	
    	for( int i = 0; i < aProperties.size(); i++ )
    	{
    		String key = aProperties.get(i).mKey;
    		Object obj = aProperties.get(i).mObj;
	    	soapObject.addProperty(key, obj);
    	}
    	
    	/* Create packet envelope */
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11 ); 
    	envelope.dotNet = true;
    	envelope.setOutputSoapObject( soapObject );
    	
    	try 
    	{
    		transport.call(Types.NAMESPACE + aMethod, envelope);
    	}
    	catch( Exception ex) {}
    	
    	return envelope;
	}
}
