package com.homework;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import data.DatabaseHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    

    public static RennClient rennClient;
    private static final String APP_ID = "267198";

	private static final String API_KEY = "6b71a5532497440fb69c923f310e79ee";

	private static final String SECRET_KEY = "2b4cce8e9d4e4787b430077951540ebd";
    public static DatabaseHelper helper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	 FeedbackAgent agent = new FeedbackAgent(this);
    	 agent.sync();
    	helper=new DatabaseHelper(getApplicationContext(), "homework.db", 1);
    	
		rennClient = RennClient.getInstance(this);
		rennClient.init(APP_ID, API_KEY, SECRET_KEY);
		rennClient
				.setScope("read_user_blog read_user_photo read_user_status read_user_album "
						+ "read_user_comment read_user_share publish_blog publish_share "
						+ "send_notification photo_upload status_update create_album "
						+ "publish_comment publish_feed");

		rennClient.setTokenType("bearer");
                  
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment=null;
        switch (position+1) {
        case 1:
        	fragment=IndexFragment.newInstance(position+1);
        	break;
        case 2:
        	fragment=MyworkFragment.newInstance(position+1);
        	break;
        case 3:
        	fragment=TestFragment.newInstance(position+1);
        	break;
        case 4:
        	fragment=LessonFragment.newInstance(position+1);
        	break;
        case 5:
        	
        	break;
        }
        if(fragment==null)
        	fragment=PlaceholderFragment.newInstance(position+1);
        fragmentManager.beginTransaction().replace(R.id.container,fragment).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menuback));
        actionBar.setTitle(mTitle);       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
//            MenuItem item=menu.findItem(R.id.log);
//            if (rennClient.isLogin()) {
//    			item.setTitle("注销");
//    		} else {
//    			item.setTitle("人人账号登陆");
//    		}
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        if(id==R.id.fankui){
        	FeedbackAgent agent = new FeedbackAgent(this);
            agent.startFeedbackActivity();	
        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if(helper!=null){
    		helper.close();
    	}
    }
}
