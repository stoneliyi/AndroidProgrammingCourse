package com.yahoo.apps.stonetwitrapp.fragments;

import java.util.ArrayList;

import com.yahoo.apps.stonetwitrapp.R;
import com.yahoo.apps.stonetwitrapp.TweetsAdapter;
import com.yahoo.apps.stonetwitrapp.models.Tweet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TweetsListFragment extends Fragment {

	private TweetsAdapter adapter;

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
	}
	
	public TweetsAdapter getTweetsAdapter() {
		return adapter;
	}

	
}
