package com.bootcamp.gattani.twitterapp.models;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {
	
	@Column(name = "JSONObject")
    protected JSONObject jsonObject;
	
	public String getName() {
        return JsonUtils.getString(this.jsonObject, "name");
    }

    public long getUserId() {
        return JsonUtils.getLong(this.jsonObject, "id");
    }

    public String getScreenName() {
        return JsonUtils.getString(this.jsonObject, "screen_name");
    }

    public String getProfileImageUrl() {
        return JsonUtils.getString(this.jsonObject, "profile_image_url");
    }

    public String getProfileBackgroundImageUrl() {
        return JsonUtils.getString(this.jsonObject, "profile_background_image_url");
    }

    public int getNumTweets() {
        return JsonUtils.getInt(this.jsonObject, "statuses_count");
    }

    public int getFollowersCount() {
        return JsonUtils.getInt(this.jsonObject, "followers_count");
    }

    public int getFriendsCount() {
        return JsonUtils.getInt(this.jsonObject, "friends_count");
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
