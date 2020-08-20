package com.furnaceapp.model.basic;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.furnaceapp.model.AbstractModel;
import com.furnaceapp.model.IModel;
import com.furnaceapp.model.ModelIdentifier;
import com.furnaceapp.model.DateTimeParser;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import org.apache.log4j.*;

public class BasicIceboxModel extends AbstractModel implements IModel {
	private static Logger logger = Logger.getLogger( BasicIceboxModel.class );

	private ArrayList<BasicTableIceboxBase> rows = null;

	public void loadReadings( IGrid grid, int maxRows, int which ) throws Exception {
		BasicTableIceboxBase btfb;
		int rowCount = 0;

		rows = new ArrayList<BasicTableIceboxBase>();
		DateTimeParser dateTimeParser = new DateTimeParser( getReadingMapping() );

		while ( rowCount < maxRows ) {
			try {
				btfb = new BasicTableIceboxBase( grid, getReadingMapping(), dateTimeParser, which );
				rows.add( btfb );
			}
			catch ( Exception e ) {
				logger.error( "Can't load from row " + grid.getCurrentRow(), e );
			}

			rowCount++;
			if ( grid.setCurrentRow( grid.getCurrentRow() + 1 ) < 1 ) {
				break;
			}
		}
	}

	public void save( Connection conn ) throws Exception {
		for ( BasicTableIceboxBase btfb : rows ) {
			btfb.save( conn );
		}
	}

	public int getReadingsCount() {
		return rows.size();
	}

	public String toString() {
		return "" + getReadingMapping();
	}
}

