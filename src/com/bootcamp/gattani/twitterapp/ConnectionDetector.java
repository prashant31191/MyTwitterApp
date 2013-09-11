package com.bootcamp.gattani.twitterapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
 
public class ConnectionDetector {
     
    private Context context;
    private static ConnectionDetector instance = null;
    
    public enum ConnectionType {
    	TYPE_NOT_CONNECTED,
    	TYPE_WIFI,
    	TYPE_MOBILE
    };
    
    private ConnectionDetector(Context context){
        this.context = context;
    }
 
    public static ConnectionDetector getInstance(Context context){
    	if(instance == null && context != null){
    		instance = new ConnectionDetector(context);
    	}
    	
    	return instance;
    }
    
    public boolean isConnected(){
    	ConnectionType conn = getConnection();
        if (conn == ConnectionType.TYPE_WIFI) {
            //Toast.makeText(context, "Connected to Wifi", Toast.LENGTH_SHORT).show();
            Log.d("DEBUG", "Connected to Wifi");
        } else if (conn == ConnectionType.TYPE_MOBILE) {
        	Log.d("DEBUG", "Connected to Mobile N/W");
        	//Toast.makeText(context, "Connected to Mobile N/W", Toast.LENGTH_SHORT).show();
        } else if (conn == ConnectionType.TYPE_NOT_CONNECTED) {
        	Log.d("DEBUG", "Not Connected");
        	//Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
        }
        
        return (conn != ConnectionType.TYPE_NOT_CONNECTED);
    }
    
    public ConnectionType getConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return ConnectionType.TYPE_WIFI;
             
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return ConnectionType.TYPE_MOBILE;
        } 
        return ConnectionType.TYPE_NOT_CONNECTED;
    }
}