package com.yahoo.apps.stonetwitrapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

@Table(name = "Tweet")
public class Tweet extends Model {
	@Column(name = "Body")
	public String body;
	
	@Column(name = "User")
    public User user;
	
	private long tweetId;
	private boolean favorited;
	private boolean retweeted;
	

	public Tweet() {
		super();
	}
	
    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
        	tweet.body = jsonObject.getString("text");
        	tweet.tweetId = jsonObject.getLong("id");
        	tweet.favorited = jsonObject.getBoolean("favorited");
        	tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}