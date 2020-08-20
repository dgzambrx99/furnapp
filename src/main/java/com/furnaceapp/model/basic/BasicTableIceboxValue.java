package com.furnaceapp.model.basic;

import java.io.*;
import java.util.*;
import java.sql.*;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import org.apache.log4j.*;

class BasicTableIceboxValue {
	private static Logger logger = Logger.getLogger( BasicTableIceboxValue.class );

	private String data_value;
	private int data_order, fbid;

	BasicTableIceboxValue( String d, int o, int f ) {
		data_value = d;
		data_order = o;
		fbid = f;
	}

	void setFBID( int f ) {
		fbid = f;
	}

	int save( Connection conn ) throws Exception {
		PreparedStatement ps = null;
		int rowsAffected;

		try {
			ps = conn.prepareStatement( "INSERT INTO iceboxdata_value( fbid, data_value, data_order ) " +
											" VALUES( ?,?,? )" );
			ps.setInt( 1, fbid );
			ps.setString( 2, data_value );
			ps.setInt( 3, data_order );
			rowsAffected = ps.executeUpdate();
			return rowsAffected == 1 ? 1 : 0;
		}
		finally {
			try { if (ps != null) ps.close(); } catch(Exception e) { }
		}
	}
}
