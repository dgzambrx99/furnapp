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

public class BasicFurnaceModel extends AbstractModel implements IModel {
	private static Logger logger = Logger.getLogger( BasicFurnaceModel.class );

	private ArrayList<BasicTableFurnaceBase> rows = null;

	public void loadReadings( IGrid grid, int maxRows, int which ) throws Exception {
		BasicTableFurnaceBase btfb;
		int rowCount = 0;

		rows = new ArrayList<BasicTableFurnaceBase>();
		DateTimeParser dateTimeParser = new DateTimeParser( getReadingMapping() );

		while ( rowCount < maxRows ) {
			try {
				btfb = new BasicTableFurnaceBase( grid, getReadingMapping(), dateTimeParser, which );
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
		for ( BasicTableFurnaceBase btfb : rows ) {
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

/*
		DateTimeParser dateTimeParser = new DateTimeParser( readingsMapping );
		BasicTableFurnaceBase t = new BasicTableFurnaceBase( thegrid, dateTimeParser, 23 );
		t.addMultipoint( new BasicTableMultipoint( "somed", 0, 0 ) );
		t.addMultipoint( new BasicTableMultipoint( "somed1", 1, 0 ) );
		t.addMultipoint( new BasicTableMultipoint( "somed2", 2, 0 ) );
		t.addLoadTC( new BasicTableLoadTC( "somed", 0, 0 ) );
		t.addLoadTC( new BasicTableLoadTC( "somed1", 1, 0 ) );
		t.addLoadTC( new BasicTableLoadTC( "somed2", 2, 0 ) );
*/
