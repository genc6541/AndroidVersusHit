package com.versus.hit;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
public class TabbedFragment extends SherlockFragmentActivity {
	
	ActionBar mActionBar;
	ViewPager mPager;
	Tab tab;
	FragmentManager fm = getSupportFragmentManager();
	
	public static final String TAG = TabbedFragment.class.getSimpleName();
	ViewPagerAdapter mSectionsPagerAdapter;
    
    public static TabbedFragment newInstance() {
        return new TabbedFragment();
    }
 
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabbed);
        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    	mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	
    	// Locate ViewPager in activity_main.xml
    	mPager = (ViewPager)findViewById(R.id.pager);
    	
    	ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
    		@Override
    		public void onPageSelected(int position) {
    			super.onPageSelected(position);
    			// Find the ViewPager Position
    			mActionBar.setSelectedNavigationItem(position);
    		}
    	};
    	mPager.setOnPageChangeListener(ViewPagerListener);
    	// Locate the adapter class called ViewPagerAdapter.java
    	final ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm);
    	
    	// Set the View Pager Adapter into ViewPager
    	mPager.setAdapter(viewpageradapter);
    	
    	// Capture tab button clicks
    	ActionBar.TabListener tabListener = new ActionBar.TabListener() {

    		@Override
    		public void onTabSelected(Tab tab, FragmentTransaction ft) {
    			// Pass the position on tab click to ViewPager
    			mPager.setCurrentItem(tab.getPosition());
    			
    		}

    		@Override
    		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    			// TODO Auto-generated method stub
    		}

    		@Override
    		public void onTabReselected(Tab tab, FragmentTransaction ft) {
    			// TODO Auto-generated method stub
    		}
    	};
    	tab = mActionBar.newTab().setText("Countries").setTabListener(tabListener);
    	mActionBar.addTab(tab);
    	
    	// Create second Tab
    	tab = mActionBar.newTab().setText("Categories").setTabListener(tabListener);
    	mActionBar.addTab(tab);
    	
    	// Create third Tab
    	tab = mActionBar.newTab().setText("Favorites").setTabListener(tabListener);
    	mActionBar.addTab(tab);		
         
    }	
}
