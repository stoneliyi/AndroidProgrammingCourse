package com.yahoo.apps.stonetwitrapp;

import com.yahoo.apps.stonetwitrapp.fragments.UserInfoFragment;
import com.yahoo.apps.stonetwitrapp.fragments.UserTimelineFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		SharedPreferences pref = getSharedPreferences("MyPrefGroup", MODE_PRIVATE);
		String userId = pref.getString("userId", "");
		Log.d("DEBUG", "userId: " + userId);
		if ("".equals(userId)) {
			Log.d("DEBUG", "no userId retrieved, return");
			return;
		}
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserInfoFragment userInfoFragment = UserInfoFragment.newInstance(userId);
		UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(userId);
		ft.replace(R.id.flUserInfoContainer, userInfoFragment);
		ft.replace(R.id.flUserTweetsContainer, userTimelineFragment);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
