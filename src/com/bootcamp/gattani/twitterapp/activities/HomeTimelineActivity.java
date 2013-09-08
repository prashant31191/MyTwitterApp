package com.bootcamp.gattani.twitterapp.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.adapters.TweetsAdapter;
import com.bootcamp.gattani.twitterapp.listeners.EndlessScrollListener;
import com.bootcamp.gattani.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomeTimelineActivity extends Activity {
	private ListView lvTweets;
	private ArrayList<Tweet> tweets;
	private TweetsAdapter tweetLvAdapter;
	private MenuItem refreshItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		lvTweets = (ListView)findViewById(R.id.lvTweets);			
		tweets = new ArrayList<Tweet>();
		tweetLvAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetLvAdapter); 

		//get timeline feed
		MyTwitterApp.getRestClient().getHomeTimeline(null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets){
				tweets.addAll(Tweet.fromJson(jsonTweets));
				Collections.sort(tweets);
				tweetLvAdapter.notifyDataSetChanged();
				Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
			}
		});
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
    	    @Override
    	    public void loadMore(int page, int totalItemsCount) {  	    
    			if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
    				RequestParams rparams = new RequestParams();
    				rparams.put("max_id", String.valueOf(tweets.get(tweets.size() - 1).getTweetId()));
        			//get timeline feed
        			MyTwitterApp.getRestClient().getHomeTimeline(rparams, new JsonHttpResponseHandler() {
        				@Override
        				public void onSuccess(JSONArray jsonTweets){
        					tweets.addAll(Tweet.fromJson(jsonTweets));
        					Collections.sort(tweets);
        					tweetLvAdapter.notifyDataSetChanged();
        					Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
        				}
        			});

    			} else {
    				Toast.makeText(getBaseContext(), "End of tweets ...", Toast.LENGTH_SHORT).show();
    			}    			
    	    }
    	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refreshItem = item;
			refreshItem.setActionView(R.layout.action_refresh);
			refreshItem.expandActionView();
			
			RequestParams rparams = null;
			if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
				rparams = new RequestParams();
				//rparams.put("max_id", String.valueOf(tweets.get(tweets.size() - 1).getTweetId()));
				rparams.put("since_id", String.valueOf(tweets.get(0).getTweetId()));
			} else {
				//max limit of tweets for timeline that rest api supports
				tweets.clear();
			}
			
			//scroll to top
			lvTweets.smoothScrollToPosition(0);
			
			//get timeline feed
			MyTwitterApp.getRestClient().getHomeTimeline(rparams, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets){
					tweets.addAll(Tweet.fromJson(jsonTweets));
					Collections.sort(tweets);
					tweetLvAdapter.notifyDataSetChanged();
					refreshItem.collapseActionView();
				    refreshItem.setActionView(null);
					Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
				}
			});
			break;
			
		case R.id.menu_compose:
			Intent i = new Intent(getBaseContext(), ComposeTweetActivity.class);
			startActivityForResult(i, ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY_ID);
			break;
			
		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY_ID) {
			if (resultCode == Activity.RESULT_OK) {
				RequestParams rparams = null;
				if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
					rparams = new RequestParams();
					rparams.put("since_id", String.valueOf(tweets.get(0).getTweetId()));
				} else {
					//max limit of tweets for timeline that rest api supports
					tweets.clear();
				}
				
				//scroll to top
				lvTweets.smoothScrollToPosition(0);
				
				//get timeline feed
				MyTwitterApp.getRestClient().getHomeTimeline(rparams, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets){
						tweets.addAll(Tweet.fromJson(jsonTweets));
						Collections.sort(tweets);
						tweetLvAdapter.notifyDataSetChanged();
						Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
					}
				});				
			}			
		}
	}


}
