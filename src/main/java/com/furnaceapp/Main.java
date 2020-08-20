package com.furnaceapp;

import java.io.File;
import java.util.Date;
import java.sql.*;
import javax.sql.DataSource;
import javax.swing.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.codec.digest.DigestUtils;
import com.furnaceapp.grid.*;
import com.furnaceapp.model.*;
import com.furnaceapp.model.basic.BasicModelFactory;
import com.furnaceapp.checktable.ICheckTable;
import com.furnaceapp.checktable.CheckTableFactory;
import com.furnaceapp.db.*;
import com.furnaceapp.file.FileHierarchy;
import com.furnaceapp.excel.ExcelFile;
import org.apache.log4j.*;

public class Main {
	private static Logger logger = Logger.getLogger( Main.class );
	private IModelFactory modelFactory;
	private ICheckTable checkTable;
	private DBConnection dbConnection;
	private ConfigFile config;
	private int delayBetweenLoops;
	private String baseDir;

	public Main() {
		try {
			config = new ConfigFile();
			modelFactory = new BasicModelFactory();
			CheckTableFactory checkTableFactory = new CheckTableFactory( config );
			checkTable = checkTableFactory.createCheckTable();
			dbConnection = new DBConnection( config );
			delayBetweenLoops = config.getInt( "furnaceapp.delay_between_loops" );
			baseDir = config.getString( "furnaceapp.base_dir" );

			FileProcessor fileProcessor = new FileProcessor(
												modelFactory,
												checkTable,
												dbConnection,
												delayBetweenLoops,
												baseDir );

			Path dir = Paths.get( baseDir ).toAbsolutePath();

			new WatchDir( dir, true ).processEvents( fileProcessor );

			logger.info( "Monitoring " + baseDir );
		}
		catch ( Exception e ) {
			logger.error( "Main caught", e );
		}
	}

	public static void main(String[] args) throws Exception {
		new Main();
	}
}


