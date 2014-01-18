package com.yahoo.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {
	private EditText etQuery;
	private GridView gvResults;
	private Button btSearch;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageArrayAdapter;
	
	private static final String imageSearchAPIBaseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&start=0&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        
        imageArrayAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageArrayAdapter);
        
        gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				ImageResult image = imageResults.get(position);
				Intent intent = new Intent(getApplicationContext(), FullImageViewActivity.class);
				intent.putExtra("imageObj", image);
				startActivity(intent);
			}
        	
		});
    }

    private void setupViews() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btSearch = (Button) findViewById(R.id.searchButton);    			
	}


	public void onClickImageSearch(View button) {
    	String query = etQuery.getText().toString();
    	final String apiUrl = imageSearchAPIBaseUrl + Uri.encode(query);
    	//Toast.makeText(getApplicationContext(), "search term: " + apiUrl, Toast.LENGTH_LONG).show();
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	Log.d("DEBUG", "making a api call");
    	client.get(apiUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageResults.clear();
					imageArrayAdapter.addAll(ImageResult.imageResultListFromJsonArray(imageJsonResults));
					// will throw exception
					// imageArrayAdapter.notify();
					Log.d("debug", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

    	});


	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
        return true;
    }
    
}
