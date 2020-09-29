package com.versus.hit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
	public String listtype="0";
    private Activity activity;
    private Vector<String> data;
    private Vector<String> datakat;
    private Vector<String> flagskat;
    private Vector<String> flags;
    private Vector<String> counts;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> newlist;
    private ArrayList<HashMap<String, String>> arraylist = null;

    public LazyAdapter(ArrayList<HashMap<String, String>> worldpopulationlist,Activity a, Vector<String> d, Vector<String> f) {
    	listtype = "0";
    	list = worldpopulationlist;
		arraylist = new ArrayList<HashMap<String, String>>();
		arraylist.addAll(worldpopulationlist);
    	activity = a;
        data=d;
        flags=f;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    
    public LazyAdapter(LayoutInflater a, Vector<String> d, Vector<String> f,Context context) {
        //activity = a;
    	listtype = "1";
        datakat=d;
        flagskat=f;
        inflater = a;
        imageLoader=new ImageLoader(context);
    }
    
    public LazyAdapter(LayoutInflater a, Vector<String> d, Vector<String> f,Vector<String> c,Context context) {
        //activity = a;
    	listtype = "2";
        data=d;
        flags=f;
        counts = c;
        inflater = a;
        imageLoader=new ImageLoader(context);
    }

    public int getCount() {
    	
       int size = 0 ;
    	if(listtype=="0")
    	{
    		size = list.size();
    	}
    	else
    	{
    		if(listtype=="1")
    		{
    			
    		  size = datakat.size();
    		}
    		
    		if(listtype=="2")
    		{
    			
    		  size = data.size();
    		}
    		
    	}
    		 return size;
    
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        TextView text=(TextView)vi.findViewById(R.id.text);
        TextView logo=(TextView)vi.findViewById(R.id.logo);
        TextView stream=(TextView)vi.findViewById(R.id.streamlink);
        TextView radioid=(TextView)vi.findViewById(R.id.radioid);
        TextView count=(TextView)vi.findViewById(R.id.count);

        ImageView image=(ImageView)vi.findViewById(R.id.image);
        if(listtype == "0")
        {
        text.setText( list.get(position).get("radio").toString());
        logo.setText( list.get(position).get("logo").toString());
        stream.setText( list.get(position).get("stream").toString());
        radioid.setText( list.get(position).get("id").toString());
  
           
        imageLoader.DisplayImage(list.get(position).get("logo"), image);
        }
        else
        {
        	if(listtype == "1")
        	{
            text.setText(datakat.get(position)); 
            imageLoader.DisplayImage(flagskat.get(position), image);
        	}
        	if(listtype == "2")
        	{
            text.setText(data.get(position));
            imageLoader.DisplayImage(flags.get(position), image);
            count.setText("(" + counts.get(position)+")");
            
        	}
        	
        
        }
   
        return vi;
    }
    
    public void filter(String charText) {

		charText = charText.toLowerCase(Locale.getDefault());
		//list.clear();
		list = new ArrayList<HashMap<String, String>>();
		if (charText.length() == 0) {
			list.addAll(arraylist);
		} 
		else 
		{
			for (HashMap<String, String> map : arraylist) 
			{
				if (map.get("radio").toString().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					list.add(map);
				}
			}
		}
		notifyDataSetChanged();
	}
}