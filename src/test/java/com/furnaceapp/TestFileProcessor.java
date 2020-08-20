package com.furnaceapp;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import org.apache.log4j.*;
import static java.nio.file.StandardWatchEventKinds.*;
import com.furnaceapp.ConfigFile;
import com.furnaceapp.grid.*;
import com.furnaceapp.db.*;
import com.furnaceapp.checktable.*;
import com.furnaceapp.file.*;
import com.furnaceapp.excel.*;
import com.furnaceapp.model.*;
import com.furnaceapp.model.basic.*;
import junit.framework.TestCase;

public class TestFileProcessor extends TestCase {
	private static Logger logger = Logger.getLogger( TestFileProcessor.class );

	public void testHashtableGrid() throws Exception {
		try {
			ConfigFile config = new ConfigFile();
			IModelFactory modelFactory = new BasicModelFactory();
			CheckTableFactory checkTableFactory = new CheckTableFactory( config );
			ICheckTable checkTable = checkTableFactory.createCheckTable();
			DBConnection dbConnection = new DBConnection( config );

			FileProcessor fileProcessor = new FileProcessor(
												modelFactory,
												checkTable,
												dbConnection,
												1,
												"" );

			Path path = Paths.get( "/home/zapp/projects/htdocs/xlstodb/commandtest/src/test/resources/icebox.xlsx" );

			fileProcessor.processFile( ENTRY_CREATE, path );

			logger.info( "processed" );
		}
		catch ( Exception e ) {
			logger.error( "Main caught", e );
		}
	}
}


