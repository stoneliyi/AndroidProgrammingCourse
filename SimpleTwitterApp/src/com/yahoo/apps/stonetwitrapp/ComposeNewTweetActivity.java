package com.yahoo.apps.stonetwitrapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.stonetwitrapp.models.User;

public class ComposeNewTweetActivity extends Activity {
	private TextView tvMyName;
	private ImageView ivMyProfile;
	private EditText etNewTweet;
	private User me;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_new_tweet);
		me = (User) getIntent().getSerializableExtra("loggedInUser");
		tvMyName = (TextView)findViewById(R.id.tvMyName);
		ivMyProfile = (ImageView)findViewById(R.id.ivMyProfile);
		etNewTweet = (EditText)findViewById(R.id.etNewTweet);
		
		tvMyName.setText(me.getName() + " @" + me.getScreenName());
		ImageLoader.getInstance().displayImage(me.getProfileImageUrl(), ivMyProfile);	
	}
	
	public void onClickTweet(View btTweet) {
		String tweetText = etNewTweet.getText().toString();
		if ("".equals(tweetText)) {
			Toast.makeText(getApplicationContext(), "oops, please write something to tweet", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			MyTwitterApp.getRestClient().postNewTweet(URLEncoder.encode(tweetText, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

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
