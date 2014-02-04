package com.yahoo.apps.stonetwitrapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.apps.stonetwitrapp.EndlessScrollListener;
import com.yahoo.apps.stonetwitrapp.MyTwitterApp;
import com.yahoo.apps.stonetwitrapp.models.Tweet;

public class UserTimelineFragment extends TweetsListFragment {
	private String userId;
	
	public static UserTimelineFragment newInstance(String userId) {
		UserTimelineFragment userTimeline = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        userTimeline.setArguments(args);
        return userTimeline;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userId = getArguments().getString("userId", "");
		
		//get the handle of the activity this fragment attaches to
		//FragmentActivity activity = getActivity();
		
		MyTwitterApp.getRestClient().getUserTimeLine(userId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", "retrieve user timeline successfully");
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				getTweetsAdapter().addAll(tweets);
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				Log.d("DEBUG", "handleFailureMessage");
			}
		});
	}

	@Override
	EndlessScrollListener getScrollListener() {
		// not implement yet
		return null;
	}
	
	

}
