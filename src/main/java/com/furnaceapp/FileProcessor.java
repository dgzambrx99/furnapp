package com.furnaceapp;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import com.furnaceapp.grid.*;
import com.furnaceapp.model.*;
import com.furnaceapp.checktable.ICheckTable;
import com.furnaceapp.db.*;
import com.furnaceapp.file.FileHierarchy;
import com.furnaceapp.excel.ExcelFile;
import org.apache.log4j.*;

class FileProcessor implements IDirectoryEventCallback {
	private static Logger logger = Logger.getLogger( FileProcessor.class );
	private IModelFactory modelFactory;
	private ICheckTable checkTable;
	private DBConnection dbConnection;
	private int delayBetweenLoops;
	private String baseDir;

	//	MODIFY THIS TO DECIDE IF THE GRID IS A FURNACE OR AN ICEBOX AND TO LOCATE THE READINGS
	IModel processGrid( IGrid grid, FileHierarchy fileHierarchy ) throws Exception {
		int maxRowsToProcess = 100;

		grid.find( new GridValue( "Tag Index" ), 1 );
		grid.setCurrentRow( grid.getCurrentRow() + 2 );

		//IModel model = modelFactory.createModel( ModelType.FURNACE );
		IModel model = modelFactory.createModelFromGrid( grid );
		model.loadReadingMapping( grid );
		//logger.info( "model=" + model );
		model.setReadingMapping( 1, new ModelIdentifier( new GridValue( "2016/01/29" ) ) );
		model.setReadingMapping( 2, new ModelIdentifier( new GridValue( "15:02:29" ) ) );

		grid.setCurrentRow( grid.getCurrentRow() + 2 );

		model.loadReadings( grid, maxRowsToProcess, fileHierarchy.getFolderNumber() );

		return model;
	}

	FileProcessor( IModelFactory imf, ICheckTable ct, DBConnection dbc, int del, String base ) {
		modelFactory = imf;
		checkTable = ct;
		dbConnection = dbc;
		delayBetweenLoops = del;
		baseDir = base;
	}

	public void processFile( WatchEvent.Kind kind, Path path ) throws Exception {
		if ( !( kind == ENTRY_CREATE || kind == ENTRY_MODIFY ) ) {
			//logger.info( "Not processing because not create or modify " + kind );
			return;
		}

		if ( !path.toString().toLowerCase().endsWith( ".xlsx" ) ) {
			//logger.info( "Not processing because not xlsx" );
			return;
		}

		long startTime = System.currentTimeMillis();

		Connection conn = null;

		try {
			logger.info( "getting connection" );
			conn = dbConnection.getConnection();
			logger.info( "got connection" );

			File file = path.toFile();
			logger.info( "about to process " + file );
			long fileLen = file.length();
			if ( fileLen < 1000L ) {
				logger.info( "not processing " + path + " because fileLen=" + fileLen );
				return;
			}

			FileHierarchy fileHierarchy = new FileHierarchy( file );
			ExcelFile excelFile = new ExcelFile( file, true );

			if ( checkTable.exists( conn, excelFile.getHash() ) ) {
				logger.info( file.getPath() + " exists in checktable at " + excelFile.getHash() + ", skipping" );
				return;
			}

			//logger.info( "FILE=" + excelFile.getProcessedValues() );
			HashtableGrid grid = new HashtableGrid( excelFile.getProcessedValues() );

			IModel model = processGrid( grid, fileHierarchy );
			//logger.info( "model=" + model );

			conn.setAutoCommit( false );
			if ( checkTable.upsert( conn, file.getName(), file.getPath(), excelFile.getHash() ) == 1 ) {
				model.save( conn );
				conn.commit();
				logger.info( "successfully inserted " + model.getReadingsCount() + " rows from " + file.getName() );
			}
			else {
				logger.info( "skipped " + file.getName() + " as a duplicate" );
			}
		}
		finally {
			try { if (conn != null) conn.close(); } catch(Exception e) { }
		}

		long endTime = System.currentTimeMillis();

		logger.info( "processed in " + ( endTime - startTime ) + " milliseconds" );

		if ( delayBetweenLoops > 0 ) {
			pauseFor( delayBetweenLoops );
		}
	}

	private void pauseFor( int millis ) {
		try {
			Thread.sleep( millis );
		}
		catch ( Exception e ) {
		}
	}
}
