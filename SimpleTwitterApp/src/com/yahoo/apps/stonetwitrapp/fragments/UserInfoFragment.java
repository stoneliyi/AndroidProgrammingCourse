package com.yahoo.apps.stonetwitrapp.fragments;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.stonetwitrapp.MyTwitterApp;
import com.yahoo.apps.stonetwitrapp.R;
import com.yahoo.apps.stonetwitrapp.models.User;

public class UserInfoFragment extends Fragment {	
	private String userId;
	private User user;
	
	TextView username;
	TextView description;
	TextView followerCount;
	TextView followingCount;
	ImageView userProfileImage;
	
	public static UserInfoFragment newInstance(String userId) {
        UserInfoFragment userInfo = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        userInfo.setArguments(args);
        return userInfo;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		userId = getArguments().getString("userId", "");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//get the handle of the activity this fragment attaches to
		FragmentActivity activity = getActivity();
		
		username = (TextView)activity.findViewById(R.id.tvUsername);
		description = (TextView)activity.findViewById(R.id.tvUserDesription);
		followerCount = (TextView)activity.findViewById(R.id.tvFollowerCount);
		followingCount = (TextView)activity.findViewById(R.id.tvFollowingCount);
		userProfileImage = (ImageView) activity.findViewById(R.id.ivUserProfile);
		
		Log.d("DEBUG", "running twitter client to get user info: " + userId);
		MyTwitterApp.getRestClient().getUserInfo(userId, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonUser) {
				Log.d("DEBUG", "retrieve user info successfully");
				user = User.fromJson(jsonUser);
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), userProfileImage);
				username.setText(user.getName());
				description.setText(user.getDescription());
				followerCount.setText(user.getFollowersCount() + " Followers");
				followingCount.setText(user.getFriendsCount() + " Following");
			}

			@Override
			protected void handleFailureMessage(Throwable tr, String arg1) { 
				Log.d("DEBUG", "failed to get user info by userid " + userId + ". " + tr.getMessage());
			}
		});

		
	}

}
