package com.furnaceapp.checktable;

import java.io.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.swing.*;
import java.util.Properties;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.codec.digest.DigestUtils;

class CheckTableMSSQL implements ICheckTable {
	public CheckTableMSSQL() {
	}

	public boolean exists( Connection conn, String hash ) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement( "SELECT * FROM checktable WHERE hash=?" );
			ps.setString( 1, hash );

			rs = ps.executeQuery();
			return rs.next();
		}
		finally {
			try { if (rs != null) rs.close(); } catch(Exception e) { }
			try { if (ps != null) ps.close(); } catch(Exception e) { }
		}
	}

		//	returns 0 if failed, 1 if inserted, 2 if updated
	public int upsert( Connection conn, String filename, String filepath, String hash ) throws Exception {
		PreparedStatement ps = null, ps2 = null;
		int rowsAffected;

		try {
			int curTime = (int) ( System.currentTimeMillis() / 1000 );

			ps = conn.prepareStatement( "UPDATE checktable SET mtime=? WHERE hash=?" );
			ps.setInt( 1, curTime );
			ps.setString( 2, hash );
			rowsAffected = ps.executeUpdate();
			if ( rowsAffected > 0 ) {
				return 2;
			}

			ps2 = conn.prepareStatement( "INSERT INTO checktable( filename, filepath, hash, mtime ) " +
											" VALUES( ?,?,?,? )" );
			ps2.setString( 1, filename );
			ps2.setString( 2, filepath );
			ps2.setString( 3, hash );
			ps2.setInt( 4, curTime );
			rowsAffected = ps2.executeUpdate();
			return rowsAffected == 1 ? 1 : 0;
		}
		finally {
			try { if (ps != null) ps.close(); } catch(Exception e) { }
			try { if (ps2 != null) ps2.close(); } catch(Exception e) { }
		}
	}

/*
		//	returns 1 if inserted, 2 if updated
	public static int upsert( Connection conn, String filename, String filepath, String hash ) throws Exception {
		PreparedStatement ps = null;

		try {
			int curTime = (int) ( System.currentTimeMillis() / 1000 );

			ps = conn.prepareStatement(
				"INSERT INTO checktable(filename,filepath,hash,mtime) " +
				"VALUES(?,?,?,?) " +
				"ON DUPLICATE KEY UPDATE " +
				"mtime=?" );
			ps.setString( 1, filename );
			ps.setString( 2, filepath );
			ps.setString( 3, hash );
			ps.setInt( 4, curTime );
			ps.setInt( 5, curTime );
			return ps.executeUpdate();
		}
		finally {
			try { if (ps != null) ps.close(); } catch(Exception e) { }
		}
	}
*/
}
