package com.bootcamp.gattani.twitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "RWB57DW6lI0otOX43Esjg";
    public static final String REST_CONSUMER_SECRET = "NkZvnE6uYdrRZQm5JQ2MMBJ7zamUC0dQWCTyE1rH8Q";
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp";
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(RequestParams params, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        client.get(apiUrl, params, handler);
    }

    public void getCurrentUser(RequestParams params, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, params, handler);
    }

    public void postStatusUpdate(RequestParams params, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        client.post(apiUrl, params, handler);
    }
    
    

}