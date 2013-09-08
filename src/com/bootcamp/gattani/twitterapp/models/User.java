package com.bootcamp.gattani.twitterapp.models;

import org.json.JSONObject;

public class User extends BaseModel{

	public String getName() {
        return getString("name");
    }

    public long getUserId() {
        return getLong("id");
    }

    public String getScreenName() {
        return getString("screen_name");
    }

    public String getProfileImageUrl() {
        return getString("profile_image_url");
    }

    public String getProfileBackgroundImageUrl() {
        return getString("profile_background_image_url");
    }

    public int getNumTweets() {
        return getInt("statuses_count");
    }

    public int getFollowersCount() {
        return getInt("followers_count");
    }

    public int getFriendsCount() {
        return getInt("friends_count");
    }

    public static User fromJson(JSONObject json) {
        User u = new User();

        try {
            u.jsonObject = json;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [");
		if (getName() != null) {
			builder.append("name=");
			builder.append(getName());
			builder.append(", ");
		}
		builder.append("id=");
		builder.append(getUserId());
		builder.append(", ");
		if (getScreenName() != null) {
			builder.append("screenName=");
			builder.append(getScreenName());
			builder.append(", ");
		}
		builder.append("numTweets=");
		builder.append(getNumTweets());
		builder.append(", followers=");
		builder.append(getFollowersCount());
		builder.append(", friends=");
		builder.append(getFriendsCount());
		builder.append("]");
		return builder.toString();
	}
    
    
}
