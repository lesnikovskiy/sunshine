package com.fewpeople.sunshine.test.java;

import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import data.WeatherContract.LocationEntry;
import data.WeatherContract.WeatherEntry;
import data.WeatherDbHelper;

public class TestDbClass extends AndroidTestCase {
	private static final String LOG_TAG = TestDbClass.class.getSimpleName();
	
	public void testCreateDb() throws Throwable {
		mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
		SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
		assertEquals(true, db.isOpen());
		db.close();
	}
	
	public void testInsertReadDb() {		
		WeatherDbHelper dbHelper =
				new WeatherDbHelper(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = getLocationContentValues();
		
		long locationRowId;
		locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);
		
		assertTrue(locationRowId != -1);
		Log.d(LOG_TAG, "New row id: " + locationRowId);	
		
			
		Cursor cursor = db.query(LocationEntry.TABLE_NAME, null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			validateCursor(values, cursor);
			
			ContentValues weatherValues = getWeatherContentValues(locationRowId);
			
			long weatherRowId;
			weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
			assertTrue(weatherRowId != -1);
			
			Cursor weatherCursor = db.query(WeatherEntry.TABLE_NAME, null, null, null, null, null, null);
			
			if (weatherCursor.moveToFirst()) {
				validateCursor(weatherValues, weatherCursor);				
			} else {
				fail("No values returned");
			}
		} else {
			fail("No values returned :(");
		}		
	}
	
	private static final String TEST_CITY_NAME = "North Pole";
	
	private static ContentValues getLocationContentValues() {
		String testLocationSetting = "99705";
		double testLatitude = 64.772;
		double testLongitude = -147.355;
		
		ContentValues values = new ContentValues();
		values.put(LocationEntry.COLUMN_CITY_NAME, TEST_CITY_NAME);
		values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
		values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
		values.put(LocationEntry.COLUMN_COORD_LON, testLongitude);
		
		return values;
	}
	
	private static ContentValues getWeatherContentValues(long locationRowId) {
		ContentValues weatherValues = new ContentValues();
		weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
		weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
		weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
		weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
		weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
		weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
		weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
		weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
		weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
		weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);
		
		return weatherValues;
	}
	
	private static void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
		Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
		
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int index = valueCursor.getColumnIndex(columnName);
			assertFalse(index == -1);
			
			String expectedValue = entry.getValue().toString();
			assertEquals(expectedValue, valueCursor.getString(index));
		}
	}
}
