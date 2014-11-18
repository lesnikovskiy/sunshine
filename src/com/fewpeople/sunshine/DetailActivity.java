package com.fewpeople.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends ActionBarActivity {
	private static final String LOG_TAG = DetailActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
				.add(R.id.detail_container, new DetailFragment()).commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		if (id == R.id.action_map) {
			openPreferredLocationInMap();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void openPreferredLocationInMap() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
		
		Uri geolocation = Uri.parse("geo:0,0?").buildUpon()
				.appendQueryParameter("q", location)
				.build();
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(geolocation);
		
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		} else {
			Log.d(LOG_TAG, "Couldn't call " + location + ", no activity resolved");
		}
	}
}