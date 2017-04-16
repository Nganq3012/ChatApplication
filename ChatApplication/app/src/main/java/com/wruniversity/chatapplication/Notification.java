package com.wruniversity.chatapplication;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by laptop88 on 3/7/2017.
 */

public class Notification {
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
