package com.furnaceapp.model.basic;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.security.*;
import java.io.FileInputStream;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import com.furnaceapp.model.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.xml.sax.InputSource;
import org.apache.log4j.*;

public class BasicModelFactory implements IModelFactory {
	private static Logger logger = Logger.getLogger( BasicModelFactory.class );

	public IModel createModel( ModelType mt ) throws Exception {
		if ( mt == ModelType.ICEBOX ) {
			return new BasicIceboxModel();
		}
		else if ( mt == ModelType.FURNACE ) {
			return new BasicFurnaceModel();
		}
		else {
			throw new IllegalArgumentException( "bad model type " + mt );
		}
	}

	public IModel createModelFromGrid( IGrid grid ) throws Exception {
		for ( int i = 1; i <= 26; i++ ) {
			GridValue gv = grid.getColumnValue( i );
			ModelIdentifier mi = new ModelIdentifier( gv );
			ModelIdentifierType miType = mi.getType();

			logger.info( "found " + miType + " at " + i );

			if ( miType == ModelIdentifierType.CONTROL ||
					miType == ModelIdentifierType.MULTIPOINT ||
					miType == ModelIdentifierType.LOADTC ||
					miType == ModelIdentifierType.QT_WATER ||
					miType == ModelIdentifierType.QT_35 ||
					miType == ModelIdentifierType.QT_16 ) {
				logger.info( "returning furnace model" );
				return createModel( ModelType.FURNACE );
			}
			else if ( miType == ModelIdentifierType.ICEBOX ) {
				logger.info( "returning icebox model" );
				return createModel( ModelType.ICEBOX );
			}
		}

		throw new IllegalArgumentException( "can't determine model type" );
	}
}
