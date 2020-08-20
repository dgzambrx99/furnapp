package com.furnaceapp.db;

import java.io.File;
import java.util.Date;
import java.sql.*;
import javax.sql.DataSource;
import java.util.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.*;
import com.mchange.v2.c3p0.*;
import com.furnaceapp.ConfigFile;

public class DBConnection {
	private static Logger logger = Logger.getLogger( DBConnection.class );
	private ComboPooledDataSource cpds;
	private BasicDataSource dataSource;
	private ConfigFile config;
	private boolean bUsingMSSQL;

	public DBConnection( ConfigFile cf ) throws Exception {
		config = cf;

		if ( "mssql".equals( config.getString( "furnaceapp.db_use_mysql_or_mssql" ) ) ) {
			logger.info( "USING MSSQL" );
			bUsingMSSQL = true;
			createDatabasePoolC3P0();
		}
		else {
			logger.info( "USING MYSQL" );
			bUsingMSSQL = false;
			createDatabasePool();
		}
	}

	public Connection getConnection() throws SQLException {
		if ( bUsingMSSQL ) {
			return cpds.getConnection();
		}
		else {
			return dataSource.getConnection();
		}
	}

	private void createDatabasePoolC3P0() throws Exception {
		cpds = new ComboPooledDataSource();

		String driverClassName = config.getString( "furnaceapp.db_driver_class_name.mssql" );
		String username = config.getString( "furnaceapp.db_username.mssql" );
		String password = config.getString( "furnaceapp.db_password.mssql" );
		String connectionUrl = config.getString( "furnaceapp.db_connection_url.mssql" );
logger.info( "USING " + driverClassName + " " + connectionUrl );
		cpds.setDriverClass( driverClassName );
		cpds.setJdbcUrl( connectionUrl );
		cpds.setUser( username );
		cpds.setPassword( password );
	}

	private void createDatabasePool() throws Exception {
		dataSource = new BasicDataSource();

		String driverClassName = config.getString( "furnaceapp.db_driver_class_name.mysql" );
		String username = config.getString( "furnaceapp.db_username.mysql" );
		String password = config.getString( "furnaceapp.db_password.mysql" );
		String connectionUrl = config.getString( "furnaceapp.db_connection_url.mysql" );

		dataSource.setDriverClassName( driverClassName );
		dataSource.setUsername( username );
		dataSource.setPassword( password );
		dataSource.setUrl( connectionUrl );
	}
}
