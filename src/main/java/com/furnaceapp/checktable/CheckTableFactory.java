package com.furnaceapp.checktable;

import java.io.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.swing.*;
import java.util.Properties;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.codec.digest.DigestUtils;
import com.furnaceapp.ConfigFile;

public class CheckTableFactory {
	private boolean bUsingMSSQL;

	public CheckTableFactory( ConfigFile cf ) throws Exception {
		if ( "mssql".equals( cf.getString( "furnaceapp.db_use_mysql_or_mssql" ) ) ) {
			bUsingMSSQL = true;
		}
		else {
			bUsingMSSQL = false;
		}
	}

	public ICheckTable createCheckTable() {
		return bUsingMSSQL ? new CheckTableMSSQL() : new CheckTableMySQL();
	}
}

