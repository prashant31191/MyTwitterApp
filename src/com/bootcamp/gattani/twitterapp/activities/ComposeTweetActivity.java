package com.bootcamp.gattani.twitterapp.activities;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bootcamp.gattani.twitterapp.MyTwitterApp;
import com.bootcamp.gattani.twitterapp.R;
import com.bootcamp.gattani.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetActivity extends Activity {
	public static final int COMPOSE_TWEET_ACTIVITY_ID = 101;
	public static final int MAX_TWEET_SIZE = 140;
	private User currentUser;
	ImageView ivMyProfilePicture;
	TextView tvMyScreenName;
	EditText etTweetBody;
	int tweetCharactersLeft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		//save off layout handles
		ivMyProfilePicture = (ImageView)findViewById(R.id.ivMyProfilePicture);
		etTweetBody = (EditText)findViewById(R.id.etTweetBody);
		tvMyScreenName = (TextView)findViewById(R.id.tvMyScreenName);
		registerTweetSizeCounter();
		
		//get current user
		MyTwitterApp.getRestClient().getCurrentUser(null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject userInfo){
				currentUser = User.fromJson(userInfo);
				if(currentUser != null){
					//set action bar
			        getActionBar().setTitle(currentUser.getScreenName());

			        //load up the pictures
			        ImageLoader.getInstance().displayImage(currentUser.getProfileImageUrl(), ivMyProfilePicture);			        
			        
			        //show screen name
			        String formattedName = "<b>" + currentUser.getName() + "</b>" + " <small><font color='#777777'>@" +
			        		currentUser.getScreenName() + "</font></small>";
			        tvMyScreenName.setText(Html.fromHtml(formattedName));
				}
			}
		});
	}
	
	private void registerTweetSizeCounter(){
		etTweetBody.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable textView) {				
				int len = etTweetBody.length();
//				if (numCharacters <= 140) {
//					//tvCharacterCount.setText(String.valueOf(140 - numCharacters) + " characters remaining");
//				} else {
//					//tvCharacterCount.setText(String.valueOf(140 - numCharacters) + " too many characters");
//				}
				tweetCharactersLeft = MAX_TWEET_SIZE - len;
				Log.d("DEBUG", "Left Characters: " + tweetCharactersLeft);
				return;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				return;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				return;
			}
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_tweet:
			String tweetBody = etTweetBody.getText().toString();
			if(StringUtils.isBlank(tweetBody)){
				Toast.makeText(getApplicationContext(), "Tweet Discarded", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				if (getParent() == null) {
					setResult(Activity.RESULT_OK, data);
				} else {
					getParent().setResult(Activity.RESULT_OK, data);
				}
				finish();
			}
			
			if(tweetBody.length() > 140){
				
			}
			
			RequestParams params = new RequestParams();
			params.put("status", tweetBody);	

			MyTwitterApp.getRestClient().postStatusUpdate(params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject jsonTweets) {
					Toast.makeText(getApplicationContext(), "Tweet Sent", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFinish() {
					Intent data = new Intent();
					if (getParent() == null) {
						setResult(Activity.RESULT_OK, data);
					} else {
						getParent().setResult(Activity.RESULT_OK, data);
					}
					finish();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					Toast.makeText(getApplicationContext(), "Service Unavailable!!!", Toast.LENGTH_SHORT).show();
				}
			});
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
    public void onBackPressed() {
		Toast.makeText(getApplicationContext(), "Tweet Discarded", Toast.LENGTH_SHORT).show();
		Intent data = new Intent();
		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
		super.onBackPressed();   
    }

}
