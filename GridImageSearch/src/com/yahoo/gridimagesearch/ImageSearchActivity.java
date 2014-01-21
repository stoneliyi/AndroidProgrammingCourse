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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {
	private EditText etQuery;
	private GridView gvResults;
	private Button btLoadMore;
	private String optionsQuery = "";
	private int start = 0;
	protected int nextStart;
	private int currentPageIndex;
	private int numberOfPages;
	
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageArrayAdapter;
	
	private static final String imageSearchAPIBaseUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
	private static int pageSize = 8;
	private static final int REQUEST_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        
        Log.d("DEBUG", "method onCreate called!");
        setupViews();
        
    	btLoadMore.setVisibility(View.INVISIBLE);
        
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
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				queryAPIForResults(etQuery.getText().toString(), page * pageSize, optionsQuery, false);				
			}
		});
    }

    private void setupViews() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btLoadMore = (Button) findViewById(R.id.btLoadMore);
	}


	public void onClickImageSearch(View button) {
		resetPagination();
		imageResults.clear();
    	queryAPIForResults(etQuery.getText().toString(), start, this.optionsQuery, true);
	}

	private void resetPagination() {
		start = 0;
		nextStart = 0;
		currentPageIndex = 0;
		numberOfPages = 0;
	}
	
	public void onClickLoadMore(View btLoadMore) {
		//Toast.makeText(getApplicationContext(), "loading more images...", Toast.LENGTH_SHORT).show();
		queryAPIForResults(etQuery.getText().toString(), nextStart, this.optionsQuery, true);

	}

	private void queryAPIForResults(String query, int start, String optionsQuery, final boolean clearResult) {
    	final String apiUrl = imageSearchAPIBaseUrl + Uri.encode(query) + "&start=" + start + "&" + optionsQuery;
    	
    	AsyncHttpClient client = new AsyncHttpClient();
    	Log.d("DEBUG", "making a api call to url: " + apiUrl);
    	client.get(apiUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				JSONObject resultCursor = null;
				
				try {				
					resultCursor = response.getJSONObject("responseData").getJSONObject("cursor");
					currentPageIndex = resultCursor.getInt("currentPageIndex");
					JSONArray pages = resultCursor.getJSONArray("pages");
					numberOfPages = pages.length();
					Log.d("DEBUG", "number of pages: " + numberOfPages + ", current index: " + currentPageIndex);
					if (currentPageIndex < numberOfPages - 1) {
						nextStart = pages.getJSONObject(currentPageIndex + 1).getInt("start");
						Log.d("DEBUG", "next start: " + nextStart);
						Log.d("DEBUG", "setting load more button visibility to: " + View.VISIBLE);
			    		//btLoadMore.setVisibility(View.VISIBLE);
						btLoadMore.setVisibility(View.INVISIBLE);
					} else {
						Log.d("DEBUG", "setting load more button visibility to: " + View.INVISIBLE);
			    		btLoadMore.setVisibility(View.INVISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					if (clearResult) {
						imageResults.clear();
					}
					imageArrayAdapter.addAll(ImageResult.imageResultListFromJsonArray(imageJsonResults));
					// will throw exception
					// imageArrayAdapter.notify();
					//Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

    	});
	}
	
    public void onClickAdvancedMenu(MenuItem mi) {
    	Intent i = new Intent(getApplicationContext(), AdvancedOptionsActivity.class);
    	startActivityForResult(i, REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    		Log.d("DEBUG", "advanced options set, filtering the result");
    		this.optionsQuery =  getOptionsQuery(data);
    		queryAPIForResults(etQuery.getText().toString(), this.start, this.optionsQuery, true);
   	  	}
    }
    
    private String getOptionsQuery(Intent data) {
    		String size = Uri.encode(data.getExtras().getString("image_size"));
	     	String color = Uri.encode(data.getExtras().getString("image_color"));
	     	String type = Uri.encode(data.getExtras().getString("image_type"));
	     	String site = Uri.encode(data.getExtras().getString("image_site"));
	 
	    return "imgsz=" + size + "&imgcolor=" + color + "&imgtype=" + type + "&as_sitesearch=" + site;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
        return true;
    }
    
}
