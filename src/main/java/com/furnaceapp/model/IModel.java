package com.furnaceapp.model;

import java.sql.Connection;
import com.furnaceapp.grid.IGrid;
import com.furnaceapp.grid.GridValue;
import java.util.Date;

public interface IModel {
	public void loadReadingMapping( IGrid grid ) throws Exception;
	public void setReadingMapping( int column, ModelIdentifier mi ) throws Exception;
	public void loadReadings( IGrid grid, int maxRows, int which ) throws Exception;
	public int getReadingsCount();

	public void setValue( String key, GridValue value ) throws Exception;
	public void setValue( String key, String value ) throws Exception;
	public void setValue( String key, Date value ) throws Exception;
	public void setValue( String key, float value ) throws Exception;
	public void setValue( String key, int value ) throws Exception;
	public void setValue( String key, GridValue[] value ) throws Exception;
	public void setValue( String key, String[] value ) throws Exception;
	public void setValue( String key, Date[] value ) throws Exception;
	public void setValue( String key, float[] value ) throws Exception;
	public void setValue( String key, int[] value ) throws Exception;
	public void save( Connection conn ) throws Exception;
}
