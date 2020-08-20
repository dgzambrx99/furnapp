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

class BasicTableIceboxBase {
	private static Logger logger = Logger.getLogger( BasicTableIceboxBase.class );

	private ArrayList<BasicTableIceboxValue> values = new ArrayList<BasicTableIceboxValue>();
	private IGrid grid;
	private java.sql.Timestamp sqlts;
	private DateTimeParser dateTimeParser;
	private int id = 0, ts = 0, selected = 0, which = 0;

	BasicTableIceboxBase( IGrid g, TreeMap<Integer,ModelIdentifier> readingsMapping, DateTimeParser dateTimeParser, int w ) throws Exception {
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

			if ( miType == ModelIdentifierType.ICEBOX ) {
				if ( idNum > 0 ) {
					addValue( gridValue.getValue(), idNum - 1 );
				}
			}
		}
	}

	void addValue( String value, int ordinal ) {
		BasicTableIceboxValue btm = new BasicTableIceboxValue( value, ordinal, 0 );
		values.add( btm );
	}

	void save( Connection conn ) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"INSERT INTO icebox_base(ts,selected,which) " +
					"VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS );

			ps.setTimestamp( 1, sqlts );
			ps.setInt( 2, selected );
			ps.setInt( 3, which );

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			if ( rs.next() ) {
				id = rs.getInt( 1 );
			}

			if ( id == 0 ) {
				throw new Exception( "Can't insert to icebox_base" );
			}

			for ( BasicTableIceboxValue tm : values ) {
				tm.setFBID( id );
				tm.save( conn );
			}
		}
		finally {
			try { if (ps != null) ps.close(); } catch(Exception e) { }
		}
	}
}
