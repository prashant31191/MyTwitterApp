package com.bootcamp.gattani.twitterapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name="Tweets")
public class Tweet extends Model implements Comparable<Tweet> {	
	private static final String CREATED_AT_FORMAT ="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	SimpleDateFormat sdf = new SimpleDateFormat(CREATED_AT_FORMAT, Locale.US);

	@Column(name = "JSONObject")
    protected JSONObject jsonObject;
	
	@Column(name = "User")
	protected User user;
	
	@Column(name = "CreateTs")
	protected long createdTs;

    public User getUser() {
        return user;
    }

    public long getCreatedTs(){
    	return createdTs;
    }
    
    public String getBody() {
        return JsonUtils.getString(this.jsonObject, "text");
    }

    public long getTweetId() {
        return JsonUtils.getLong(this.jsonObject, "id");
    }
    
    public Date getCreatedAt(){
    	Date createdDate = null;
    	String createdAt = JsonUtils.getString(this.jsonObject, "created_at");
    	sdf.setLenient(true);
    	try {
			createdDate = sdf.parse(createdAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return createdDate;
    }

    public boolean isFavorited() {
        return JsonUtils.getBoolean(this.jsonObject, "favorited");
    }

    public boolean isRetweeted() {
        return JsonUtils.getBoolean(this.jsonObject, "retweeted");
    }

    /**
     * Needed to sort the tweets in descending order
     */
	@Override
	public int compareTo(Tweet another) {
		return (int) (another.createdTs - this.createdTs);
	} 

    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tweet [");
		builder.append("id=");
		builder.append(getTweetId());
		builder.append(", ");
		if (user != null) {
			builder.append("user=");
			builder.append(user.toString());
			builder.append(", ");
		}
		if (getCreatedAt() != null) {
			builder.append("crated_at=");
			builder.append(getCreatedAt().toString());
			builder.append(", ");
		}
		if (getBody() != null) {
			builder.append("body=");
			builder.append(getBody());
			builder.append(", ");
		}
		builder.append("isFavorited()=");
		builder.append(isFavorited());
		builder.append(", isRetweeted()=");
		builder.append(isRetweeted());
		builder.append("]");
		return builder.toString();
	}

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.jsonObject = jsonObject;
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.createdTs = tweet.getCreatedAt().getTime();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
    
    /**
     * Get all stored tweets sorted by newest first    
     * @return
     */
	public static List<Tweet> getStoredTweets() {    
        return new Select().from(Tweet.class).orderBy("CreatedTs DEC").limit("300").execute();
    }
    
    /**
     * This function deletes all stored tweets and then writes out 25 from the list passed
     * @param tweets
     */
    public static void storeTweets(List<Tweet> tweets){
    	//store 25 tweets
		for(int i = 0; i < tweets.size() && i < 25; i++){
			Log.d("DEBUG", "i=" + i);
			Tweet t = tweets.get(i);
			Tweet toSave = new Tweet();
			toSave.jsonObject = t.jsonObject;
			toSave.user = t.user;
			toSave.createdTs = t.createdTs;
			toSave.save();
			
		}
    }

    /**
     * This function deletes all stored tweets and then writes out 25 from the list passed
     * @param tweets
     */
    public static void overWriteTweets(List<Tweet> tweets){
    	//delete all first
    	ArrayList<Tweet> allStoredTweets = new Select().all().from(Tweet.class).execute();
    	if(allStoredTweets != null) {
        	for(Tweet t : allStoredTweets){
        		Tweet.delete(Tweet.class, t.getId());
        	}    		
    	}
    	
    	//store 25 tweets
		for(int i = 0; i < tweets.size() && i < 25; i++){
			Tweet toSave = new Tweet();
			toSave.jsonObject = tweets.get(i).jsonObject;
			toSave.user = tweets.get(i).user;
			toSave.createdTs = tweets.get(i).createdTs;
			toSave.save();
		}
    }	
}
