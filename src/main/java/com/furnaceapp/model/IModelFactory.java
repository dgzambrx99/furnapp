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
import com.furnaceapp.grid.IGrid;
import org.apache.log4j.*;

public interface IModelFactory {
	IModel createModel( ModelType mt ) throws Exception;
	IModel createModelFromGrid( IGrid grid ) throws Exception;
}
