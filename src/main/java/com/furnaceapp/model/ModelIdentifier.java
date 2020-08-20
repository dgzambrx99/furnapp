package com.furnaceapp.model;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;

public class ModelIdentifier {
	private static Logger logger = Logger.getLogger( ModelIdentifier.class );

	private ModelIdentifierType type;
	private GridValue gridValue;

	public ModelIdentifier( GridValue gv ) {
		gridValue = gv;

		type = ModelIdentifierType.UNKNOWN;

		for ( ModelIdentifierType mit : ModelIdentifierType.values() ) {
			if ( mit.matches( gridValue ) ) {
				type = mit;
				break;
			}
		}
	}

	public GridValue getGridValue() {
		return gridValue;
	}

	public ModelIdentifierType getType() {
		return type;
	}

	public String toString() {
		return "type=" + type.name() + " for gridValue: " + gridValue;
	}
}

/*

		char cc;

		input = s;

		idName = s;
		idNum = -1;
		type = ModelIdentifierType.UNKNOWN;

		if ( s == null ) {
			idName = "";
			return;
		}

		if ( s.length() < 2 ) {
			return;
		}

		String[] split = s.split( " " );
		if ( split == null || split.length < 1 ) {
			return;
		}

		if ( split.length == 1 ) {
			cc = s.charAt( s.length() - 1 );
			if ( cc >= '0' && cc <= '9' ) {
				s = s.substring( 0, s.length() - 1 );
				split = new String[]{ s, String.valueOf( cc ) };
			}
		}

		if ( split.length == 1 ) {
			return;
		}

		idName = split[ 0 ];

		cc = split[ 1 ].charAt( 0 );
		if ( cc >= 'A' && cc <= 'Z' ) {
			idNum = cc - 'A';
		}
		else {
			try {
				idNum = Integer.parseInt( split[ 1 ] );
			}
			catch ( Exception e ) {
			}
		}
*/
