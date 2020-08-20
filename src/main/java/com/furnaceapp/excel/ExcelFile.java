package com.furnaceapp.excel;

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

public class ExcelFile {
	private static Logger logger = Logger.getLogger( ExcelFile.class );
	ExcelXML excelXML = null;
	SharedStrings sharedStrings = null;
	String hash = null;

//http://www.codeproject.com/Articles/208075/How-to-read-and-write-xlsx-Excel-file-Part-I
//https://github.com/plutext/docx4j

	public static void main(String[] args) throws Exception {
		try {
			//File f = new File( "furnace1-0017.xlsx" );
			File f = new File( "furnace1-001-GX20-1sec-1124.xlsx" );
			ExcelFile ef = new ExcelFile( f, true );
			//System.out.println( ef.getProcessedValues() );
		}
		catch ( Exception e ) {
			logger.error( e );
			throw e;
		}
	}

	public ExcelFile( File f, boolean bUseAllRowsOrJustOne ) throws Exception {
		//logger.info( "reading " + f.getName() );

		readFile( new FileInputStream( f ), bUseAllRowsOrJustOne );
	}

	public ExcelFile( InputStream is, boolean bUseAllRowsOrJustOne ) throws Exception {
		readFile( is, bUseAllRowsOrJustOne );
	}

	protected void readFile( InputStream is, boolean bUseAllRowsOrJustOne ) throws Exception {
		ZipInputStream zis = new ZipInputStream( is );

		ZipEntry entry = zis.getNextEntry();
		while ( entry != null ) {
			String name = entry.getName();
			//System.out.println( name );
			if ( name.equals( "xl/worksheets/sheet1.xml" ) ) {
				String theString = IOUtils.toString( zis, "UTF-8" );
				excelXML = readXMLDoc( theString, bUseAllRowsOrJustOne );
				hash = DigestUtils.md5Hex( theString );
				zis.closeEntry();
			}

			if ( name.equals( "xl/sharedStrings.xml" ) ) {
				String theString = IOUtils.toString( zis, "UTF-8" );
				sharedStrings = new SharedStrings( theString );
				zis.closeEntry();
			}

			zis.closeEntry();
			entry = zis.getNextEntry();
		}

		is.close();
	}

	public TreeMap<String, TreeMap<String, String>> getProcessedValues() {
		return excelXML.getProcessedValues();
	}

/*
    <row r="23">
      <c r="A23" s="14" t="inlineStr">
        <is>
          <t xml:space="preserve">Stopped By</t>
        </is>
      </c>
    </row>

   <row r="29" spans="1:5" x14ac:dyDescent="0.25">
      <c r="A29" s="3" t="s">
        <v>33</v>
      </c>
    </row>
*/

	private ExcelXML readXMLDoc( String s, boolean bUseAllRowsOrJustOne ) throws Exception {
		ExcelXML ret = new ExcelXML();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource( new StringReader( s ) );
		Document doc = builder.parse(is);

		NodeList rows = doc.getElementsByTagName( "row" );
		for ( int i = 0; i < rows.getLength(); i++ ) {
			Element row = (Element) rows.item( i );
			String rowNum = row.getAttribute( "r" );
			//logger.info( "rowNum=" + rowNum );
			int rowNumLen = rowNum.length();

			NodeList cols = row.getElementsByTagName( "c" );
			for ( int j = 0; j < cols.getLength(); j++ ) {
				Element value = (Element) cols.item( j );
				String colNum = value.getAttribute( "r" );
				String sharedStringType = value.getAttribute( "t" );
				String colName = colNum.substring( 0, colNum.length() - rowNumLen );
				String val = value.getTextContent();

				NodeList directValues = value.getElementsByTagName( "is" );
				NodeList indirectValues = value.getElementsByTagName( "v" );

				boolean bUsesSharedStrings;
				if ( directValues.getLength() > 0 ) {
					bUsesSharedStrings = false;
				}
				else if ( indirectValues.getLength() > 0 && ( sharedStringType == null || !sharedStringType.equals( "s" ) ) ) {
					bUsesSharedStrings = false;
				}
				else if ( "s".equals( sharedStringType ) && indirectValues.getLength() > 0 ) {
					bUsesSharedStrings = true;
				}
				else {
						//	some cells are empty: "<c r="C1" s="3"/>"
					val = "";
					bUsesSharedStrings = false;
					//throw new IllegalArgumentException( "Neither direct nor indirect value at row " + rowNum + ", col " + colName + ", val=" + val );
				}

				//logger.info( "putting " + val + " at " + rowNum + " x " + colName );
				ret.set( rowNum, colName, val, bUsesSharedStrings );
			}
		}

		ret.finish( bUseAllRowsOrJustOne, sharedStrings );

		return ret;
	}

	public String getHash() { return hash; }
}

