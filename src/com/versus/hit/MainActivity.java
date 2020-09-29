package com.versus.hit;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.ActionProvider;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.versus.hit.LastPlayed.LoadLastPlayedRadios;
import com.versus.hit.RadioStream.insertTask;

	
	public class MainActivity extends SherlockFragmentActivity implements OnClickListener {
	

	public boolean isProgressFinished = false;
	public ProgressDialog pDialog;
	public MenuItem menuSearch;
	Intent myintent;
	public int c1=0,c2=0,c3=0,c4=0,c5=0;
	public MenuItem menuShare;
	ScrollView scrollview;
	MainActivity activity;
	MainActivity activity2 = this;
	CharSequence countrytext;
	private int NOTFICATION_ID = 1;
	Context context;
	// Declare Variables
	Adapter adapter;
	ActionBar mActionBar;
	ViewPager mPager;
	Tab tab;
	EditText editsearch;
	public Vector<String> str;
    public Vector<String> strFlags;
	LazyAdapter sadapter;
	public static ArrayList<HashMap<String, String>> AllproductsList;
	private CharSequence mTitle;
	CountryList clist = new CountryList();
	SelectRadio rlist = new SelectRadio();
	public static RadioStream rs = new RadioStream();
	LastPlayed lp = new LastPlayed();
	MostPopular mp = new MostPopular();
	Categories catgr = new Categories();
   	Favorites fav = new Favorites();
   	Pref pref = new Pref();
   	public static Vector<String> laststr = new Vector<String>();
   	public static Vector<String> lasturl = new Vector<String>();
    public static Vector<String> laststrFlags = new Vector<String>();
	public static String Email;
	// url to get all products list
	public static String url_all_products = "http://www.androidmusicdownload.com/radio/getAllRadios.php";
	public int scrSize;
	ListView list;
	
	FragmentManager fm = getSupportFragmentManager();
	private DrawerLayout mDrawerLayout;
	   private ListView mDrawerList;
	   private ActionBarDrawerToggle mDrawerToggle;
	   private String[] mPlanetTitles;
	   
	   public static final String TAG = TabbedFragment.class.getSimpleName();
		ViewPagerAdapter mSectionsPagerAdapter;
	    
	    public static TabbedFragment newInstance() {
	        return new TabbedFragment();
	    }
	    
	    @Override
	    public void onPause(){

	        super.onPause();
	        if ((pDialog != null) && pDialog.isShowing()) {
	            pDialog.dismiss();
	        }
	    }
	    
	    
	    @Override
	    protected void onStop() {
	        super.onStop();
	        if ((pDialog != null) && pDialog.isShowing()) {
	            pDialog.dismiss();
	        }
	    }
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);
		
		if(AllproductsList == null)
		{
		
		AllproductsList = new ArrayList<HashMap<String, String>>();
		// Loading products in Background Thread
		
		}
		
		new LoadAllRadios().execute();
		
		
		myintent = getIntent();
		Bundle extras= myintent.getExtras();
		if(extras != null){
			
		String Rname = myintent.getStringExtra("radio_name");
		String Rlogo = myintent.getStringExtra("radio_logo");
		String Rstream = myintent.getStringExtra("radio_stream");
		String Rid = myintent.getStringExtra("radio_id");
		
		goToStream(Rname, Rstream, Rlogo, Rid);
		}
		
		//scrollview = (ScrollView)findViewById(R.id.container_scroll_view);
	/*	ImageButton sports = (ImageButton) findViewById(R.id.sports);
		ImageButton blues = (ImageButton) findViewById(R.id.blues);
		ImageButton news = (ImageButton) findViewById(R.id.news);
		ImageButton classical = (ImageButton) findViewById(R.id.classical);
		ImageButton culture = (ImageButton) findViewById(R.id.culture);
		ImageButton dance = (ImageButton) findViewById(R.id.dance);
		ImageButton education = (ImageButton) findViewById(R.id.education);
		ImageButton entertainment = (ImageButton) findViewById(R.id.entertainment);
		ImageButton hiphop = (ImageButton) findViewById(R.id.hiphop);
		ImageButton jazz = (ImageButton) findViewById(R.id.jazz);
		ImageButton altmis = (ImageButton) findViewById(R.id.altmis);
		ImageButton yetmis = (ImageButton) findViewById(R.id.yetmis);
		ImageButton seksen = (ImageButton) findViewById(R.id.seksen);
		ImageButton hits = (ImageButton) findViewById(R.id.hits);
		ImageButton mix = (ImageButton) findViewById(R.id.mix);
		ImageButton talk = (ImageButton) findViewById(R.id.talk);
		ImageButton pop = (ImageButton) findViewById(R.id.pop);
		ImageButton religion = (ImageButton) findViewById(R.id.religion);
		ImageButton rb = (ImageButton) findViewById(R.id.rb);
		ImageButton rockroll = (ImageButton) findViewById(R.id.rockroll);
	*/
		scrSize = getScreenSize();
		 if(scrSize == 540){
			 /*
			sports.setImageResource(R.drawable.sports1540);
			sports.invalidate();
			blues.setImageResource(R.drawable.blues1540);
			blues.invalidate();
			news.setImageResource(R.drawable.news1540);
			news.invalidate();
			rockroll.setImageResource(R.drawable.rockroll1540);
			rockroll.invalidate();
			classical.setImageResource(R.drawable.classical1540);
			classical.invalidate();
			culture.setImageResource(R.drawable.culture1540);
			culture.invalidate();
			dance.setImageResource(R.drawable.dance1540);
			dance.invalidate();
			education.setImageResource(R.drawable.education1540);
			education.invalidate();
			entertainment.setImageResource(R.drawable.entertainment1540);
			entertainment.invalidate();
			hiphop.setImageResource(R.drawable.hiphop1540);
			hiphop.invalidate();
			jazz.setImageResource(R.drawable.jazz1540);
			jazz.invalidate();
			altmis.setImageResource(R.drawable.altmis1540);
			altmis.invalidate();
			yetmis.setImageResource(R.drawable.yetmis1540);
			yetmis.invalidate();
			seksen.setImageResource(R.drawable.seksen1540);
			seksen.invalidate();
			hits.setImageResource(R.drawable.hits1540);
			hits.invalidate();
			mix.setImageResource(R.drawable.mix1540);
			mix.invalidate();
			talk.setImageResource(R.drawable.talk1540);
			talk.invalidate();
			pop.setImageResource(R.drawable.pop1540);
			pop.invalidate();
			religion.setImageResource(R.drawable.religion1540);
			religion.invalidate();
			rb.setImageResource(R.drawable.rb1540);
			rb.invalidate();
			rockroll.setImageResource(R.drawable.rockroll1540);
			rockroll.invalidate();*/
		
		}
		 /*
		 rb.setOnClickListener(this);
		 rockroll.setOnClickListener(this);
		 religion.setOnClickListener(this);
		 pop.setOnClickListener(this);
		 talk.setOnClickListener(this);
		 mix.setOnClickListener(this);
		 hits.setOnClickListener(this);
		 seksen.setOnClickListener(this);
		 yetmis.setOnClickListener(this);
		 altmis.setOnClickListener(this);
		 jazz.setOnClickListener(this);
		 hiphop.setOnClickListener(this);
		 entertainment.setOnClickListener(this);
		 education.setOnClickListener(this);
		 dance.setOnClickListener(this);
		 culture.setOnClickListener(this);
		 classical.setOnClickListener(this);
		 blues.setOnClickListener(this);
		 sports.setOnClickListener(this);
		 news.setOnClickListener(this);
		
		 */
		/* get email */
		 
	/*	 Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
         Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
         for (Account account : accounts) {
         if (emailPattern.matcher(account.name).matches()) {
           Email = account.name;
          
         }
         }
         */
		 
		list=(ListView)findViewById(R.id.lister);
		  // OnClick
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {	
				// getting values from selected ListItem
				String radioName = ((TextView) v.findViewById(R.id.text)).getText()
						.toString();
			
				String radioStream = ((TextView) v.findViewById(R.id.streamlink)).getText()
						.toString();
				
				String logoUrl = ((TextView) v.findViewById(R.id.logo)).getText()
						.toString();
				
				
				String radioid = ((TextView) v.findViewById(R.id.radioid)).getText()
						.toString();
				
				list.setVisibility(View.GONE);
				if (fm.getBackStackEntryCount() == 0)
					goToStream(radioName,radioStream,logoUrl,radioid);
				else
					rs.streamInit(radioName,radioStream,logoUrl,radioid);
				menuSearch.collapseActionView();
				//Intent i = new Intent(getActivity(), RadioStream.class);
				//i.putExtra("RADIO",radioName);
				//i.putExtra("STREAM",radioStream);
				//i.putExtra("LOGO",logoUrl);
				//i.putExtra("USERNAME", currentUser);
				// starting new activity and expecting some response back
				//startActivityForResult(i, 100);
				
				
			     }
			});
		
		//menu
		mTitle = getResources().getString(R.string.app_name);
	    mPlanetTitles = getResources().getStringArray(R.array.planets_array);
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    
	      // set a custom shadow that overlays the main content when the drawer
	      // opens
	      // set up the drawer's list view with items and click listener
	      mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
	      mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	      // enable ActionBar app icon to behave as action to toggle nav drawer
	      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	      getSupportActionBar().setHomeButtonEnabled(true);
	      getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
	      // ActionBarDrawerToggle ties together the the proper interactions
	      // between the sliding drawer and the action bar app icon
	      mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
	      mDrawerLayout, /* DrawerLayout object */
	      R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
	      R.string.drawer_open, /* "open drawer" description for accessibility */
	      R.string.drawer_close /* "close drawer" description for accessibility */
	      ) {
	      };
	      mDrawerLayout.setDrawerListener(mDrawerToggle);

	      
	   // Create first Tab
			
	      //end
		
	}
	 public void onClick(View v) {
		 
		 scrollview.setVisibility(View.GONE);
		 c5++;
		 
		 /*
		  switch (v.getId()) {

		  	case R.id.sports:
		  		selectCategory("Sports");
		  		break;
		
		  	case R.id.blues:
		  		selectCategory("Blues");
		  		break;
		
		  	case R.id.news:
		  		selectCategory("News");
		  		break;
		
		  	case R.id.rockroll:
		  		selectCategory("Rock");
		  		break;
		   
		  	case R.id.culture:
		  		selectCategory("Culture");
		  		break;
		  		
		  	case R.id.classical:
		  		selectCategory("Classical");
		  		break;
		
		  	case R.id.dance:
		  		selectCategory("Dance");
		  		break;
		
		  	case R.id.education:
		  		selectCategory("Education");
		  		break;
		
		  	case R.id.entertainment:
		  		selectCategory("Entertainment");
		  		break;
			 
		  	case R.id.hiphop:
		  		selectCategory("Hiphop");
			   break;
		
			case R.id.jazz:
				selectCategory("Jazz");
			   break;
		
			case R.id.altmis:
				selectCategory("60");
			   break;
		
			case R.id.yetmis:
				selectCategory("70");
			   break;
			   
			  case R.id.seksen:
				  selectCategory("80");
			   break;
		
			  case R.id.hits:
				  selectCategory("Hits");
				break;
		
			  case R.id.mix:
				  selectCategory("Mix");
				 break;
		
			  case R.id.talk:
				  selectCategory("Talk");
				 break;
				 
			  case R.id.pop:
				  selectCategory("Pop");
				  break;
		
			case R.id.religion:
				selectCategory("Religion");
				break;
		
			case R.id.rb:
				selectCategory("Rb");
				break;
		  }*/
		
		 }


		public int getScreenSize() {
			WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;

			return width;
		}
	
	
	/*private void sendNotification(CharSequence contentTitle,CharSequence contentText) {
		int icon = R.drawable.icon;
		CharSequence tickerText = getResources().getString(R.string.app_name);
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		// use the default notification vibrate (requires the VIBRATE
		// permission)

		// use the default notification lights
		notification.defaults |= Notification.DEFAULT_LIGHTS;

		// notification is canceled when it is clicked by the user
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// the audio will be repeated until the notification is cancelled or the
		// notification window is opened
		notification.flags |= Notification.FLAG_INSISTENT;

		Intent notifyIntent = new Intent(this,
				MainActivity.class);

		PendingIntent intent = PendingIntent.getActivity(
				MainActivity.this, 0, notifyIntent,
				android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

		notification
				.setLatestEventInfo(this, contentTitle, contentText, intent);

		mNotificationManager.notify(NOTFICATION_ID, notification);

	}

	private void cancelNotification() {
		mNotificationManager.cancel(NOTFICATION_ID);	
	}*/
	
	   /* Called whenever we call invalidateOptionsMenu() */
	   @Override
	   public boolean onPrepareOptionsMenu(Menu menu) {
	      // If the nav drawer is open, hide action items related to the content
	      // view
	      boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	      return super.onPrepareOptionsMenu(menu);
	   }

	   @Override
	   public boolean onOptionsItemSelected(final MenuItem item) {
	      // The action bar home/up action should open or close the drawer.
	      // ActionBarDrawerToggle will take care of this.
		   setTitle(getResources().getString(R.string.app_name));
	      if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item))) { 
	         return true; 
	      }
	      
		return false;

	   }
	   
	   private android.view.MenuItem getMenuItem(final MenuItem item) {
	      return new android.view.MenuItem() {
	         @Override
	         public int getItemId() {
	            return item.getItemId();
	         }

	         public boolean isEnabled() {
	            return true;
	         }

	         @Override
	         public boolean collapseActionView() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public boolean expandActionView() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public ActionProvider getActionProvider() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public View getActionView() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public char getAlphabeticShortcut() {
	            // TODO Auto-generated method stub
	            return 0;
	         }

	         @Override
	         public int getGroupId() {
	            // TODO Auto-generated method stub
	            return 0;
	         }

	         @Override
	         public Drawable getIcon() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public Intent getIntent() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public ContextMenuInfo getMenuInfo() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public char getNumericShortcut() {
	            // TODO Auto-generated method stub
	            return 0;
	         }

	         @Override
	         public int getOrder() {
	            // TODO Auto-generated method stub
	            return 0;
	         }

	         @Override
	         public SubMenu getSubMenu() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public CharSequence getTitle() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public CharSequence getTitleCondensed() {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public boolean hasSubMenu() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public boolean isActionViewExpanded() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public boolean isCheckable() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public boolean isChecked() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public boolean isVisible() {
	            // TODO Auto-generated method stub
	            return false;
	         }

	         @Override
	         public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setActionView(View view) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setActionView(int resId) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setCheckable(boolean checkable) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setChecked(boolean checked) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setEnabled(boolean enabled) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setIcon(Drawable icon) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setIcon(int iconRes) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setIntent(Intent intent) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setNumericShortcut(char numericChar) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public void setShowAsAction(int actionEnum) {
	            // TODO Auto-generated method stub

	         }

	         @Override
	         public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setTitle(CharSequence title) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setTitle(int title) {
	            // TODO Auto-generated method stub
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setTitleCondensed(CharSequence title) {
	            // TODO Auto-generated method stub
	        	 
	            return null;
	         }

	         @Override
	         public android.view.MenuItem setVisible(boolean visible) {
	            // TODO Auto-generated method stub
	            return null;
	         }
	      };
	   }
	   
	   public void selectCategory(String str){
		   
		   FragmentTransaction ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putString("category",str);
			catgr.setArguments(bundle);
			ft.replace(R.id.content_frame, catgr);
			ft.addToBackStack("Frags");
			ft.commit();
			menuSearch.setVisible(false);
		   
	   }
	   
	   public void goToNextPage(String str) {
		   
			FragmentTransaction ft = fm.beginTransaction();
			Bundle bundle = new Bundle();
			countrytext = str;
			bundle.putString("country",str);
			rlist.setArguments(bundle);
			ft.replace(R.id.content_frame, rlist);
			ft.addToBackStack("Frags");
			ft.commit();
			
			menuSearch.setVisible(false);
		   }
	   
	   public void goToStream(String str,String str2,String str3,String str4) {
		   	FragmentTransaction ft = fm.beginTransaction();	
	    	
		   	//
		   	//sendNotification(str,countrytext);
		   	//Son dinlenenlere ekle
		   	
		    //lp.new LoadLastPlayedRadios(this).execute(str4);
		   	
		   	setTitle("Playing now");
			Bundle bundle = new Bundle();
			bundle.putStringArray("key",new String[]{str, str2,str3,str4});
			rs.setArguments(bundle);
			ft.replace(R.id.content_frame, rs);
			ft.addToBackStack("Frags");
			ft.commit();
			menuSearch.setVisible(true);
			menuShare.setVisible(true);
		   }
	   
	   /* The click listner for ListView in the navigation drawer */
	   private class DrawerItemClickListener implements ListView.OnItemClickListener {
	      @Override
	      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	 if (fm.getBackStackEntryCount() > 0)
	  			   fm.popBackStack("Frags", FragmentManager.POP_BACK_STACK_INCLUSIVE);
	         selectItem(position);
	         switch(position) {
	         case 0:
	        	FragmentTransaction ft = fm.beginTransaction();
	 	 	  	ft.replace(R.id.content_frame, clist);
	 	 	  if (fm.getBackStackEntryCount() == 0)
	 	 	  		ft.addToBackStack("Frag");
	 	 	  	if(c2 == 1 || c3 == 1 || c4 == 1 || c5>0){
	 	 	  		ft.addToBackStack("Frag");
	 	 	  		c1=0;
	 	 	  		c2=0;
	 	 	  		c3=0;
	 	 	  		c4=0;
	 	 	  	}
	 	 	  	ft.commit();
	 	 	  	menuSearch.setVisible(false);
	 	 	  	c1++;
	             break;
	         case 1:
	        	 FragmentTransaction ft1 = fm.beginTransaction();
	        	 ft1.replace(R.id.content_frame, mp);
	        	 if (fm.getBackStackEntryCount() == 0)
	        		 ft1.addToBackStack("Frag");
	        	 if(c1 == 1 || c3 == 1 || c4 == 1 || c5>0){
		 	 	  		ft1.addToBackStack("Frag");
		 	 	  		c1=0;
		 	 	  		c2=0;
		 	 	  		c3=0;
		 	 	  		c4=0;
		 	 	  	}
	        	 ft1.commit();	
	        	 menuSearch.setVisible(false);
	        	 c2++;
	             break;
	         case 2:
	        	 FragmentTransaction ft2 = fm.beginTransaction();
	        	 ft2.replace(R.id.content_frame, fav);
	        	 if (fm.getBackStackEntryCount() == 0)
	        		 ft2.addToBackStack("Frag");
	        	 if(c2 == 1 || c1 == 1 || c4 == 1 || c5>0){
		 	 	  		ft2.addToBackStack("Frag");
		 	 	  		c1=0;
		 	 	  		c2=0;
		 	 	  		c3=0;
		 	 	  		c4=0;
		 	 	  	}
	        	 ft2.commit();	
	        	 menuSearch.setVisible(false);
	        	 c3++;
	             break;
	         case 3:
	        	 FragmentTransaction ft3 = fm.beginTransaction();
	        	 ft3.replace(R.id.content_frame, lp);
	        	 if (fm.getBackStackEntryCount() == 0)
	        		 ft3.addToBackStack("Frag");
	        	 if(c2 == 1 || c3 == 1 || c1 == 1 || c5>0){
		 	 	  		ft3.addToBackStack("Frag");
		 	 	  		c1=0;
		 	 	  		c2=0;
		 	 	  		c3=0;
		 	 	  		c4=0;
		 	 	  	}
	        	 ft3.commit();
	        	 menuSearch.setVisible(false);
	        	 c4++;
	             break;
	         }
	         
	         mDrawerLayout.closeDrawer(mDrawerList);
	      }
	   }

	   private void selectItem(int position) {
	      // update the main content by replacing fragments
	      
	      // update selected item and title, then close the drawer
	      mDrawerList.setItemChecked(position, true);
	      setTitle(mPlanetTitles[position]);
	      mDrawerLayout.closeDrawer(mDrawerList);
	   }

	   public void setTitle(CharSequence t) {
	      getSupportActionBar().setTitle(t);
	   }

	   /**
	    * When using the ActionBarDrawerToggle, you must call it during
	    * onPostCreate() and onConfigurationChanged()...
	    */

	   @Override
	   protected void onPostCreate(Bundle savedInstanceState) {
	      super.onPostCreate(savedInstanceState);
	      // Sync the toggle state after onRestoreInstanceState has occurred.
	      mDrawerToggle.syncState();
	   }

	   @Override
	   public void onConfigurationChanged(Configuration newConfig) {
	      super.onConfigurationChanged(newConfig);
	      // Pass any configuration change to the drawer toggls
	      mDrawerToggle.onConfigurationChanged(newConfig);
	   }
	   @Override
	    public void onBackPressed()
	    {
		   
		   	menuShare.setVisible(false);
		   	menuSearch.setVisible(false);
		    if (fm.getBackStackEntryCount() > 0) {
		        fm.popBackStack();
		        if (fm.getBackStackEntryCount() == 1){
		        	menuSearch.setVisible(true);
		        	setTitle(getResources().getString(R.string.app_name));
		        	scrollview.setVisibility(View.VISIBLE);
		        	}
		        
		    } else {
			    	
		    	moveTaskToBack(true);  
		    }
	    }
	   
	// Create the options menu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Get the options menu view from menu.xml in menu folder
			getSupportMenuInflater().inflate(R.menu.menu, menu);

			// Locate the EditText in menu.xml
			editsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();

			// Capture Text in EditText
			editsearch.addTextChangedListener(textWatcher);

			// Show the search menu item in menu.xml
			menuSearch = menu.findItem(R.id.menu_search);
			
			menuSearch.setOnActionExpandListener(new OnActionExpandListener() {

				// Menu Action Collapse
				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					// Empty EditText to remove text filtering
					
					editsearch.setText("");
					editsearch.clearFocus();
					list.setVisibility(View.GONE);
					return true;
				}

				// Menu Action Expand
				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {
					// Focus on EditText
					editsearch.requestFocus();
					// Force the keyboard to show on EditText focus
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					return true;
				}
			});
			
			// Show the settings menu item in menu.xml
						menuShare = menu.findItem(R.id.menu_item_share);
						menuShare.setVisible(false);
						// Capture menu item clicks
						menuShare.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Intent shareIntent = new Intent();
							    shareIntent.setAction(Intent.ACTION_SEND);
							    shareIntent.setType("text/plain");
							    String url = "http://bando.fm";
							    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bando");
							    shareIntent.putExtra("radio_name", rs.radioName);
							    shareIntent.putExtra("radio_logo", rs.radioLogo);
							    shareIntent.putExtra("radio_stream", rs.radioStream);
							    shareIntent.putExtra("radio_id", rs.favradioid);
							    shareIntent.putExtra(Intent.EXTRA_TEXT,rs.radioName + " radio now playing at " + url + " Listen Now!");
							    startActivity(Intent.createChooser(shareIntent, "Share via"));
								return false;
							}

						});

			return true;
		}

		// EditText TextWatcher
		private TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				
				String text = editsearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				
				sadapter.filter(text);
				
				if(editsearch.getText().toString().length() == 0){
					list.setVisibility(View.GONE);
				}else
					list.setVisibility(View.VISIBLE);

				
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			
			}

		};

		
		/**
		 * Background Async Task to Load all product by making HTTP Request
		 * */
		class LoadAllRadios extends AsyncTask<String, String, String> {
			//public SherlockFragmentActivity activity;
			LayoutInflater inf;
		    ListView list1;
		    LazyAdapter adapter;
		    public Vector<String> str;
		    public Vector<String> strFlags;
		    public String getCountry;
		    Context context;
		    //Veritabaný iþlemleri
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
			

				// Creating JSON Parser object
				JSONParser jParser = new JSONParser();

				//ArrayList<HashMap<String, String>> productsList;
				
				// url to get all products list
				//public static String url_all_products = "http://www.androidmusicdownload.com/radio/getAllRadios.php";

				// JSON Node names
				private static final String TAG_SUCCESS = "success";
				private static final String TAG_PRODUCTS = "radiolar";
				private static final String TAG_ID = "id";
				private static final String TAG_COUNTRY = "country";
				private static final String TAG_RADIO = "radio";
				private static final String TAG_STREAM = "stream";
				private static final String TAG_LOGO = "logo";
				private static final String TAG_COUNT = "count";
				private static final String TAG_TUR = "tur";
				
			
				// products JSONArray
				JSONArray products = null;
				JSONArray productsY = null;
					
				//Veritabaný

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(MainActivity.this);
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
				
				if(!(AllproductsList.size() > 0))
				{
				
				String newUrl = url_all_products;
				
				JSONObject json = jParser.makeHttpRequest(newUrl, "GET", params);
				
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
							String count = c.getString(TAG_COUNT);
							String tur = c.getString(TAG_TUR);
							imageUrl = logo;

							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_ID, id);
							map.put(TAG_COUNTRY, country);
							map.put(TAG_RADIO, radio);
							map.put(TAG_STREAM, stream);
							map.put(TAG_LOGO,logo);
							map.put(TAG_COUNT,count);
							map.put(TAG_TUR,tur);

							// adding HashList to ArrayList
							AllproductsList.add(map);
						}
					} else {
						Toast.makeText(activity.getApplicationContext(), "JSON kısmında Bir Hata olmalı", Toast.LENGTH_LONG);
						
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
				
				}
//					try {
//		               bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://www.androidmusicdownload.com"+imageUrl).getContent());
//		               downImageUrl = "http://www.androidmusicdownload.com"+imageUrl;
//				} catch (Exception e) {
//		              e.printStackTrace();
//		        }
		      return null;
				
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
	
				
				  try {
				        if ((pDialog != null) && pDialog.isShowing()) {
				            pDialog.dismiss();
				        }
				    } catch (final IllegalArgumentException e) {
				        // Handle or log or ignore
				    } catch (final Exception e) {
				        // Handle or log or ignore
				    } finally {
				        pDialog = null;
				    }  
				
				isProgressFinished = true;
				  installCountriesAndFlags();
				  sadapter = new LazyAdapter(AllproductsList,MainActivity.this,str,strFlags);
				  list.setVisibility(View.GONE);
				  list.setAdapter(sadapter);
		    	}
			
			public void installCountriesAndFlags()
		    {
		    	
		    	str=new Vector<String>();
		    	strFlags=new Vector<String>();
		    	
			 	for (HashMap<String, String> map : AllproductsList) {
		    	    for (String key : map.keySet()) {       	    
		    	    	str.add(map.get("radio").toString());
		    	    	strFlags.add(map.get("logo").toString());    
		    	    	break;
		    	   } 

			 	}
			 	
		    }
			
			}
		
}
