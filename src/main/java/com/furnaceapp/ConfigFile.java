package com.furnaceapp;

import java.util.Properties;
import java.io.InputStream;
import org.apache.log4j.*;

public class ConfigFile {
	private static Logger logger = Logger.getLogger( ConfigFile.class );
	private Properties props;

	public ConfigFile() throws Exception {
		props = new Properties();
		InputStream in = getClass().getResourceAsStream( "/app.properties" );
		props.load( in );
		in.close();
		System.out.println( "loaded" );
	}

	public String getString( String key ) throws Exception {
		String temp = props.getProperty( key );
		System.out.println( "got " + temp + " for " + key );
		if ( temp == null ) {
			throw new IllegalArgumentException( "ConfigFile: property " + key + " not found" );
		}

		return temp;
	}

	public int getInt( String key ) throws Exception {
		String temp = props.getProperty( key );
		if ( temp == null ) {
			throw new IllegalArgumentException( "ConfigFile: property " + key + " not found" );
		}

		return Integer.parseInt( temp );
	}
}
