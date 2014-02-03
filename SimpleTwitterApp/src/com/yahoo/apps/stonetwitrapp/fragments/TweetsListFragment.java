package com.yahoo.apps.stonetwitrapp.fragments;

import java.util.ArrayList;

import com.yahoo.apps.stonetwitrapp.R;
import com.yahoo.apps.stonetwitrapp.TweetsAdapter;
import com.yahoo.apps.stonetwitrapp.models.Tweet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TweetsListFragment extends Fragment {

	private TweetsAdapter adapter;
	private OnItemSelectedListener listener;

	// Define the events that the fragment will use to communicate
	public interface OnItemSelectedListener {
		public void onProfileImageSelected(String userId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnItemSelectedListener) {
			this.listener = (OnItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement TweetsListFragment.OnItemSelectedListener");
		}
	}
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tweets_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//get the handle of the activity this fragment attaches to
		//Activity activity = getActivity();
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getActivity(), tweets);			
		lvTweets.setAdapter(adapter);
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Tweet tweet = (Tweet)adapter.getItemAtPosition(position);
				String userId = tweet.getUser().getUserIdStr();
				if ("".equals(userId)) {
					Log.d("DEBUG", "user id is empty. there is something wrong. do nothing");
					return;
				}
				listener.onProfileImageSelected(userId);
			}  	
		});
	}
	
	
	public TweetsAdapter getTweetsAdapter() {
		return adapter;
	}

	
}
