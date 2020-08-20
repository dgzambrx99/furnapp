package com.furnaceapp.model;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import org.apache.log4j.*;

public class DateTimeParser {
	private static Logger logger = Logger.getLogger( DateTimeParser.class );
	private static final String EXCEL_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private int colFullDateTime = 0, colDate = 0, colTime = 0;

	public DateTimeParser( TreeMap<Integer,ModelIdentifier> readingsMapping ) throws Exception {
		for ( Map.Entry<Integer,ModelIdentifier> entry : readingsMapping.entrySet() ) {
			ModelIdentifier mi = entry.getValue();
			if ( mi != null ) {
				if ( mi.getType() == ModelIdentifierType.READING_DATE_FULL ) {
					colFullDateTime = entry.getKey();
				}
				else if ( mi.getType() == ModelIdentifierType.READING_DATE_DATE_ONLY ) {
					colDate = entry.getKey();
				}
				else if ( mi.getType() == ModelIdentifierType.READING_DATE_TIME_ONLY ) {
					colTime = entry.getKey();
				}
			}
		}

		if ( colFullDateTime == 0 && colDate == 0 && colTime == 0 ) {
			throw new Exception( "No date/time columns set" );
		}

		//logger.info( "colFullDateTime=" + colFullDateTime + ", colDate=" + colDate + ", colTime=" + colTime );
	}

	public java.sql.Timestamp getTimestamp( IGrid grid ) throws Exception {
		return getSQLTimestamp( getTimeDateString( grid ) );
	}

	public int getSelected( IGrid grid ) throws Exception {
		int ts = getTimestamp( getTimeDateString( grid ) );
		return ( ( ts % 120 ) == 0 ) ? 1 : 0;
	}

	protected String getTimeDateString( IGrid grid ) throws Exception {
		if ( colFullDateTime > 0 ) {
			GridValue full = grid.getColumnValue( colFullDateTime );
			if ( full == null ) {
				throw new Exception( "Date+time not found in column " + colFullDateTime );
			}
			return full.getValue();
		}
		else {
			GridValue dt = grid.getColumnValue( colDate );
			GridValue tm = grid.getColumnValue( colTime );
			if ( dt == null || tm == null ) {
				throw new Exception( "Date/time not found in columns " + colDate + " and " + colTime );
			}
			return dt.getValue() + " " + tm.getValue();
		}
	}

	protected java.sql.Timestamp getSQLTimestamp( String dateandtime ) throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat( EXCEL_DATE_TIME_FORMAT );
			java.util.Date newDate = format.parse( dateandtime );
			return new java.sql.Timestamp( newDate.getTime() );
		}
		catch ( Exception e ) {
			logger.info( "Unable to parse " + dateandtime + " (as " + EXCEL_DATE_TIME_FORMAT + ")" );
			throw e;
		}
	}

	protected int getTimestamp( String dateandtime ) throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat( EXCEL_DATE_TIME_FORMAT );
			java.util.Date newDate = format.parse( dateandtime );
			return (int) ( newDate.getTime() / 1000L );
		}
		catch ( Exception e ) {
			logger.info( "Unable to parse " + dateandtime + " (as " + EXCEL_DATE_TIME_FORMAT + ")" );
			throw e;
		}
	}
}
