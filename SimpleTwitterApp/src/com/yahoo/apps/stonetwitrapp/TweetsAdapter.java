package com.yahoo.apps.stonetwitrapp;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.stonetwitrapp.fragments.TweetsListFragment.OnItemSelectedListener;
import com.yahoo.apps.stonetwitrapp.models.Tweet;


public class TweetsAdapter extends ArrayAdapter<Tweet> {
	private OnItemSelectedListener onclickListener;

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(getContext());
        	convertView = inflater.inflate(R.layout.tweet_item, null);
        }
		// Get the data item for this position
        final Tweet tweet = getItem(position);
        
        // Lookup views within item layout
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        
        // Populate the data into the template view using the data object
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), ivProfile);
        tvName.setText(tweet.getUser().getName() + " @" + tweet.getUser().getScreenName() );
        tvBody.setText(tweet.getBody());
        
        ivProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(getContext(), "u click a profile image", Toast.LENGTH_SHORT).show();			
				String userId = tweet.getUser().getUserIdStr();
				Log.d("DEBUG", "click profile image for userId: " + userId);
				if (onclickListener != null) {
					Log.d("DEBUG", "calling listener method with userId: " + userId);
					onclickListener.onProfileImageSelected(userId);
				}			
			}
		});
        
        // Return the completed view to render on screen
        return convertView;
	}
	
	public void addClickListener(OnItemSelectedListener listener) {
		this.onclickListener = listener;
	}
}
