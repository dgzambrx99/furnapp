package com.furnaceapp.model;

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
import com.furnaceapp.grid.GridValue;
import com.furnaceapp.grid.GridValueType;

public enum ModelIdentifierType {
	UNKNOWN {
		@Override
		public boolean matches( GridValue gv ) {
			return false;
		}
	},
	READING_DATE_FULL {
		@Override
		public boolean matches( GridValue gv ) {
			return gv != null && gv.getType() == GridValueType.TYPE_DATETIME;
		}
	},
	READING_DATE_DATE_ONLY {
		@Override
		public boolean matches( GridValue gv ) {
			return gv != null && gv.getType() == GridValueType.TYPE_DATE;
		}
	},
	READING_DATE_TIME_ONLY {
		@Override
		public boolean matches( GridValue gv ) {
			return gv != null && gv.getType() == GridValueType.TYPE_TIME;
		}
	},
	ICEBOX {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_IDENTIFIER ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getIDName(), "ICEBOX", "ICE BOX" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	CONTROL {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_STRING ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getValue(), "CONTROL TC", "TC" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	MULTIPOINT {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_IDENTIFIER ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getIDName(), "MULTIPOINT", "MP" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	LOADTC {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_IDENTIFIER ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getIDName(), "LOAD TC", "LC" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	QT_WATER {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_STRING ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getValue(), "QUENCH TANK", "QT WATER", "QUENCHTANK", "QT" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	QT_35 {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_STRING ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getValue(), "QT 35%", "QT 35", "QT35%" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	},
	QT_16 {
		@Override
		public boolean matches( GridValue gv ) {
			if ( gv == null ) {
				return false;
			}

			if ( gv.getType() != GridValueType.TYPE_STRING ) {
				return false;
			}

			boolean bMatches = compareMultiple( gv.getValue(), "QT 16%", "QT 16", "QT16%" );
			if ( !bMatches ) {
				return false;
			}

			return true;
		}
	};

	private static Logger logger = Logger.getLogger( ModelIdentifierType.class );

	public abstract boolean matches( GridValue gv );

	private static boolean compareMultiple( String toCheck, String value1, String ... values ) {
		if ( toCheck.equalsIgnoreCase( value1 ) ) {
			return true;
		}

		for ( int i = 0; i < values.length; i++ ) {
			if ( toCheck.equals( values[ i ] ) ) {
				return true;
			}
		}

		return false;
	}
}

