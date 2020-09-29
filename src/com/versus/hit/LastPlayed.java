package com.versus.hit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.versus.hit.Favorites.LoadFavoriRadios;
import com.versus.hit.RadioStream.insertTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LastPlayed extends SherlockFragment{
	public String lastRadioId;
	public Vector<String> str;
    public Vector<String> strFlags;
    Activity activity;
	public Spinner spinDers;
	public ListView lv;
	public String secilenDers;
	public String currentUser;
	 public String imageUrl;
	 public static int isLastPlayed = 0;
	//Favori ekle
		String favemail;
		String favradioid;
		InputStream is=null;
		String result=null;
		String line=null;
		int code;
		//
		JSONParser jParser = new JSONParser();
	LazyAdapter adapter;
	Context context;
	 LayoutInflater inf;
	public String radioName;
	public String radioStream;
	public String radioLogo;
	// Progress Dialog

	public Bitmap bitmap;
	public ImageView img;
	View view;
	public String datas[];
	public ListView lastplayed;
	ArrayList<HashMap<String, String>> dersList;
	ArrayList<HashMap<String, String>> productsList;
	ArrayList<HashMap<String, String>> soruList;
	
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
	
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.lastplayed, container, false);
		
		lastplayed=(ListView)view.findViewById(R.id.lastplayed);
     
		new LoadLastPlayedRadios(getActivity()).execute("0");
      	 
    	 lastplayed.setOnItemClickListener(new OnItemClickListener() {

 			public void onItemClick(AdapterView<?> parent, View v,
 				int position, long id) {	
 				// getting values from selected ListItem
 				String radioName = ((TextView) v.findViewById(R.id.text)).getText()
 						.toString();
 				
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
	  lastplayed.setAdapter(adapter);
	     
    	
    }
  
	public String insert(String favradioid)
    {
	  
		
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 
    	nameValuePairs.add(new BasicNameValuePair("id",favradioid));
    	nameValuePairs.add(new BasicNameValuePair("email",MainActivity.Email));

    	try
    	{
		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://www.androidmusicdownload.com/radio/lastplayedEkle.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost); 
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        Log.e("pass 1", "connection success ");
	}
        catch(Exception e)
	{
        	Log.e("Fail 1", e.toString());
	    	Toast.makeText(getActivity().getApplicationContext(), "Invalid IP Address",
			Toast.LENGTH_LONG).show();
	}     
        
        try
        {
            BufferedReader reader = new BufferedReader
			(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
	    {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
	    Log.e("pass 2", "connection success ");
	}
        catch(Exception e)
	{
            Log.e("Fail 2", e.toString());
	}     
       
	try
	{
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("code"));
			
            if(code==1)
            {
		Toast.makeText(getActivity().getBaseContext(), "Inserted Successfully",
			Toast.LENGTH_SHORT).show();
            }
            else
            {
		 Toast.makeText(getActivity().getBaseContext(), "Sorry, Try Again",
			Toast.LENGTH_LONG).show();
            }
	}
	catch(Exception e)
	{
            Log.e("Fail 3", e.toString());
	}
	return "OK";
	
    }

  class insertTask extends AsyncTask<String, String, String> {
     protected String doInBackground(String... params) {
         String result = insert(params[0]);
         return result ;
     }

     
     protected void onPostExecute(String result) {
       
    	 new updateTask().execute(lastRadioId); 
         //after background is done, use this to show or hide dialogs
     }
 }
	
  
    public void lastPlayedControl()
    {
    	for (HashMap<String, String> map : productsList) {
    	    for (String key : map.keySet()) { 
    	    	if (map.get("id").toString().equals(lastRadioId))
    	    	{
    	    		isLastPlayed = 1;

    	    		break;
    	    	}
    	    }
		}
    }
  /**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadLastPlayedRadios extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		private ProgressDialog pDialog;
		Activity act;
		
	
		LoadLastPlayedRadios(Activity activity){
			
			act = activity;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(act);
			pDialog.setMessage("Loading...Please Wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			

			productsList = new ArrayList<HashMap<String, String>>();
			lastRadioId =  args[0];
			
			String url_all_products = "http://www.androidmusicdownload.com/radio/getLastplayedList.php";

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
					 
					    isLastPlayed = 0;
						if(isLastPlayed == 0 && lastRadioId != "0")
						{
							 if(productsList.size() == 10)
					  		 {
					  			 new deleteTask().execute();
					  		 }
					  		 else
					  		 {
					  		   new insertTask().execute(lastRadioId); 
					  		 }
						}
					 
					 
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
			isLastPlayed = 0;
			
			if(lastRadioId != "0")
			{
				lastPlayedControl();
			}
			else
			{
				installCountriesAndFlags();
			}
			
			
			
		  	if(isLastPlayed == 0 && lastRadioId != "0")
		  	{
		  		 if(productsList.size() == 10)
		  		 {
		  			 new deleteTask().execute();
		  		 }
		  		 else
		  		 {
		  		   new insertTask().execute(lastRadioId); 
		  		 }
		  		 
			    
		  	}
		}

	}
	
	//Favori Silme İşlemleri
	
	 public String delete()
	    {
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 
	     	//nameValuePairs.add(new BasicNameValuePair("radioid",favradioid));
	     	nameValuePairs.add(new BasicNameValuePair("email",MainActivity.Email));
	    	
	    	try
	    	{
			HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://www.androidmusicdownload.com/radio/deleteLastPlayed.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost); 
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		        Log.e("pass 1", "connection success ");
	    	}
	        catch(Exception e)
	    	{
	        	Log.e("Fail 1", e.toString());
		    	Toast.makeText(getActivity().getApplicationContext(), "Invalid IP Address",
				Toast.LENGTH_LONG).show();
	    	}     
	        
	        try
	        {
	            	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	            	StringBuilder sb = new StringBuilder();
	            	while ((line = reader.readLine()) != null)
			{
	        	    sb.append(line + "\n");
	    		}
	            	is.close();
	            	result = sb.toString();
		        Log.e("pass 2", "connection success ");
		}
	        catch(Exception e)
	    	{
	        	Log.e("Fail 2", e.toString());
	    	}     
	       
		try
	    	{
	        	JSONObject json_data = new JSONObject(result);
	        	code=(json_data.getInt("code"));
	        	if(code==1)
	        	{
				Toast.makeText(getActivity().getBaseContext(),"Record Deleted",
					Toast.LENGTH_SHORT).show();
	        	}
	        	else
	        	{
	    			Toast.makeText(getActivity(),"Sorry, Try Again",
					Toast.LENGTH_SHORT).show();
	        	}
	    	}
	        catch(Exception e)
	    	{
	        	Log.e("Fail 3", e.toString());
	    	}
		
		return "OK";
	    }
	
	 class deleteTask extends AsyncTask<String, String, String> {
	     protected String doInBackground(String... params) {
	         String result = delete();
	         return result ;
	     }
	     
	     protected void onPostExecute(String result) {
           
	    	 new insertTask().execute(lastRadioId);
	   }
	 
	
	 }
	
	 
	 
	 //Update radio count for TOP100
	 
	 
	 public String update_count(String favradioid)
	    {
		  
			
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 
	    	nameValuePairs.add(new BasicNameValuePair("id",lastRadioId));

	    	try
	    	{
			HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://www.androidmusicdownload.com/radio/update_count.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost); 
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		        Log.e("pass 1", "connection success ");
		}
	        catch(Exception e)
		{
	        	Log.e("Fail 1", e.toString());
		    	Toast.makeText(getActivity().getApplicationContext(), "Invalid IP Address",
				Toast.LENGTH_LONG).show();
		}     
	        
	        try
	        {
	            BufferedReader reader = new BufferedReader
				(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            while ((line = reader.readLine()) != null)
		    {
	                sb.append(line + "\n");
	            }
	            is.close();
	            result = sb.toString();
		    Log.e("pass 2", "connection success ");
		}
	        catch(Exception e)
		{
	            Log.e("Fail 2", e.toString());
		}     
	       
		try
		{
	            JSONObject json_data = new JSONObject(result);
	            code=(json_data.getInt("code"));
				
	            if(code==1)
	            {
			Toast.makeText(getActivity().getBaseContext(), "Updated Successfully",
				Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
			 Toast.makeText(getActivity().getBaseContext(), "Sorry, Try Again",
				Toast.LENGTH_LONG).show();
	            }
		}
		catch(Exception e)
		{
	            Log.e("Fail 3", e.toString());
		}
		return "OK";
		
	    }

	  class updateTask extends AsyncTask<String, String, String> {
	     protected String doInBackground(String... params) {
	         String result = update_count(params[0]);
	         return result ;
	     }

	     
	     protected void onPostExecute(String result) {
	       
	        	 
	         //after background is done, use this to show or hide dialogs
	     }
	 }
	
	
	
}
