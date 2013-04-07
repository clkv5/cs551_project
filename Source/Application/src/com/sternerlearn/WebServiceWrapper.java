package com.sternerlearn;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import org.kobjects.isodate.IsoDate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class WebServiceWrapper 
{
	public static String mResponse = "Initial";
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
    	envelope.implicitTypes = true;
    	envelope.dotNet = true;
    	envelope.encodingStyle = SoapSerializationEnvelope.XSD;
    	envelope.setOutputSoapObject( soapObject );
    	
    	/* Register marshals */
    	MarshalDouble marshalDouble = new MarshalDouble();
    	MarshalDate marshalDate = new MarshalDate();
    	marshalDouble.register(envelope);
    	marshalDate.register(envelope);
    	
    	/* Call web service */
    	try 
    	{
    		transport.call(Types.NAMESPACE + aMethod, envelope);
    		mResponse = "SOAP transport OK";
    	}
    	catch( Exception ex)
    	{
    		mResponse = ex.toString();
    	}
    	
    	return envelope;
	}
	
	public class MarshalDouble implements Marshal 
	{
	    public Object readInstance(XmlPullParser parser, String namespace, String name, 
	            PropertyInfo expected) throws IOException, XmlPullParserException
	    {
	        return Double.parseDouble(parser.nextText());
	    }

	    public void register(SoapSerializationEnvelope cm)
	    {
	         cm.addMapping(cm.xsd, "double", Double.class, this);
	    }


	    public void writeInstance(XmlSerializer writer, Object obj) throws IOException
	    {
	        writer.text(obj.toString());
	    }
	}
	
	public class MarshalDate implements Marshal
	{
        public Object readInstance(XmlPullParser parser, String namespace, String name, 
                PropertyInfo expected) throws IOException, XmlPullParserException
        {
            return IsoDate.stringToDate(parser.nextText(), IsoDate.DATE_TIME);
        }

        public void register(SoapSerializationEnvelope cm)
        {
            cm.addMapping(cm.xsd, "DateTime", Date.class, this);
        }

        public void writeInstance(XmlSerializer writer, Object obj) throws IOException
        {
            writer.text(IsoDate.dateToString((Date) obj, IsoDate.DATE_TIME));
        }
	}
	
}
