package com.bootcamp.gattani.twitterapp.adapters;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ocpsoft.pretty.time.PrettyTime;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
	private PrettyTime pT = new PrettyTime(Locale.US);

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (view == null) {
	    	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	view = inflater.inflate(R.layout.tweet_item, null);
	    }
	     
        Tweet tweet = getItem(position);
        
        ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
        ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
        
        TextView nameView = (TextView) view.findViewById(R.id.tvName);
        String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#777777'>@" +
                tweet.getUser().getScreenName() + "</font></small>";
        nameView.setText(Html.fromHtml(formattedName));

        TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
        bodyView.setText(Html.fromHtml(tweet.getBody()));
        
        TextView timeView = (TextView) view.findViewById(R.id.tvCreatedAt);
        String time = String.valueOf(tweet.getCreatedAt());
        try{
        	time = pT.format(tweet.getCreatedAt());
        }catch(Exception e){
        	Log.d("DEBUG", "Created At: " + tweet.getCreatedAt());
        }
        timeView.setText(time);
        return view;
	}
}
