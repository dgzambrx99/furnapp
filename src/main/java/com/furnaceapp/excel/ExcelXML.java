package com.furnaceapp.excel;

import java.util.*;
import org.apache.log4j.*;

class ExcelXML {
	class Holder {
		private String value;
		private boolean bUsesSharedStrings;

		Holder( String val, boolean b ) {
			value = val;
			bUsesSharedStrings = b;
		}

		String getValue() { return value; }
		boolean usesSharedStrings() { return bUsesSharedStrings; }

		public String toString() {
			if ( bUsesSharedStrings ) {
				return value + "(S)";
			}
			else {
				return value;
			}
		}
	}

	private static Logger logger = Logger.getLogger( ExcelXML.class );
	TreeMap<String, TreeMap<String, Holder>> rawValues;
	TreeMap<String, TreeMap<String, String>> processedValues;
	String dataColumns[] = { "A", "B", "C", "D", "F", "H", "J", "L", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public ExcelXML() {
		rawValues = new TreeMap<String, TreeMap<String, Holder>>();
	}

	public TreeMap<String, TreeMap<String, String>> getProcessedValues() {
		return processedValues;
	}

	public void set( String row, String col, String val, boolean bUsesSharedStrings ) {
		TreeMap testRow = rawValues.get( row );
		if ( testRow == null ) {
			testRow = new TreeMap<String, Holder>();
			rawValues.put( row, testRow );
		}
		Holder holder = new Holder( val, bUsesSharedStrings );
		testRow.put( col, holder );
	}

	private TreeMap<String, String> convertRowFromRawToProcessed( TreeMap<String, Holder> tempRawRow, SharedStrings sharedStrings ) throws Exception {
		TreeMap<String, String> ret = new TreeMap<String, String>();
		for ( Map.Entry<String, Holder> entry : tempRawRow.entrySet() ) {
			String newValue;
			String key = entry.getKey();
			Holder holder = entry.getValue();
			String holderValue = holder.getValue();
			if ( holder.usesSharedStrings() ) {
				if ( sharedStrings == null || !sharedStrings.has( holderValue ) ) {
					throw new Exception( "Holder uses shared strings, but " + holderValue + " not found or ss is null " + sharedStrings );
				}

				newValue = sharedStrings.translate( holderValue );
			}
			else {
				newValue = holderValue;
			}

			ret.put( key, newValue );
		}

		return ret;
	}

	public void finish( boolean returnAllValidRows, SharedStrings sharedStrings ) throws Exception {
		TreeMap<String, Holder> tempRawRow;
		TreeMap<String, String> tempProcessedRow;

		processedValues = new TreeMap<String, TreeMap<String, String>>( new NumericStringComparator() );
		List<String> sortedKeys = new ArrayList<String>( rawValues.keySet() );
		Collections.sort( sortedKeys, new NumericStringComparator() );
		for ( String key : sortedKeys ) {
			tempRawRow = rawValues.get( key );
			//logger.info( "tempRawRow=" + tempRawRow );
			if ( validate( tempRawRow, sharedStrings ) ) {
				tempProcessedRow = convertRowFromRawToProcessed( tempRawRow, sharedStrings );
				//logger.info( "tempProcessedRow=" + tempProcessedRow );
				processedValues.put( key, tempProcessedRow );
				if ( !returnAllValidRows ) {
					return;
				}
			}
		}
	}

	private boolean validate( TreeMap<String, Holder> map, SharedStrings sharedStrings ) {
/*	5/15/16: commenting out since not all columns will have values if we read the whole file
		for ( int i = 0; i < dataColumns.length; i++ ) {
			if ( map.get( dataColumns[ i ] ) == null ) {
				logger.info( "NOT VALIDATING BECAUSE " + dataColumns[ i ] + " IS NULL" );
				return false;
			}
		}
*/

		Holder aaHolder = map.get( "A" ), bbHolder = map.get( "B" );
		if ( aaHolder != null && bbHolder != null ) {
			String aa = aaHolder.getValue();
			String bb = bbHolder.getValue();
			if ( aa != null && bb != null ) {
				if ( sharedStrings != null ) {
					if ( !sharedStrings.has( aa ) || !sharedStrings.has( bb ) ) {
						return false;
					}

					aa = sharedStrings.translate( aa );
					bb = sharedStrings.translate( bb );
				}

				//logger.info( "validate - checking " + aa + " and " + bb );
				if ( aa.indexOf( "/" ) < 0 || bb.indexOf( ":" ) < 0 ) {
					return false;
				}
			}
		}

		return true;
	}
}

class NumericStringComparator implements Comparator<String> {
	@Override
	public int compare( String str1, String str2 ) {
		try {
			return Integer.parseInt( str1 ) - Integer.parseInt( str2 );
		}
		catch ( Exception e ) {
			return 0;
		}
	}
}
