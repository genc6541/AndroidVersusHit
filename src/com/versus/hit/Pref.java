package com.versus.hit;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Pref extends SherlockPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		addPreferencesFromResource(R.layout.pref);
		
	  }   


public static String Read(Context context, final String key) {
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    return pref.getString(key, "");
}

public static void Write(Context context, final String key, final String value) {
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString(key, value);
      editor.commit();        
}
 
// Boolean  
public static boolean ReadBoolean(Context context, final String key, final boolean defaultValue) {
    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    return settings.getBoolean(key, defaultValue);
}

public static void WriteBoolean(Context context, final String key, final boolean value) {
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
      SharedPreferences.Editor editor = settings.edit();
      editor.putBoolean(key, value);
      editor.commit();        
}

}