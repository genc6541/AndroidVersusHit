package com.versus.hit;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MostPopular extends SherlockFragment{
	
	
    public Vector<String> str;
    public Vector<String> strFlags;
    public Vector<String> strCounts;
	// Progress Dialog
			public ListView mpList;
			 public String imageUrl;
			 LayoutInflater inf;
				private ProgressDialog pDialog;
	
	LazyAdapter adapter_mp;
	Context context;
	
	public String radioName;
	public String radioStream;
	public String radioLogo;
	// Progress Dialog

	public Bitmap bitmap;
	public ImageView img;
	View view;

	@Override
	public void onStop() {
	    super.onStop();
	 
	}
	 
	@Override
	public void onDestroy() {
	    super.onDestroy();
	
	    
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		view = inflater.inflate(R.layout.most_popular, container, false);
		
		mpList=(ListView)view.findViewById(R.id.mostpopular_list);


		
		new LoadMostPopularRadios().execute();
		
    	 
		mpList.setOnItemClickListener(new OnItemClickListener() {

 			public void onItemClick(AdapterView<?> parent, View v,
 				int position, long id) {	
 				// getting values from selected ListItem
 				String radioName = ((TextView) v.findViewById(R.id.text)).getText().toString();
 				//veritabanindan logo ve link cekmemiz lazim burda
 				//String radioStream= productsList.get(position).toString();
 				
 				
 				//String logoUrl = productsList.get(position).toString();
 			     String radioStream= MainActivity.AllproductsList.get(position).get("stream").toString();
 				
 				
 				String logoUrl = MainActivity.AllproductsList.get(position).get("logo").toString();
 				
 				String radioid =  MainActivity.AllproductsList.get(position).get("id").toString();
  		
 						
 				((MainActivity)getActivity()).goToStream(radioName,radioStream,logoUrl,radioid);

 				
 				
 			     }
 			});
    	 setInflate(inflater);
		return view;
    
    }
	
	
	public LayoutInflater getInflate(){
		return inf;
		
	}
	
	public void setInflate(LayoutInflater inflate){
		inf = inflate;
		
	}
	
	
	public void installCountriesAndFlags()
    {
    	
    	str=new Vector<String>();
    	strFlags=new Vector<String>();
    	strCounts=new Vector<String>();
    	
    	Collections.sort(MainActivity.AllproductsList, new MapComparator("count"));
 
    	int say = 0;
	 	for (HashMap<String, String> map : MainActivity.AllproductsList) {
    	    for (String key : map.keySet()) {  
    	    	say++;
    	    	str.add(map.get("radio").toString());
    	    	strFlags.add(map.get("logo").toString());    	
    	    	strCounts.add(map.get("count").toString());
    	    	break;
    	   } 
    		if(say==100)
	    		break;

	 	}
	 	
	
	 	
	  adapter_mp=new LazyAdapter(getInflate(), str, strFlags,strCounts,context);
	  mpList.setAdapter(adapter_mp);
	     
    	
    }
	
	
	class MapComparator implements Comparator<Map<String, String>>{
	    private final String key;

	    public MapComparator(String key){
	        this.key = key;
	    }
	    
	    public BigInteger toBigInteger(String foo)
	    {
	        return new BigInteger(foo.getBytes());
	    }


	    public int compare(Map<String, String> first,
	                       Map<String, String> second){
	        // TODO: Null checking, both for maps and values
	    	BigInteger  firstValue = toBigInteger(first.get(key));
	    	BigInteger  secondValue = toBigInteger(second.get(key));
	        // return   new BigInteger(secondValue).compareTo(firstValue);
	        return secondValue.compareTo(firstValue);
	    }
	}
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadMostPopularRadios extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		
		
	
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters


			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			
			installCountriesAndFlags();
		}
		
		

	}
	

}
