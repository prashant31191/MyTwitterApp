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
	private MenuItem refreshProgressIndicator;
	
	private enum GET {
		ON_LOAD,
		ON_SCROLL,
		ON_REFRESH
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		lvTweets = (ListView)findViewById(R.id.lvTweets);			
		tweets = new ArrayList<Tweet>();
		tweetLvAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetLvAdapter); 

		//get timeline feed
		getHomeTimeLineByInvoction(GET.ON_LOAD);

		//setup the endless scroll
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void loadMore(int page, int totalItemsCount) {  	  
				//artificially clipped to not load more than 200 tweets when scrolling 
				if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
					getHomeTimeLineByInvoction(GET.ON_SCROLL);
				} else {
					Toast.makeText(getBaseContext(), "End of tweets ...", Toast.LENGTH_SHORT).show();
				}    			
			}
		});
		
		
		if(MyTwitterApp.getConnectionDetector().isConnected()){
			Toast.makeText(getBaseContext(), "Connected ...", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getBaseContext(), "No connection could be established ...", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds the compose and refresh
		getMenuInflater().inflate(R.menu.home_timeline, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refreshProgressIndicator = item;
			refreshProgressIndicator.setActionView(R.layout.action_refresh);
			refreshProgressIndicator.expandActionView();
			getHomeTimeLineByInvoction(GET.ON_REFRESH);
			break;

		case R.id.menu_compose:
			Intent i = new Intent(getBaseContext(), ComposeTweetActivity.class);
			startActivityForResult(i, ComposeTweetActivity.COMPOSE_TWEET_ACTIVITY_ID);
			break;
			
		case R.id.action_logout:
			//logout and send to logged out activity
			MyTwitterApp.getRestClient().clearAccessToken();
			Intent logout = new Intent(getBaseContext(), LoggedOutActivity.class);
			logout.putExtra("action", "logout");
			startActivity(logout); 
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
				getHomeTimeLineByInvoction(GET.ON_REFRESH);
			}			
		}
	}
	
	/**
	 * Constructs the parameters to call the Twitter API with and UI treatment 
	 * based on user action.
	 * 
	 * @param invoked
	 */
	private void getHomeTimeLineByInvoction(GET invoked){
		RequestParams rparams = null;
		boolean overWriteLocal = false;;
		boolean resetPosition = true;
		
		switch(invoked){
		case ON_LOAD:
			//not much to do here. this would get the default number of tweets and store them to db
			rparams = null;
			overWriteLocal = false;
			resetPosition = false;
			break;
			
		case ON_SCROLL:
			//construct the request params to fetch only older tweets
			if(tweets != null && !tweets.isEmpty()){
				rparams = new RequestParams();
				rparams.put("max_id", String.valueOf(tweets.get(tweets.size() - 1).getTweetId()));
			} else {
				rparams = null;
			}
			//set overwrite to true
			overWriteLocal = true;
			resetPosition = false;
			break;
			
		case ON_REFRESH:
			//request only the tweets that are newer than the latest we have
			if(tweets != null && !tweets.isEmpty()){
				rparams = new RequestParams();
				rparams.put("since_id", String.valueOf(tweets.get(0).getTweetId()));
			} else {
				rparams = null;
			}
			
			//set overwrite to true
			overWriteLocal = true;
			resetPosition = true;
			break;
		}		
		
		getHomeTimeline(rparams, overWriteLocal, resetPosition);

	}

	/**
	 * Gets and persists the tweets from home_timline based on request parameters. Applies
	 * UI treatment to scroll top and reset the refresh spinner
	 *  
	 * @param rparams
	 * @param overWriteLocal
	 * @param resetView
	 */
	private void getHomeTimeline(RequestParams rparams, final boolean overWriteLocal, final boolean resetView){		
		//get timeline feed
		MyTwitterApp.getRestClient().getHomeTimeline(rparams, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets){
				tweets.addAll(Tweet.fromJson(jsonTweets));
				Collections.sort(tweets);
				Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
				tweetLvAdapter.notifyDataSetChanged();
				if(resetView){
					//scroll to top
					lvTweets.smoothScrollToPosition(0);
				}
				
				if(overWriteLocal){
				//	Tweet.overWriteTweets(tweets);					
				} else {
				//	Tweet.storeTweets(tweets);
				}
				
				if(refreshProgressIndicator != null){
					refreshProgressIndicator.collapseActionView();
					refreshProgressIndicator.setActionView(null);
					refreshProgressIndicator = null;
				}

			}
		});				
	}
	
}
