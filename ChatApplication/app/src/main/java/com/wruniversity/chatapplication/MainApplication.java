package com.wruniversity.chatapplication;

import android.app.Application;
import android.os.StrictMode;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;

/**
 * Created by laptop88 on 3/13/2017.
 */
public class MainApplication extends Application implements IAdobeAuthClientCredentials  {
    private static final String CREATIVE_SDK_CLIENT_ID = "ac76a1cf32784c33a028f1cc4c5c8b0c";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "bd7b4a4a-7cb9-4af3-aa0d-4253fb81a6ba";
    private static final String CREATIVE_SDK_REDIRECT_URI = "ams+e753e3a6b6240c47d3b4ef762fa6caee3e84d801://adobeid/ac76a1cf32784c33a028f1cc4c5c8b0c\n";
    private static final String[] CREATIVE_SDK_SCOPES = {"email", "profile", "address"};

    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }


}
