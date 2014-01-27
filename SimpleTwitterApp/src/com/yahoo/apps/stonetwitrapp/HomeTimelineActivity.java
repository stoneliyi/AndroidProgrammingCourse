package com.yahoo.apps.stonetwitrapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
//import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.app.stonetwitrapp.network.Connectivity;
import com.yahoo.apps.stonetwitrapp.db.Utils;
import com.yahoo.apps.stonetwitrapp.models.Tweet;
import com.yahoo.apps.stonetwitrapp.models.User;

public class HomeTimelineActivity extends Activity {
	private static final int REQUEST_CODE = 10000;
	
	//TODO: store loggedInUser in local DB
	private User loggedInUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		
		if (Connectivity.getInstance(this).isOnline()) {
			MyTwitterApp.getRestClient().getLogedInSUser(new JsonHttpResponseHandler() {

				@Override
				public void onSuccess(JSONObject jsonUser) {
					loggedInUser = User.fromJson(jsonUser);
					Log.d("DEBUG", "user name: " + loggedInUser.getName() + " screen name: " + loggedInUser.getScreenName());
				}

			});
			refreshHomeTimeLine();
		} else {
			offlineTimeLine();
		}
		
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

	private void refreshHomeTimeLine() {
		MyTwitterApp.getRestClient().getHomeTimeLine(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", "refresh home timeline successfully");
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				ListView lvTweets = (ListView)findViewById(R.id.lvTweets);
				TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);			
				lvTweets.setAdapter(adapter);
				Utils.saveTweetsToDB(tweets, 100);

			}

			@Override
			public void onFailure(Throwable error, JSONObject jsonResponse) {
				Log.d("DEBUG", "onFailure with JsonObj");
				Log.d("DEBUG", error.toString());
				Log.d("DEBUG", jsonResponse.toString());
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				Log.d("DEBUG", "handleFailureMessage");
			}

			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.d("DEBUG", "onFailure with JsonArray");
			}			
			
		});
	}
	
	public void onClickComposeMenu(MenuItem miCompose) {
		//Toast.makeText(getApplicationContext(), "composing a new tweet", Toast.LENGTH_SHORT).show();
    	Intent i = new Intent(HomeTimelineActivity.this, ComposeNewTweetActivity.class);
    	i.putExtra("loggedInUser", loggedInUser);
    	startActivityForResult(i, REQUEST_CODE);
	}
	
	public void onClickRefresh(MenuItem miRefresh) {
		if (Connectivity.getInstance(this).isOnline()) {
			refreshHomeTimeLine();
		} else {
			offlineTimeLine();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data /* return Intent from another activity */) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    	     // Extract name value from result extras
    	     String operation = data.getExtras().getString("operation");
    	     if ("tweet".equals(operation)) {
    	    	Log.d("DEBUG", "new tweet sent, refresh the timeline");
    	    	 refreshHomeTimeLine();
    	     }
    	  }

     }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}

}
