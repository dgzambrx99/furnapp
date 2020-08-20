package com.furnaceapp.excel;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.security.*;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.xml.sax.InputSource;
import org.apache.log4j.*;

class SharedStrings {
	private static Logger logger = Logger.getLogger( SharedStrings.class );
	private HashMap<String, String> data;

	public SharedStrings( String s ) throws Exception {
		data = new HashMap<String, String>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource( new StringReader( s ) );
		Document doc = builder.parse( is );

		NodeList rows = doc.getElementsByTagName( "si" );
		for ( int i = 0; i < rows.getLength(); i++ ) {
			Element row = (Element) rows.item( i );
			String val = row.getTextContent();
			data.put( "" + i, val );
		}

		//logger.info( "SharedStrings=" + data );
	}

	public boolean has( String key ) {
		return data.get( key ) != null;
	}

	public String translate( String key ) {
		return data.get( key );
	}
}
