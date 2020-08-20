package com.furnaceapp.grid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import org.apache.log4j.*;

/*
 * TYPE_IDENTIFIER = "SOMETHING A", "SOMETHING 1", "SOME THING 2", etc.
 * Only one last character is supported and A maps to 1, B to 2, etc.
 * The last character, converted to an int, becomes idNum
 * The rest of the string becomes idName
 */
public class GridValue {
	private static Logger logger = Logger.getLogger( GridValue.class );

	private static final DateFormat dateTimeFormat = new SimpleDateFormat( "yyyy/MM/dd hh:mm:ss" );
	private static final DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd" );
	private static final Pattern timePattern = Pattern.compile( "\\d\\d:\\d\\d:\\d\\d" );
	private static final Pattern tcPattern = Pattern.compile( " TC" );
	private static final Pattern replacedtcPattern = Pattern.compile( "REPLACEDTC" );
	private GridValueType type = GridValueType.TYPE_STRING;
	private String value = "", input = "", idName = "";
	private Date dateValue = null;
	private Float floatValue = null;
	private int idNum = -1;

	public GridValue( String s ) {
		char cc;

		input = s;

		if ( s == null ) {
			value = "";
			type = GridValueType.TYPE_STRING;
			return;
		}

		value = s.trim();

		//if ( s.startsWith( "517" ) ) {
		//	logger.info( "LOADING GRIDVALUE FROM " + s );
		//}

		try {
			dateValue = dateTimeFormat.parse( value );
			type = GridValueType.TYPE_DATETIME;
			return;
		}
		catch ( Exception e ) {
		}

		try {
			dateValue = dateFormat.parse( value );
			type = GridValueType.TYPE_DATE;
			return;
		}
		catch ( Exception e ) {
		}

		try {
			Matcher m = timePattern.matcher( value );
			if ( m.find() ) {
				type = GridValueType.TYPE_TIME;
				return;
			}
		}
		catch ( Exception e ) {
		}

		try {
			floatValue = Float.parseFloat( value );
			type = GridValueType.TYPE_NUMBER;
			return;
		}
		catch ( Exception e ) {
		}

		int len = value.length();

		if ( len < 2 ) {
			type = GridValueType.TYPE_STRING;
			return;
		}

		char ccLast = value.charAt( len - 1 );
		char ccPenult = value.charAt( len - 2 );
		idNum = parseIDNumber( ccLast );

		if ( ccPenult == ' ' && idNum >= 0 ) {
			idName = value.substring( 0, len - 2 );
			type = GridValueType.TYPE_IDENTIFIER;
			return;
		}

		if ( idNum >= 0 && ccLast >= '0' && ccLast <= '9' ) {
			idName = value.substring( 0, len - 1 );
			type = GridValueType.TYPE_IDENTIFIER;
			return;
		}

		idNum = -1;
		type = GridValueType.TYPE_STRING;
	}

	public GridValueType getType() {
		return type;
	}

	public String getIDName() {
		return idName;
	}

	public int getIDNum() {
		return idNum;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "input: " + input + ", value=" + value + ", idName=" + idName + ", idNum=" + idNum + ", type=" + type.name();
	}

	protected int parseIDNumber( char cc ) {
		if ( cc >= 'A' && cc <= 'Z' ) {
			return 1 + ( cc - 'A' );
		}

		if ( cc >= '0' && cc <= '9' ) {
			return cc - '0';
		}

		return -1;
	}
}
