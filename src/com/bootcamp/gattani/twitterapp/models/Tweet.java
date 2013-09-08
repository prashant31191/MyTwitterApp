package com.bootcamp.gattani.twitterapp.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet extends BaseModel implements Comparable<Tweet> {
	private static final String CREATED_AT_FORMAT ="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	SimpleDateFormat sdf = new SimpleDateFormat(CREATED_AT_FORMAT, Locale.US);

	private User user;
	private long createdTs;

    public User getUser() {
        return user;
    }

    public long getCreatedTs(){
    	return createdTs;
    }
    
    public String getBody() {
        return getString("text");
    }

    public long getTweetId() {
        return getLong("id");
    }
    
    public Date getCreatedAt(){
    	Date createdDate = null;
    	String createdAt = getString("created_at");
    	sdf.setLenient(true);
    	try {
			createdDate = sdf.parse(createdAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return createdDate;
    }

    public boolean isFavorited() {
        return getBoolean("favorited");
    }

    public boolean isRetweeted() {
        return getBoolean("retweeted");
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

	@Override
	public int compareTo(Tweet another) {
		return (int) (another.createdTs - this.createdTs);
	}  
	
}
