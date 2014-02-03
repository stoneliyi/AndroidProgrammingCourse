package com.yahoo.gridimagesearch;

import java.io.InputStream;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class FullImageViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_view);
		Intent receivedIntent = getIntent();
		ImageResult image = (ImageResult) receivedIntent.getSerializableExtra("imageObj");
//		SmartImageView sivImage = (SmartImageView) findViewById(R.id.sivFull);
//		sivImage.setImageUrl(image.getFullUrl());
//		new DownloadImageTask(sivImage).execute(image.getFullUrl());
		
		ImageView sivImage = (ImageView) findViewById(R.id.sivFull);
		Picasso.with(getApplicationContext()).load(image.getFullUrl()).into(sivImage);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image_view, menu);
		return true;
	}

	@SuppressWarnings("unused")
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
}
