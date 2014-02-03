package com.yahoo.apps.stonetwitrapp;

import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.apps.stonetwitrapp.db.Utils;
import com.yahoo.apps.stonetwitrapp.fragments.HomeTimelineFragment;
import com.yahoo.apps.stonetwitrapp.fragments.MentionsFragment;
import com.yahoo.apps.stonetwitrapp.fragments.TweetsListFragment;
import com.yahoo.apps.stonetwitrapp.models.Tweet;
import com.yahoo.apps.stonetwitrapp.models.User;
import com.yahoo.apps.stonetwitrapp.network.Connectivity;

public class HomeTimelineActivity extends FragmentActivity implements TabListener {
	private static final int REQUEST_CODE_COMPOSE = 10000;
	private static final int REQUEST_CODE_PROFILE = 10001;
	private User loggedInUser;
	TweetsListFragment tweetsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		
		setUpNavigationTabs();
		
		if (Connectivity.getInstance(this).isOnline()) {
			MyTwitterApp.getRestClient().getLogedInUser(new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONObject jsonUser) {
					loggedInUser = User.fromJson(jsonUser);
					Log.d("DEBUG", "user name: " + loggedInUser.getName() + " screen name: " + loggedInUser.getScreenName());
					SharedPreferences pref = getSharedPreferences("MyPrefGroup", MODE_PRIVATE);
					Editor edit = pref.edit();
					edit.putString("userId", loggedInUser.getUserIdStr());
					edit.putString("username", loggedInUser.getName());
					edit.putString("screen_name", loggedInUser.getScreenName());
					edit.putString("profile_image_url", loggedInUser.getProfileImageUrl());
					edit.commit();
					Log.d("DEBUG", "saved the logegedIn user info to preferences");
				}

			});
		} else {
			offlineTimeLine();
		}
		
	}
	
	private void setUpNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment")
				.setTabListener(this);
		
		Tab tabMentions = actionBar.newTab().setText("@ Mentions").setTag("MentionsTimelineFragment")
				.setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
		
	}

	private void offlineTimeLine() {
		Log.d("DEBUG", "no network, refresh home timeline offline");
		
		List<Tweet> tweets = Utils.retrieveAllTweetsFromDB();
		ListView lvTweets = (ListView)findViewById(R.id.lvTweets);
		TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);			
		lvTweets.setAdapter(adapter);
		
		for (Tweet tweet : tweets) {
			Log.d("DEBUG", "user: " + tweet.getUser().getName() + " text: " + tweet.getBody());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_timeline, menu);
	    return true;
	}


	public void onClickComposeMenu(MenuItem miCompose) {
    	Intent i = new Intent(HomeTimelineActivity.this, ComposeNewTweetActivity.class);
    	startActivityForResult(i, REQUEST_CODE_COMPOSE);
	}
	
	public void onClickProfile(MenuItem miProfile) {
		Intent i = new Intent(HomeTimelineActivity.this, ProfileActivity.class);
    	startActivityForResult(i, REQUEST_CODE_PROFILE);	
	}
	
	private void refreshHomeTimeLine() {
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data /* return Intent from another activity */) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_COMPOSE) {
    	     // Extract name value from result extras
    	     String operation = data.getExtras().getString("operation");
    	     if ("tweet".equals(operation)) {
    	    	Log.d("DEBUG", "new tweet sent, refresh the timeline");
    	    	 refreshHomeTimeLine();
    	     }
    	  }

     }

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		FragmentManager fmMgr = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = fmMgr.beginTransaction();
		
		if (tab.getTag().equals("HomeTimelineFragment")) {
			fts.replace(R.id.flContainer, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.flContainer, new MentionsFragment());
		}
		fts.commit();
		
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		
	}


}
