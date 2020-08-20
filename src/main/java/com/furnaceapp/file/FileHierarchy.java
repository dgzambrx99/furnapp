package com.furnaceapp.file;

import java.io.File;
import org.apache.log4j.*;

public class FileHierarchy {
	private static Logger logger = Logger.getLogger( FileHierarchy.class );

	private int folderNumber, parNumber, parParNumber;

	public FileHierarchy( File file ) {
		folderNumber = parNumber = parParNumber = -1;

		try {
			parNumber = Integer.parseInt( file.getParentFile().getName() );
		}
		catch ( Exception e2 ) {
		}

		try {
			parParNumber = Integer.parseInt( file.getParentFile().getParentFile().getName() );
		}
		catch ( Exception e ) {
		}

		if ( parParNumber > 0 ) {
			folderNumber = parParNumber;
		}
		else if ( parNumber > 0 ) {
			folderNumber = parNumber;
		}

		//logger.info( "for " + file + ", folder number is " + folderNumber + ", parParNumber is " + parParNumber + ", parNumber is " + parNumber );
	}

	public int getFolderNumber() {
		return folderNumber;
	}

	public int getParNumber() {
		return parNumber;
	}

	public int getParParNumber() {
		return parParNumber;
	}
}

