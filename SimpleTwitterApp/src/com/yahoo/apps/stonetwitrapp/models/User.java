package com.yahoo.apps.stonetwitrapp.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "Name")
	public String name;
	
	@Column(name = "ScreenName")
	public String screenName;
	
	private long userId;
	private String profileBgImageUrl;
	private int numTweets;
	private int followersCount;
	private int friendsCount;
	private String profileImageUrl;
	
	public User() {
		super();
	}
	
    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBgImageUrl;
    }

    public int getNumTweets() {
        return numTweets;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
        	u.name = json.getString("name");
        	u.userId = json.getLong("id");
        	u.screenName = json.getString("screen_name");
        	u.profileBgImageUrl = json.getString("profile_background_image_url");
        	u.profileImageUrl = json.getString("profile_image_url");
        	u.numTweets = json.getInt("statuses_count");
        	u.followersCount = json.getInt("followers_count");
        	u.friendsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public long getUserId() {
		return userId;
	}
}
