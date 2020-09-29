/**
 * 
 */
package com.versus.hit;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * @author little
 *
 */
public class MyNotification extends Notification {
	
	public static RemoteViews contentView;
	private Context ctx;
	public static NotificationManager mNotificationManager;
	public static Notification notification;
	public MyNotification(Context ctx){
		super();
		this.ctx=ctx;

		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
		CharSequence tickerText = "Bando";
		long when = System.currentTimeMillis();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
		notification=builder.getNotification();
		notification.when=when;
		notification.tickerText=tickerText;
		notification.icon=R.drawable.icon;

	
		
		contentView=new RemoteViews(ctx.getPackageName(), R.layout.notification_layout);
		
		builder.setContent(contentView).build();
		//set the button listeners
		setListeners(contentView);
		
		notification.contentView = contentView;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		// used to call up this specific intent when you click on the notification
	
		if(Build.VERSION.SDK_INT < 11)
		{
			
	    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 1, 
			    		new Intent(ctx,MainActivity.class), 0);
		notification.contentIntent = contentIntent;		
		}
	   
		
		CharSequence contentTitle = "From Shortcuts";
		mNotificationManager.notify(548853, notification);
	}
	
	public void setListeners(RemoteViews view){
		//TODO screencapture listener
	        //adb shell /system/bin/screencap -p storage/sdcard0/SimpleAndroidTest/test.png
		

	
		view.setTextViewText(R.id.radioname,MainActivity.rs.radioName);
		
		Intent radio=new Intent(ctx, HelperActivity.class);
		radio.putExtra("DO", "play");
		radio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, radio, PendingIntent.FLAG_UPDATE_CURRENT);
		
		view.setOnClickPendingIntent(R.id.play, pRadio);
		
		//TODO screen size listener
		Intent volume=new Intent(ctx, HelperActivity.class);
		volume.putExtra("DO", "pause");
		volume.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, volume, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.pause, pVolume);
		
		//top listener
		Intent top=new Intent(ctx, HelperActivity.class);
		top.putExtra("DO", "close");
		top.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pTop = PendingIntent.getActivity(ctx, 3, top, PendingIntent.FLAG_CANCEL_CURRENT);
		view.setOnClickPendingIntent(R.id.close, pTop);
		
		//app listener
		Intent app=new Intent(ctx, HelperActivity.class);
		app.putExtra("DO", "app");
		PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
		view.setOnClickPendingIntent(R.id.app, pApp);
	}

}
