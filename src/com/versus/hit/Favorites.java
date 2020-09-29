package com.versus.hit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class Favorites extends SherlockFragment{
	
	
    public Vector<String> str;
    public Vector<String> strFlags;
	// Progress Dialog
			private ProgressDialog pDialog;
			public int isFavori = 1;
			public Spinner spinDers;
			public ListView lv;
			public String secilenDers;
			public String currentUser;
			 public String imageUrl;
			 LayoutInflater inf;
			// Creating JSON Parser object
			JSONParser jParser = new JSONParser();


			ArrayList<HashMap<String, String>> productsList;

			
			// JSON Node names
			private static final String TAG_SUCCESS = "success";
			private static final String TAG_PRODUCTS = "favoriler";
			private static final String TAG_ID = "id";
			private static final String TAG_COUNTRY = "country";
			private static final String TAG_RADIO = "radio";
			private static final String TAG_STREAM = "stream";
			private static final String TAG_LOGO = "logo";

			// products JSONArray
			JSONArray products = null;
			
			// url to get all products list
	

	
	LazyAdapter adapter;
	Context context;
	
	public String radioName;
	public String radioStream;
	public String radioLogo;
	// Progress Dialog

	public Bitmap bitmap;
	public ImageView img;
	View view;
	public ListView favoriList;
	
	
	@Override
	public void onStop() {
	    super.onStop();
	 
	}
	 
	@Override
	public void onDestroy() {
	    super.onDestroy();
	
	    
	}
	

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.favorites, container, false);
		
		favoriList=(ListView)view.findViewById(R.id.favori_list);
	
		
		productsList = new ArrayList<HashMap<String, String>>();
		
		new LoadFavoriRadios().execute();
      
    	 
		favoriList.setOnItemClickListener(new OnItemClickListener() {

 			public void onItemClick(AdapterView<?> parent, View v,
 				int position, long id) {	
 				// getting values from selected ListItem
 				String radioName = ((TextView) v.findViewById(R.id.text)).getText().toString();
 				//veritabanindan logo ve link cekmemiz lazim burda
 				//String radioStream= productsList.get(position).toString();
 				
 				
 				//String logoUrl = productsList.get(position).toString();
 			     String radioStream= productsList.get(position).get(TAG_STREAM).toString();
 				
 				
 				String logoUrl = productsList.get(position).get(TAG_LOGO).toString();
 				
 				String radioid =  productsList.get(position).get(TAG_ID).toString();
  		
 						
 				((MainActivity)getActivity()).goToStream(radioName,radioStream,logoUrl,radioid);
 				
 				//Intent i = new Intent(getActivity(), RadioStream.class);
 				//i.putExtra("RADIO",radioName);
 				//i.putExtra("STREAM",radioStream);
 				//i.putExtra("LOGO",logoUrl);
 				//i.putExtra("USERNAME", currentUser);
 				// starting new activity and expecting some response back
 				//startActivityForResult(i, 100);
 				
 				
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
    	
	 	for (HashMap<String, String> map : productsList) {
    	    for (String key : map.keySet()) {       	    
    	    	str.add(map.get("radio").toString());
    	    	strFlags.add(map.get("logo").toString());    
    	    	break;
    	   } 

	 	}
	 	
	  adapter=new LazyAdapter(getInflate(), str, strFlags,context);
	  favoriList.setAdapter(adapter);
	     
    	
    }
	
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadFavoriRadios extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		
		
	
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Yükleniyor...Lütfen bekleyiniz");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters

			String url_all_products = "http://www.androidmusicdownload.com/radio/getFavoriList.php";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			url_all_products = url_all_products+"?email="+MainActivity.Email;
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
    					String id = c.getString(TAG_ID);
    					String country = c.getString(TAG_COUNTRY);
    					String radio = c.getString(TAG_RADIO);
    					String stream = c.getString(TAG_STREAM);
    					String logo = c.getString(TAG_LOGO);
    					
    					imageUrl = logo;

    					
    					// creating new HashMap
    					HashMap<String, String> map = new HashMap<String, String>();

    					// adding each child node to HashMap key => value
    					map.put(TAG_ID, id);
    					map.put(TAG_COUNTRY, country);
    					map.put(TAG_RADIO, radio);
    					map.put(TAG_STREAM, stream);
    					map.put(TAG_LOGO,logo);
    					

    					// adding HashList to ArrayList
    					productsList.add(map);
						}
						
					}
				 else {
					    this.cancel(true);
      			
					    for(int i = 0; i <= 9000; i++) {
				            if(this.isCancelled())
				            {
				            	
				            	pDialog.dismiss();
		     	            	break;
				            }
				
				        }
					    
					 //Toast.makeText(getActivity().getApplicationContext(), "JSON kısmında Bir Hata olmalı", Toast.LENGTH_LONG);
					// no products found
					// Launch Add New product Activity
//					Intent i = new Intent(getApplicationContext(),
//							NewProductActivity.class);
//					// Closing all previous activities
//					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

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
