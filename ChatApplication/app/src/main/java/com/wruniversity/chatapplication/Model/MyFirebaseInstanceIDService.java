package com.wruniversity.chatapplication.Model;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * Created by laptop88 on 3/17/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static String TAG = "Registration";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        System.out.println("Registration.onTokenRefresh TOKEN: " + refreshedToken );

    }
}
