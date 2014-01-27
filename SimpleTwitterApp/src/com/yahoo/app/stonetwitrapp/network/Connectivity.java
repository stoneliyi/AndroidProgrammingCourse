package com.yahoo.app.stonetwitrapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Connectivity {
	private static Connectivity instance = new Connectivity();
	static Context context;
	ConnectivityManager connectivityManager;
	NetworkInfo wifiInfo, mobileInfo;
	boolean connected = false;

	public static Connectivity getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	public boolean isOnline() {
		try {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable() &&
					networkInfo.isConnected();

		} catch (Exception e) {
			System.out.println("CheckConnectivity Exception: " + e.getMessage());
			Log.d("connectivity", e.toString());
		}
		return connected;
	}
}
