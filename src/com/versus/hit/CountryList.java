package com.versus.hit;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Vector;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class CountryList extends SherlockFragment {
	
    ListView list;
    LazyAdapter adapter;
    public Vector<String> str;
    public Vector<String> strFlags;
    FragmentManager fm = getFragmentManager();
    Context context;
    @Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
	}
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main, container, false);
		// Activate Navigation Mode Tabs
				
				
        installCountriesAndFlags();

        list=(ListView)view.findViewById(R.id.list);
        adapter=new LazyAdapter(inflater, str, strFlags,getActivity().getApplicationContext());
        list.setAdapter(adapter);
//        Button b=(Button)findViewById(R.id.button1);
//        b.setOnClickListener(listener);
        
        // OnClick
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {	
				
				// getting values from selected ListItem
				String country = ((TextView) v.findViewById(R.id.text)).getText()
						.toString();
				
				((MainActivity)getActivity()).goToNextPage(country);
				//Intent i = new Intent(getActivity(),SelectRadio.class);
				//i.putExtra("COUNTRY",country);
				//i.putExtra("USERNAME", currentUser);
				// starting new activity and expecting some response back
				//startActivityForResult(i, 100);
				
			     }
			});
        return view;
    }
    
    private ActionBar getSupportActionBar() {
		// TODO Auto-generated method stub
		return null;
	}

	private ListView findViewById(int list2) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setContentView(int main) {
		// TODO Auto-generated method stub
		
	}

	public void installCountriesAndFlags()
    {
    	
    	str=new Vector<String>();
   	    BufferedReader in;
   		try {
   			in = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("countries.txt")));
   		
   	        String line = in.readLine();
   	        int index = 0;
   	        while (line != null) {

   	            str.add(line);
   	            line = in.readLine();
   	        }
   	        
   	        
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
           
   		
   		 strFlags=new Vector<String>();
   		    BufferedReader inF;
   			try {
   				inF = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("flags.txt")));
   			
   		        String line = inF.readLine();
   		        int index = 0;
   		        while (line != null) {

   		        	strFlags.add(line);
   		            line = inF.readLine();
   		        }
   		        
   		        
   			} catch (IOException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
    	
    }
    
    
    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
    
    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };
    
   
}