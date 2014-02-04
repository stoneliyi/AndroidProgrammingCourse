package com.yahoo.apps.stonetwitrapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.apps.stonetwitrapp.EndlessScrollListener;
import com.yahoo.apps.stonetwitrapp.MyTwitterApp;
import com.yahoo.apps.stonetwitrapp.db.Utils;
import com.yahoo.apps.stonetwitrapp.models.Tweet;

import android.os.Bundle;
import android.util.Log;

public class HomeTimelineFragment extends TweetsListFragment {
	private long maxId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		maxId = Tweet.getMaxID();
		if (maxId == Long.MAX_VALUE) {
			maxId = 0;
		}
		
		queryHomeline(maxId);
	}

	private void queryHomeline(long maxId) {
		MyTwitterApp.getRestClient().getHomeTimeLine(maxId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", "refresh home timeline successfully");
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				getTweetsAdapter().addAll(tweets);
				Utils.saveTweetsToDB(tweets, 100);
			}


			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				Log.d("DEBUG", "handleFailureMessage");
			}
		});
	}
	
	class HomelineScrollListener extends EndlessScrollListener {   	
    	@Override
		public void onLoadMore(int page, int totalItemsCount) {
			Log.d("DEBUG", "scroll listener calling api for more results");
			maxId = Tweet.getMaxID();
			if (maxId == Long.MAX_VALUE) {
				maxId = 0;
			}
			queryHomeline(maxId);
		}
    }
	
	public EndlessScrollListener getScrollListener() {
		 return new HomelineScrollListener();
	 }

}
