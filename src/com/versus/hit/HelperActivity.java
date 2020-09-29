/**
 * 
 */
package com.versus.hit;

import java.io.IOException;
import java.lang.reflect.Method;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author little
 *
 */
public class HelperActivity extends Activity {
	

	private HelperActivity ctx;
    public static int notifystatus = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx=this;
		

		String action= (String)getIntent().getExtras().get("DO");
		/*
		btn_play =  (ImageButton) MainActivity.rs.getActivity().findViewById(R.id.btn_play);
		btn_pause =  (ImageButton) MainActivity.rs.getActivity().findViewById(R.id.btn_pause);
		btn_stop =  (ImageButton) MainActivity.rs.getActivity().findViewById(R.id.btn_stop);
		*/

		if(action.equals("play")){
			
			if(!RadioStream.audioStreamer.getMediaPlayer().isPlaying())
			RadioStream.audioStreamer.getMediaPlayer().start();

			//btn_play.performClick();
		/*	boolean isEnabled = Settings.System.getInt
					(getContentResolver(), 
							Settings.System.AIRPLANE_MODE_ON, 0) == 1;
			Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", !isEnabled);
			sendBroadcast(intent); */
		}
		else if(action.equals("pause")){
			

			/*
			AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
			if(mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
	    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	    	else
	    		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	    		*/
			//MainActivity.rs.pauseAudio();
			//btn_pause.performClick();
			//rds.pauseAudio();
			//MainActivity.rs.mediaPlayer.pause();
			// rds.pauseAudio();

			//Media.pause();
			if(RadioStream.audioStreamer.getMediaPlayer().isPlaying())
			  RadioStream.audioStreamer.getMediaPlayer().pause();

		}
		else if(action.equals("stop")){
	
			/*
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Σίγου�?α �?ε?")
	    	       .setPositiveButton("�?αι �?ε!", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   try {
	        	   				Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
	        	   				proc.waitFor();
	        	   			} 
		        	    	catch (IOException ioe){
		        	    		Toast.makeText(ctx, "IOException: Είσαι σίγου�?α root?", Toast.LENGTH_LONG).show();
		        	        }
		        	    	catch (InterruptedException ie){
		        	    		Toast.makeText(ctx, "InterruptedException: Είσαι σίγου�?α root?", Toast.LENGTH_LONG).show();
		        	    	}   	        	   	
	    	           }
	    	       })
	    	       .setNegativeButton("Μπάα!", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   dialog.cancel();
	    	        	   ctx.finish();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.setCancelable(true);
	    	alert.setCanceledOnTouchOutside(false);
	    	alert.show();*/
			//rds.stopAudio();
			//btn_stop.performClick();3
				
			//Media.stop();
			RadioStream.audioStreamer.getMediaPlayer().stop();
			//rds.stopAudio();
		}
		else if(action.equals("close")){
			//RadioStream.audioStreamer.getMediaPlayer().stop();
			notifystatus = 1;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(548853);
			RadioStream.audioStreamer.getMediaPlayer().stop();
		
			try{
				finish();
			 }catch(Exception e){
				 
				 mNotificationManager.cancel(548853);
				 
			 }
			
			
		}
		else if(action.equals("app")){
			Intent app=new Intent(this,MainActivity.class);
			startActivity(app);
		}

		if(!action.equals("reboot"))
			finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
