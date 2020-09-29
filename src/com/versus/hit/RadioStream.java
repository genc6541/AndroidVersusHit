package com.versus.hit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import com.versus.hit.LastPlayed.LoadLastPlayedRadios;
import com.versus.hit.LastPlayed.insertTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class RadioStream extends SherlockFragment implements OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener{
    //Favori ekle
	String favemail;
	public String favradioid;
	InputStream is=null;
	String result=null;
	String line=null;
	int code;
	 public int isRecording = 0;

	 public static String mFileName = null;
	 public static FileOutputStream out2;


	public int isFavori =0;
	public static int recordbas = 0;
	//
	//Favori Sil 
	// Progress Dialog
    private ProgressDialog dDialog;
	LastPlayed lp = new LastPlayed();
	
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
	JSONParser jParser = new JSONParser();
	
	
	private Button streamButton;
	
	private ImageButton playButton;
	
	private TextView textStreamed ;
	public ImageButton favbutton;
	private boolean isPlaying;
	
	public static StreamingMediaPlayer audioStreamer;
	public int isFirstPlay;
	public String firstStream;
	public static String radioName;
	public String radioStream;
	public String radioLogo;
    public ImageButton fav;
	// Progress Dialog
			public ProgressDialog pDialog;
			ImageButton recordButton;
	public Bitmap bitmap;
	public ImageView img;
	View view;
	public String datas[];
	private TextView txtRadioName;
	private ImageButton btn_play,
    				btn_pause,
    				btn_stop,
    				btn_sol,
    				btn_sag;
	private SeekBar seekBar;
	private MediaPlayer mediaPlayer;
	private int lengthOfAudio;
	//private final String URL = "http://android.erkutaras.com/media/audio.mp3";
	private String URL ;
	Favorites fv = new Favorites();
    private final Handler handler = new Handler();
	private final Runnable r = new Runnable() {	
		@Override
		public void run() {
			updateSeekProgress();					
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.radio, container, false);
		
		initControls();
		
    		//favori kontrol
    		    
    			productsList = new ArrayList<HashMap<String, String>>();
                
    			new LoadFavoriRadios().execute();
    			
    			recordButton = (ImageButton)view.findViewById(R.id.btnRecord);

    			recordButton.setOnClickListener(new OnClickListener() {

    			@Override

    			public void onClick(View v) {

    			if(isRecording == 0)

    			{

    				 String sep = File.separator; // Use this instead of hardcoding the "/"

    				 String newFolder = "Bando";

    				 String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

    				 File myNewFolder = new File(extStorageDirectory + sep + newFolder);

    				 if(!myNewFolder.exists())
    					 myNewFolder.mkdir();
    				

    				 mFileName = Environment.getExternalStorageDirectory().toString() + sep + newFolder + sep + RadioStream.radioName + RadioStream.getRandomNumber()+".mp3";

 
    				 try {
						 out2 = new FileOutputStream(mFileName);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
  
    				recordbas = 1;	
    				
    			recordButton.setImageResource(R.drawable.rec2);

    			recordButton.invalidate();

    			isRecording = 1;


    			}

    			else

    			{

    			isRecording = 0;
    			
    			recordbas = 2;	

    			recordButton.setImageResource(R.drawable.rec1);

    			 recordButton.invalidate();

    			Toast.makeText(getActivity(), "Audio file was recorded to the BANDO folder", Toast.LENGTH_LONG).show();

    			}

    			}

    			});
    		    
    		
    		 return view;
    }
	
	//for rounded image
		public static Bitmap loadRoundedBitmap(Bitmap bitmap) {
		
	  int targetWidth = 500;
      int targetHeight = 500;
      Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
                        targetHeight,Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(targetBitmap);
    Path path = new Path();
    path.addCircle(((float) targetWidth - 1) / 2,
        ((float) targetHeight - 1) / 2,
        (Math.min(((float) targetWidth), 
        ((float) targetHeight)) / 2),
        Path.Direction.CW);

    canvas.clipPath(path);
    Bitmap sourceBitmap = bitmap;
    canvas.drawBitmap(sourceBitmap, 
        new Rect(0, 0, sourceBitmap.getWidth(),
        sourceBitmap.getHeight()), 
        new Rect(0, 0, targetWidth, targetHeight), null);
    return targetBitmap;
		}
	
	private void init() {
		
		btn_play = (ImageButton)view.findViewById(R.id.btn_play);
		btn_play.setOnClickListener(this);
		btn_pause = (ImageButton)view.findViewById(R.id.btn_pause);
		btn_pause.setOnClickListener(this);
		btn_pause.setEnabled(false);
		btn_stop = (ImageButton)view.findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(this);
		btn_stop.setEnabled(false);
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
				
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
		//seekBar.setSecondaryProgress(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mediaPlayer.isPlaying()) {
//			SeekBar tmpSeekBar = (SeekBar)v;
//			mediaPlayer.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress() );
		}
		return false;
	}

	@Override
	public void onClick(View view) {

		if(view.getId()==R.id.btn_play)
		{
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		}
		
		
		try {
		
			mediaPlayer.setDataSource(URL);
			mediaPlayer.prepare();
			lengthOfAudio = mediaPlayer.getDuration();
		} catch (Exception e) {
			//Log.e("Error", e.getMessage());
		}
		
		switch (view.getId()) {
		case R.id.btn_play:
			playAudio();
			break;
		case R.id.btn_pause:
			pauseAudio();
			break;
		case R.id.btn_stop:
			stopAudio();
			break;
		default:
			break;
		}
		
		updateSeekProgress();
	}

	private void updateSeekProgress() {
		if (mediaPlayer.isPlaying()) {
//			seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / lengthOfAudio) * 100));
//			handler.postDelayed(r, 1000);
		}
	}

	public void stopAudio() {
		mediaPlayer.stop();
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
		//seekBar.setProgress(0);
	}

	public void pauseAudio() {
		mediaPlayer.pause();
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
	}

	public void playAudio() {
		
		mediaPlayer.start();
		
		btn_play.setEnabled(false);
		pDialog.dismiss();
		
		btn_pause.setEnabled(true);
		btn_stop.setEnabled(true);
	}
	
	
	public Bitmap setImageFromUrl(String url)
	{
		try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (Exception e) {
           e.printStackTrace();
     }
		
		return bitmap;
		
	}
	
	public void favoriControl()
	 {  
		isFavori = 0;
		favbutton.setBackgroundResource(R.drawable.favori1);
		
		for (HashMap<String, String> map : productsList) {
    	    for (String key : map.keySet()) { 
    	    	if (map.get("id").toString().equals(favradioid))
    	    	{
    	    		isFavori = 1;
    	    		favbutton.setBackgroundResource(R.drawable.favori2);
    	    		break;
    	    	}
    	    }
		}
		
	}
	
	public void getRandom(){
		
		int num = MainActivity.AllproductsList.size();
		Random rand = new Random();
		btn_play.setEnabled(false);
		int n = rand.nextInt(num) + 1;
		
		String Rname = null;
		String Rstream = null;
		String Rlogo = null;
		String Rid = null;
		
		int say = 0;
		
		for (HashMap<String, String> map : MainActivity.AllproductsList) {
			say++;
    	    for (String key : map.keySet()) {       	  
    	    	if(say == n){
    	    		
    	    		Rname = map.get("radio").toString();
    	    		Rstream = map.get("stream").toString();
    	    		Rlogo = map.get("logo").toString();
    	    		Rid = map.get("id").toString();
    	    		
    	    	}
    	    		break;
    	   } 

	 	}
		
		streamInit(Rname, Rstream, Rlogo, Rid);
		
	}

	public void streamInit(String Rname, String Rstream, String Rlogo,String Rid) {
		radioName = Rname;
	    radioStream = Rstream;
	    radioLogo = Rlogo;
	    favradioid= Rid;
	    URL = radioStream;
	    favoriControl();
		setImageFromUrl(radioLogo);
	    img.setImageBitmap(setImageFromUrl(radioLogo));
		txtRadioName.setText(radioName);
		MyNotification.notification.contentView.setTextViewText(R.id.radioname, radioName);
		MyNotification.mNotificationManager.notify(548853, MyNotification.notification);
		startStreamingAudio();
	}

    private void initControls() {
    	textStreamed = (TextView) view.findViewById(R.id.text_kb_streamed);
    	textStreamed.setText("Loading...");
    	
    	favbutton = (ImageButton)view.findViewById(R.id.btnFavori);
		btn_play = (ImageButton) view.findViewById(R.id.btn_play);
		btn_stop = (ImageButton)view.findViewById(R.id.btn_stop);
		btn_pause = (ImageButton) view.findViewById(R.id.btn_pause);
		
		btn_sol = (ImageButton)view.findViewById(R.id.btn_sol);
		btn_sag = (ImageButton) view.findViewById(R.id.btn_sag);
		
		btn_sol.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				getRandom();
				
        }});
		
		btn_sag.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				getRandom();
        }});
		
		favbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				if(isFavori == 0)
				{
				  if(productsList.size()==20)
				  {
					  AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
			 	 		alertDialog.setTitle("Warning!");
			 	 		alertDialog.setMessage("Your favorites list is full!");
			 	 		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			 	 		   public void onClick(DialogInterface dialog, int which) {
			 	 			 return;
			 	 		   }
			 	 		});
			 	 		// Set the Icon for the Dialog
			 	 		alertDialog.setIcon(R.drawable.icon);
			 	 		alertDialog.show();
				         
				  }
				  else
				  {
					  new insertTask().execute("");
				  }  			  
				}
				else
				{
					new deleteTask().execute("");
				}
				
				
        }});
		
		btn_play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				btn_stop.setEnabled(true);
				btn_play.setImageResource(R.drawable.play2);
				btn_pause.setImageResource(R.drawable.pause1);
				btn_stop.setImageResource(R.drawable.stop1);
				
				// !firstStream.equals(radi)
				
				
				
				if(isFirstPlay == 1)
				{				
					new MyNotification(getActivity());
				    startStreamingAudio();
				}
				else
				{
					if(!audioStreamer.getMediaPlayer().isPlaying())
					{
					audioStreamer.getMediaPlayer().start();
					audioStreamer.startPlayProgressUpdater();
					}
					
				}
        }});

		
		btn_pause.setEnabled(false);
		btn_pause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				
				btn_pause.setImageResource(R.drawable.pause2);
				btn_play.setImageResource(R.drawable.play1);
				btn_stop.setImageResource(R.drawable.stop1);
				
				 isFirstPlay = 0;
				if (audioStreamer.getMediaPlayer().isPlaying()) {
					audioStreamer.getMediaPlayer().pause();
					btn_play.setEnabled(true);
					//btn_pause.setImageResource(R.drawable.button_play);
				} else {
//					audioStreamer.getMediaPlayer().start();
//					audioStreamer.startPlayProgressUpdater();
					//btn_pause.setImageResource(R.drawable.button_pause);
				}
				isPlaying = !isPlaying;
        }});
		

		btn_stop.setEnabled(false);
		btn_stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				btn_stop.setImageResource(R.drawable.stop2);
				btn_pause.setImageResource(R.drawable.pause1);
				btn_play.setImageResource(R.drawable.play1);
				
				if(audioStreamer.getMediaPlayer().isPlaying())
				{
			     isFirstPlay = 1;
			     btn_play.setEnabled(true);
				 audioStreamer.getMediaPlayer().stop();
				}
				
			}
		});
		
		
    }
    
    private void startStreamingAudio() {
    	try { 
    		
    		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    		if ( audioStreamer != null) {
    			audioStreamer.interrupt();
    			
    		}
    		audioStreamer = new StreamingMediaPlayer(getView().getContext(),textStreamed, btn_pause, btn_play,progressBar);
    		
    	
    		//audioStreamer.startStreaming("http://www.pocketjourney.com/downloads/pj/tutorials/audio.mp3",1717, 214);
    		audioStreamer.startStreaming(URL,5208, 216);
    		btn_play.setEnabled(false);
    	} catch (IOException e) {
	    	Log.e(getClass().getName(), "Error starting to stream audio.", e);            		
    	}
    	    	
    }
    
    //Favori ekleme start
    

	  public String insert()
	    {
		  
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 
	    	nameValuePairs.add(new BasicNameValuePair("id",favradioid));
	    	nameValuePairs.add(new BasicNameValuePair("email",MainActivity.Email));
	
	    	try
	    	{
			HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://www.androidmusicdownload.com/radio/favoriEkle.php");
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
	         String result = insert();
	         return result ;
	     }

	     
	     protected void onPostExecute(String result) {
	         
	    	 favbutton.setBackgroundResource(R.drawable.favori2);	
	    	 isFavori = 1;
	         //after background is done, use this to show or hide dialogs
	     }
	 }
	 
	 

	 public static int getRandomNumber()

	 {

	 int min = 1;

	 int max = 5000;

	 Random r = new Random();

	 int i1 = r.nextInt(max - min + 1) + min;

	 return i1;

	 }
	 
	 //Favori ekleme end
	 
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
				
		        txtRadioName = (TextView)view.findViewById(R.id.txtRadio);
		        img = (ImageView)view.findViewById(R.id.radioLogo);
		        
				datas = getArguments().getStringArray("key");
				
		        isFirstPlay = 1;
		        
		    		    radioName = datas[0];
		    		    radioStream = datas[1];
		    		    radioLogo = datas[2];
		    		    favradioid= datas[3];
		    		    URL = radioStream;
		    		   

		        		firstStream = radioStream;
		        		 
		        	
		            //init();
		        		
		        	

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
	    					
	    					//imageUrl = logo;

	    					
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
					
						 //hiç favori yoksa favori varmış gibi radio ekle
						 
						// creating new HashMap
	    					HashMap<String, String> map = new HashMap<String, String>();

	    					// adding each child node to HashMap key => value
	    					map.put(TAG_ID, "0");
	    					map.put(TAG_COUNTRY, "bando");
	    					map.put(TAG_RADIO, "bando");
	    					map.put(TAG_STREAM, "bando");
	    					map.put(TAG_LOGO,"bando");
	    					

	    					// adding HashList to ArrayList
	    					productsList.add(map);
						 
						 /*
						    this.cancel(true);
	      			
						    for(int i = 0; i <= 9000; i++) {
					            if(this.isCancelled())
					            {
					            	
					            	pDialog.dismiss();
			     	            	break;
					            }
					            		            
					
					        }
						    */
						    /*
							if(lp.isLastPlayed == 0 && favradioid != "0")
							{
						
								lp.new LoadLastPlayedRadios().execute(favradioid);
							}*/
						    
						 //Toast.makeText(getActivity().getApplicationContext(), "JSON kısmında Bir Hata olmalı", Toast.LENGTH_LONG);
						// no products found
						// Launch Add New product Activity
//						Intent i = new Intent(getApplicationContext(),
//								NewProductActivity.class);
//						// Closing all previous activities
//						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(i);
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
			
				datas = getArguments().getStringArray("key");
				
		        isFirstPlay = 1;
		        
		    		    radioName = datas[0];
		    		    radioStream = datas[1];
		    		    radioLogo = datas[2];
		    		    favradioid= datas[3];
		    		    URL = radioStream;
				btn_play.performClick();
			
				   //setImageFromUrl(radioLogo);
      		       img.setImageBitmap(loadRoundedBitmap(setImageFromUrl(radioLogo)));
				   txtRadioName.setText(radioName);
      				 
					
					
					
				   favoriControl();
				   pDialog.dismiss();
		
	            
			       lp.new LoadLastPlayedRadios(getActivity()).execute(favradioid);
				
				//installCountriesAndFlags();
			}
			
			

		}
	
		
		//Favori Silme İşlemleri
		
		 class deleteTask extends AsyncTask<String, String, String> {
		     protected String doInBackground(String... params) {
		         String result = delete();
		         return result ;
		     }
		     protected void onPreExecute() {
					super.onPreExecute();
					dDialog = new ProgressDialog(getActivity());
					dDialog.setMessage("Updating...");
					dDialog.setIndeterminate(false);
					dDialog.setCancelable(true);
					dDialog.show();
				}
		     
		     protected void onPostExecute(String result) {
		    	 dDialog.dismiss();
		    	 favbutton.setBackgroundResource(R.drawable.favori1);
		    	 isFavori = 0;
		    	 
		 }
		 
		 public String delete()
		    {
		    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 
		     	nameValuePairs.add(new BasicNameValuePair("radioid",favradioid));
		     	nameValuePairs.add(new BasicNameValuePair("email",MainActivity.Email));
		    	
		    	try
		    	{
				HttpClient httpclient = new DefaultHttpClient();
			        HttpPost httppost = new HttpPost("http://www.androidmusicdownload.com/radio/deleteFavori.php");
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
		 }
		
}