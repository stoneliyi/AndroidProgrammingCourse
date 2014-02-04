package com.yahoo.apps.stonetwitrapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "Ps4q2wsXtidGcVaOtqBXzw";       // Change this
    public static final String REST_CONSUMER_SECRET = "Jhm2MbEZTse1PwToebl7TYeCPbhiY2GxStuLb88WOs"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeLine(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        client.get(apiUrl, params, handler);
    }
    
    public void getHomeTimeLine(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        if (maxId != 0) {
        	params.put("max_id", Long.toString(maxId));
        }
        client.get(apiUrl, params, handler);
    }

    
   public void getLogedInUser(AsyncHttpResponseHandler handler) {
	   // https://api.twitter.com/1.1/account/verify_credentials.json
	   String apiUrl = getApiUrl("/account/verify_credentials.json");
	   client.get(apiUrl, null, handler);
    }
   
   public void postNewTweet(String tweetText) {
	   // https://api.twitter.com/1.1/statuses/update.json
	   String apiUrl = getApiUrl("/statuses/update.json");
	   RequestParams params = new RequestParams();
       params.put("status", tweetText);
	   client.post(apiUrl, params, null);
   }

	public void getMentionsTimeLine(JsonHttpResponseHandler jsonHttpResponseHandler) {
		String apiUrl = getApiUrl("/statuses/mentions_timeline.json");
		client.get(apiUrl, null, jsonHttpResponseHandler);		
	}
	
	public void getUserTimeLine(String userId, JsonHttpResponseHandler jsonHttpResponseHandler) {
		// /statuses/user_timeline.json 
		String apiUrl = getApiUrl("/statuses/user_timeline.json");
		RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("count", "25");
		client.get(apiUrl, params, jsonHttpResponseHandler);
	}
	
	public void getUserTimeLine(String userId, long maxId, JsonHttpResponseHandler jsonHttpResponseHandler) {
		// /statuses/user_timeline.json 
		String apiUrl = getApiUrl("/statuses/user_timeline.json");
		RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("count", "25");
        if (maxId != 0) {
        	params.put("max_id", Long.toString(maxId));
        }
		client.get(apiUrl, params, jsonHttpResponseHandler);
	}
	
	public void getUserInfo(String userId, JsonHttpResponseHandler jsonHttpResponseHandler) {
		// users/show.json 
		String apiUrl = getApiUrl("/users/show.json");
		RequestParams params = new RequestParams();
        params.put("user_id", userId);
		client.get(apiUrl, params, jsonHttpResponseHandler);
	}
   
}