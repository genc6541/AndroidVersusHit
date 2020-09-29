package com.versus.hit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectRadio extends SherlockFragment {
	LayoutInflater inf;
    ListView list1;
    LazyAdapter adapter;
    public Vector<String> str;
    public Vector<String> strFlags;
    public String getCountry;
    Context context;
    //Veritabanı işlemleri
    InputStream is=null;
	   String result=null;
	   String line=null;
	   int code;
	
	   private File destinationFile;
	   public String imageUrl;
	   public String pdfUrl;
	   public String downPdfUrl;
		public String downImageUrl;
	   public String pid;
    public String currentUser;
    public String yorum;
    public EditText txtYorum;
    public RelativeLayout layout;
    public int succesSorular = 0;
    public int succesYorumlar = 0;
    public String imageBaslik;
    public String pdfBaslik;
    public int pressedPdfButton = 0;
    

	// Progress Dialog
		public ProgressDialog pDialog;

		// Creating JSON Parser object
		JSONParser jParser = new JSONParser();

		ArrayList<HashMap<String, String>> productsList;
		
		// url to get all products list
		public static String url_all_products = "http://www.nailyektas.com/versus/getversuslist_and.php";

		

		
	
		// products JSONArray
		JSONArray products = null;
		JSONArray productsY = null;
			
		//Veritabanı
    

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View view = inflater.inflate(R.layout.select_radio, container, false);
			// Loading products in Background Thread
			
			getCountry = getArguments().getString("country");
			String url = url_all_products;  
			list1=(ListView)view.findViewById(R.id.list1);
			productsList = new ArrayList<HashMap<String, String>>();
			
			// Loading products in Background Thread
			new LoadAllProducts(url).execute();
		
		
//        Button b=(Button)findViewById(R.id.button1);
//        b.setOnClickListener(listener);
        
        
        // OnClick
        list1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {	
				// getting values from selected ListItem
				String radioName = ((TextView) v.findViewById(R.id.text)).getText()
						.toString();
				
				//String radioStream= productsList.get(position).get(TAG_STREAM).toString();
				
				
				//String logoUrl = productsList.get(position).get(TAG_LOGO).toString();
				
				//String radioid =  productsList.get(position).get(TAG_ID).toString();
				
				//((MainActivity)getActivity()).goToStream(radioName,radioStream,logoUrl,radioid);
				
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
		
	public void setInflate(LayoutInflater inflate){
		inf = inflate;
		
	}
	
	public LayoutInflater getInflate(){
		return inf;
		
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
      list1.setAdapter(adapter);
	     
    	
    }
    
    
    @Override
    public void onDestroy()
    {
        list1.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };
    
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
       
    	LayoutInflater inf;
        ListView list1;
        LazyAdapter adapter;
        public Vector<String> str;
        public Vector<String> strFlags;
        //Veritabaný iþlemleri
        InputStream is=null;
    	   String result=null;
    	   String line=null;
    	   int code;
    	   public String newurl;
    	   private File destinationFile;
    	   public String imageUrl;
    	   public String pdfUrl;
    	   public String downPdfUrl;
    		public String downImageUrl;
    	   public String pid;
        public String currentUser;
        public String yorum;
        public EditText txtYorum;
        public RelativeLayout layout;
        public int succesSorular = 0;
        public int succesYorumlar = 0;
        public String imageBaslik;
        public String pdfBaslik;
        public int pressedPdfButton = 0;
    	

    	// Progress Dialog
    		public ProgressDialog pDialog;

    		// Creating JSON Parser object
    		JSONParser jParser = new JSONParser();

    		//ArrayList<HashMap<String, String>> productsList;
    		
    		// url to get all products list
    		//public static String url_all_products = "http://www.androidmusicdownload.com/radio/getRadioList.php";

    		// JSON Node names
    		private static final String TAG_SUCCESS = "success";
    		private static final String TAG_PRODUCTS = "versuslist";
    		private static final String TAG_ID = "id";
    		private static final String TAG_VS1 = "vs1";
    		private static final String TAG_VS2 = "vs2";
    		private static final String TAG_VS1_VOTE = "vs1_vote";
    		private static final String TAG_VS2_VOTE = "vs2_vote";
    		private static final String TAG_CATEGORY = "category";
    		private static final String TAG_LOCATION= "location";
    		private static final String TAG_VS1_PERCENT = "vs1_percent";
    		private static final String TAG_VS2_PERCENT = "vs2_percent";
    		private static final String TAG_ALLVOTES= "allvotes";
    		private static final String TAG_USER = "type";
    		private static final String TAG_CODE = "code";
    		
    	
    		// products JSONArray
    		JSONArray products = null;
    		JSONArray productsY = null;
    			
    		//Veritabaný

    	/**
    	 * Before starting background thread Show Progress Dialog
    	 *
    	 * */
    		
    		LoadAllProducts(String str){
    			
    			newurl = str;
    			
    		}
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
    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		// getting JSON string from URL
    		//newurl = newurl.replace(" ", "%20");
    		JSONObject json = jParser.makeHttpRequest(newurl, "GET", params);
    		
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
    					String vs1 = c.getString(TAG_VS1);
    					String vs2 = c.getString(TAG_VS2);
    					String vs1_vote = c.getString(TAG_VS1_VOTE);
    					String vs2_vote = c.getString(TAG_VS2_VOTE);
    					String category = c.getString(TAG_CATEGORY);
    					String location = c.getString(TAG_LOCATION);
    					String vs1_percent = c.getString(TAG_VS1_PERCENT);
    					String vs2_percent = c.getString(TAG_VS2_PERCENT);
    					String allvotes = c.getString(TAG_ALLVOTES);
    					String user = c.getString(TAG_USER);
    					String code = c.getString(TAG_CODE);
    					
    					
    					
    					
    					//imageUrl = logo;

    					
    					// creating new HashMap
    					HashMap<String, String> map = new HashMap<String, String>();

    					// adding each child node to HashMap key => value
    					map.put(TAG_ID, id);
    					map.put(TAG_VS1, vs1);
    					map.put(TAG_VS2, vs2);
    					map.put(TAG_VS1_VOTE, vs1_vote);
    					map.put(TAG_VS2_VOTE,vs2_vote);
    					map.put(TAG_CATEGORY, category);
    					map.put(TAG_LOCATION, location);
    					map.put(TAG_VS1_PERCENT, vs1_percent);
    					map.put(TAG_VS2_PERCENT, vs2_percent);
    					map.put(TAG_ALLVOTES,allvotes);
    					map.put(TAG_USER, user);
    					map.put(TAG_CODE,code);
    					

    					// adding HashList to ArrayList
    					productsList.add(map);
    				}
    			} else {
    				Toast.makeText(getActivity().getApplicationContext(), "JSON kısmsnda Bir Hata olmalı", Toast.LENGTH_LONG);
    				
    				// no products found
    				// Launch Add New product Activity
//    				Intent i = new Intent(getApplicationContext(),
//    						NewProductActivity.class);
//    				// Closing all previous activities
//    				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    				startActivity(i);
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    		
//    			try {
//                   bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.androidmusicdownload.com"+imageUrl).getContent());
//                   downImageUrl = "http://www.androidmusicdownload.com"+imageUrl;
//    		} catch (Exception e) {
//                  e.printStackTrace();
//            }
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
	
    
    
