package com.furnaceapp;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

public interface IDirectoryEventCallback {
	public void processFile( WatchEvent.Kind kind, Path p ) throws Exception;
}
