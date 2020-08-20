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

public interface ICheckTable {
	public boolean exists( Connection conn, String hash ) throws Exception;

		//	returns 1 if inserted, 2 if updated
	public int upsert( Connection conn, String filename, String filepath, String hash ) throws Exception;
}
