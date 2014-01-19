package com.yahoo.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AdvancedOptionsActivity extends Activity {
	private Spinner spSize, spColor, spType;
	private EditText edSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_options);
		//Toast.makeText(getApplicationContext(), "setting advanced options", Toast.LENGTH_SHORT).show();
		spSize = (Spinner)findViewById(R.id.spImageSize);
		spColor = (Spinner)findViewById(R.id.spColorFilter);
		spType = (Spinner)findViewById(R.id.spImageType);
		edSite = (EditText)findViewById(R.id.etSiteFilter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanced_options, menu);
		return true;
	}
	
	public void setImageSearchOptions(View button) {

		String imageSize = spSize.getSelectedItem().toString();
		String imageColor = spColor.getSelectedItem().toString();
		String imageType = spType.getSelectedItem().toString();
		String imageSite = edSite.getText().toString();
		
		Intent data = new Intent();
		// Pass relevant data back as a result
		data.putExtra("image_size", imageSize);
		data.putExtra("image_color", imageColor);
		data.putExtra("image_type", imageType);		
		data.putExtra("image_site", imageSite);
		data.putExtra("status", "OK" );
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish();
	}

}
