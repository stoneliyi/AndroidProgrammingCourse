package com.yahoo.apps.stonetwitrapp.db;

import java.util.List;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.yahoo.apps.stonetwitrapp.models.Tweet;
import com.yahoo.apps.stonetwitrapp.models.User;

public class Utils {
	
	public static void cleanAllFromDB() {
		Log.d("DEBUG", "deleting all Tweets from DB");
		new Delete().from(Tweet.class).execute();
		new Delete().from(User.class).execute();
		
	}
	
	public static void saveTweetsToDB(List<Tweet> tweets, int limit) {
		cleanAllFromDB();
		for (Tweet tweet : tweets) {
			Log.d("DEBUG", "saving user to DB: " + tweet.getUser().getName());
			tweet.getUser().save();
			Log.d("DEBUG", "saving tweet to DB: " + tweet.getBody());
			tweet.save();
		}
	}
	
	public static List<Tweet> retrieveAllTweetsFromDB() {
		Log.d("DEBUG", "querying db to retrieve all tweets from db");
	    return new Select()
	        .from(Tweet.class)
	        .execute();
	}

}
