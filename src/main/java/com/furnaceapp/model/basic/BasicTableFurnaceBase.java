package com.furnaceapp.model.basic;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.furnaceapp.model.DateTimeParser;
import com.furnaceapp.model.ModelIdentifier;
import com.furnaceapp.model.ModelIdentifierType;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import org.apache.log4j.*;

class BasicTableFurnaceBase {
	private static Logger logger = Logger.getLogger( BasicTableFurnaceBase.class );

	private ArrayList<BasicTableMultipoint> multipoints = new ArrayList<BasicTableMultipoint>();
	private ArrayList<BasicTableLoadTC> loadtcs = new ArrayList<BasicTableLoadTC>();
	private IGrid grid;
	private java.sql.Timestamp sqlts;
	private DateTimeParser dateTimeParser;
	private String tc = "", qt_water = "", qt_35 = "", qt_16 = "";
	private int id = 0, ts = 0, selected = 0, which = 0;

	BasicTableFurnaceBase( IGrid g, TreeMap<Integer,ModelIdentifier> readingsMapping, DateTimeParser dateTimeParser, int w ) throws Exception {
		GridValue gv;

		grid = g;
		sqlts = dateTimeParser.getTimestamp( grid );
		selected = dateTimeParser.getSelected( grid );
		which = w;

		for ( Map.Entry<Integer,ModelIdentifier> entry : readingsMapping.entrySet() ) {
			ModelIdentifier mi = entry.getValue();
			if ( mi == null ) {
				continue;
			}

			GridValue gridValue = grid.getColumnValue( entry.getKey() );
			if ( gridValue == null ) {
				continue;
			}

			ModelIdentifierType miType = mi.getType();
			int idNum = mi.getGridValue().getIDNum();

			if ( miType == ModelIdentifierType.CONTROL ) {
				tc = gridValue.getValue();
			}
			else if ( miType == ModelIdentifierType.MULTIPOINT ) {
				if ( idNum > 0 ) {
					addMultipoint( gridValue.getValue(), idNum - 1 );
				}
			}
			else if ( miType == ModelIdentifierType.LOADTC ) {
				if ( idNum > 0 ) {
					addLoadTC( gridValue.getValue(), idNum - 1 );
				}
			}
			else if ( miType == ModelIdentifierType.QT_WATER ) {
				qt_water = gridValue.getValue();
			}
			else if ( miType == ModelIdentifierType.QT_35 ) {
				qt_35 = gridValue.getValue();
			}
			else if ( miType == ModelIdentifierType.QT_16 ) {
				qt_16 = gridValue.getValue();
			}
		}
	}

	void addMultipoint( String value, int ordinal ) {
		BasicTableMultipoint btm = new BasicTableMultipoint( value, ordinal, 0 );
		multipoints.add( btm );
	}

	void addLoadTC( String value, int ordinal ) {
		//logger.info( "ADDING " + value );
		BasicTableLoadTC tlc = new BasicTableLoadTC( value, ordinal, 0 );
		loadtcs.add( tlc );
	}

	void save( Connection conn ) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"INSERT INTO furnace_base(ts,selected,tc,qt_water,qt_35,qt_16,which) " +
					"VALUES(?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS );

			ps.setTimestamp( 1, sqlts );
			ps.setInt( 2, selected );
			ps.setString( 3, tc );
			ps.setString( 4, qt_water );
			ps.setString( 5, qt_35 );
			ps.setString( 6, qt_16 );
			ps.setInt( 7, which );

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if ( rs.next() ) {
				id = rs.getInt( 1 );
			}

			if ( id == 0 ) {
				throw new Exception( "Can't insert to furnace_base" );
			}

			for ( BasicTableMultipoint tm : multipoints ) {
				tm.setFBID( id );
				tm.save( conn );
			}

			for ( BasicTableLoadTC tlc : loadtcs ) {
				tlc.setFBID( id );
				tlc.save( conn );
			}
		}
		finally {
			try { if (ps != null) ps.close(); } catch(Exception e) { }
		}
	}
}
