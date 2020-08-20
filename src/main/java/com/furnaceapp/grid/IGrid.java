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

public interface IGrid {
	int setCurrentRow( int row );
	int getCurrentRow();
	GridValue getColumnValue( int col );
	boolean setColumnValue( int col, GridValue val );
	boolean columnContains( int col, GridValue val );
	int find( GridValue val, int startingCol );
	boolean columnIsEmpty( int col );
	boolean rowIsEmpty();
	int findColumnContaining( int col, GridValue val );
	int findRowContaining( GridValue val );

/*
	boolean setModelType( ModelType mt );
	boolean buildReadingsMappingFromCurrentRow();
	boolean addReadingsMapping( int col, ModelMappingType mmt );

	boolean moveTo( int row, int col );
	GridValue getCurrentValue();
	boolean setCurrentValue( GridValue val );
	int getCurrentColumn();
	int setCurrentColumn( int col );
*/
}
