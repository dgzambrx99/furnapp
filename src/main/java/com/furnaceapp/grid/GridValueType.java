package com.furnaceapp.grid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import org.apache.log4j.*;

public enum GridValueType {
	TYPE_STRING,
	TYPE_IDENTIFIER,
	TYPE_NUMBER,
	TYPE_DATETIME,
	TYPE_DATE,
	TYPE_TIME;
}
