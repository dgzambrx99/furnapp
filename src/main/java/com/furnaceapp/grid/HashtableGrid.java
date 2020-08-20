package com.furnaceapp.grid;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.security.*;
import java.io.FileInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.xml.sax.InputSource;
import org.apache.log4j.*;

public class HashtableGrid implements IGrid {
	private static Logger logger = Logger.getLogger( HashtableGrid.class );
	private TreeMap<String, TreeMap<String, String>> hash;
	private int currentRow, maxRow;

	public HashtableGrid( TreeMap<String, TreeMap<String, String>> h ) {
		hash = h;
		currentRow = 1;
		maxRow = 0;
		try {
			String temp = hash.lastKey();
			maxRow = Integer.parseInt( temp );
		}
		catch ( Exception e ) {
		}
		//logger.info( "maxRow=" + maxRow );
	}

	public int setCurrentRow( int row ) {
		if ( row < 1 || row > maxRow ) {
			return 0;
		}

		currentRow = row;

		return currentRow;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public GridValue getColumnValue( int col ) {
		String temp = getValueAt( currentRow, col );

		return temp == null ? null : new GridValue( temp );
	}

	public boolean setColumnValue( int col, GridValue val ) {
		return true;
	}

	public boolean columnContains( int col, GridValue val ) {
		return true;
	}

	public int find( GridValue val, int startingCol ) {
		String stringVal = val.getValue();
		boolean bFirstRow = true;

		for ( int i = currentRow; i < maxRow; i++ ) {
			TreeMap<String, String> row = hash.get( "" + i );
			if ( row != null && row.containsValue( stringVal ) ) {
				int col = 0;
				for ( Map.Entry<String, String> entry : row.entrySet() ) {
					col++;
					if ( bFirstRow && startingCol != 0 && col < startingCol ) {
						//logger.info( "continuing with col=" + col );
						continue;
					}
					//logger.info( "checking with col=" + col );
					if ( stringVal.equals( entry.getValue() ) ) {
						currentRow = i;
						return col;
					}
				}
			}
			bFirstRow = false;
		}

		return 0;
	}

	public boolean columnIsEmpty( int col ) {
		return true;
	}

	public boolean rowIsEmpty() {
		return true;
	}

	public int findColumnContaining( int col, GridValue val ) {
		return 0;
	}

	public int findRowContaining( GridValue val ) {
		return 0;
	}

	private String getValueAt( int row, int col ) {
		if ( row < 1 || row > maxRow ) {
			return null;
		}

		String rowIndex = String.valueOf( row );
		TreeMap<String,String> testRow = hash.get( rowIndex );
		if ( testRow == null ) {
			return null;
		}

		String colIndex = columnIndexIntToString( col );
		if ( colIndex == null ) {
			return null;
		}

		return testRow.get( colIndex );
	}

	private String columnIndexIntToString( int col ) {
		if ( col < 1 || col > 26 ) {
			return null;
		}

		char cc = (char) ( 'A' + ( col - 1 ) );

		return String.valueOf( cc );
	}

	private int columnIndexStringToInt( String col ) {
		int ret = col.charAt( 0 ) - 'A' + 1;

		if ( ret >= 1 && ret <= 26 ) {
			return ret;
		}

		return 1;
	}
}
