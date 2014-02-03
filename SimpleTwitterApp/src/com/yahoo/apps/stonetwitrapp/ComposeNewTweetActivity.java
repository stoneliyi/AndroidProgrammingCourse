package com.yahoo.apps.stonetwitrapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.stonetwitrapp.network.Connectivity;

public class ComposeNewTweetActivity extends Activity {
	private TextView tvMyName;
	private ImageView ivMyProfile;
	private EditText etNewTweet;
	private Button btTweet;
	//private User me;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_new_tweet);

		tvMyName = (TextView)findViewById(R.id.tvMyName);
		ivMyProfile = (ImageView)findViewById(R.id.ivMyProfile);
		etNewTweet = (EditText)findViewById(R.id.etNewTweet);
		btTweet = (Button)findViewById(R.id.btTweet);
		btTweet.getBackground().setColorFilter(new LightingColorFilter(Color.BLUE, Color.BLUE));
		
		SharedPreferences pref = getSharedPreferences("MyPrefGroup", MODE_PRIVATE);
		String username = pref.getString("username", "");
		String screen_name = pref.getString("screen_name", "");
		tvMyName.setText(username + " @" + screen_name);
		if (Connectivity.getInstance(this).isOnline()) {
			ImageLoader.getInstance().displayImage(pref.getString("profile_image_url", ""), ivMyProfile);
		}
	}
	
	public void onClickTweet(View btTweet) {
		String tweetText = etNewTweet.getText().toString();
		if ("".equals(tweetText)) {
			Toast.makeText(getApplicationContext(), "oops, please write something to tweet", Toast.LENGTH_SHORT).show();
			return;
		}
		
		MyTwitterApp.getRestClient().postNewTweet(tweetText);


		Intent data = new Intent();
		data.putExtra("operation", "tweet" );
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();
	}
	
	public void onClickCancel(View btCancel) {
		Intent data = new Intent();
		data.putExtra("operation", "cancel" );
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_new_tweet, menu);
		return true;
	}

}
