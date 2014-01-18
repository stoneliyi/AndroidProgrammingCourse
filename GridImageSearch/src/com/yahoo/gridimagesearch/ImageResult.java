package com.yahoo.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private String fullUrl;
	private String thumbUrl;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5331445091208860642L;
	
	public ImageResult(JSONObject jsObj) {
		try {
			this.fullUrl = jsObj.getString("url");
			this.thumbUrl = jsObj.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;		
		}
		
	}
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	
	public String toString() {
		return this.thumbUrl;
	}

	public static ArrayList<ImageResult> imageResultListFromJsonArray(JSONArray imageJsonResults) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < imageJsonResults.length(); i++) {
			try {
				results.add(new ImageResult(imageJsonResults.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

}
