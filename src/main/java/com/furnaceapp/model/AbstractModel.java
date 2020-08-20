package com.furnaceapp.model;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import org.apache.log4j.*;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import com.furnaceapp.model.ModelIdentifier;

public abstract class AbstractModel implements IModel {
	private static Logger logger = Logger.getLogger( AbstractModel.class );

	private TreeMap<Integer,ModelIdentifier> readingsMapping = null;

	public abstract void save( Connection conn ) throws Exception;
	public abstract void loadReadings( IGrid grid, int maxRows, int which ) throws Exception;
	public abstract int getReadingsCount();

	public void loadReadingMapping( IGrid grid ) throws Exception {
		readingsMapping = new TreeMap<Integer,ModelIdentifier>();
		for ( int column = 1; column < 26; column++ ) {
			GridValue gv = grid.getColumnValue( column );
			if ( gv == null ) {
				readingsMapping.put( column, null );
			}
			else {
				ModelIdentifier mi = new ModelIdentifier( gv );
				readingsMapping.put( column, mi );
			}
		}
	}

	public TreeMap<Integer,ModelIdentifier> getReadingMapping() {
		return readingsMapping;
	}

	public void setReadingMapping( int column, ModelIdentifier mi ) throws Exception {
		validateColumn( column );
		readingsMapping.put( column, mi );
	}

	protected void validateColumn( int column ) {
		if ( column < 1 || column > 26 ) {
			throw new IllegalArgumentException( "Bad column value " + column );
		}
	}

	public void setValue( String key, GridValue value ) throws Exception {
	}

	public void setValue( String key, String value ) throws Exception {
	}

	public void setValue( String key, Date value ) throws Exception {
	}

	public void setValue( String key, float value ) throws Exception {
	}

	public void setValue( String key, int value ) throws Exception {
	}

	public void setValue( String key, GridValue[] value ) throws Exception {
	}

	public void setValue( String key, String[] value ) throws Exception {
	}

	public void setValue( String key, Date[] value ) throws Exception {
	}

	public void setValue( String key, float[] value ) throws Exception {
	}

	public void setValue( String key, int[] value ) throws Exception {
	}
}
