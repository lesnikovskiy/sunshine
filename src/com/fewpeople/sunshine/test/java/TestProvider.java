package com.fewpeople.sunshine.test.java;

import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import data.WeatherContract.LocationEntry;
import data.WeatherContract.WeatherEntry;
import data.WeatherDbHelper;

public class TestProvider extends AndroidTestCase {
private static final String LOG_TAG = TestDbClass.class.getSimpleName();
	
	public void testDeleteDb() throws Throwable {
		mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);		
	}
	
	public void testGetType() {
		// content://com.example.android.sunshine.app/weather
		String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
		// vnd.android.cursor.dir/com.example.android.sunshine.app/weather
		assertEquals(WeatherEntry.CONTENT_TYPE, type);
		
		String testLocation = "94074";
		// content://com.example.android.sunshine.app/weather/94074
		type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocation(testLocation));
		//  vnd.android.cursor.dir/com.example.android.sunshine.app/weather
		assertEquals(WeatherEntry.CONTENT_TYPE, type);
		
		String testDate = "20140612";
		// content://com.example.android.sunshine.app/weather/94074/20140612
		type = mContext.getContentResolver().getType(WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
		// vnd.android.cursor.dir/com.example.android.sunshine.app/weather
		assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);
		
		// content://com.example.android.sunshine.app/location
		type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
		// vnd.android.cursor.dir/com.example.android.sunshine.app/location
		assertEquals(LocationEntry.CONTENT_TYPE, type);
		
		// content://com.example.android.sunshine.app/location/1
		type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
		// vnd.android.cursor.dir/com.example.android.sunshine.app/location
		assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
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
