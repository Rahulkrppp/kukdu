package com.kukdudelivery.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kukdudelivery.util.AppPreferences;


public class FcmInstanceIDService extends FirebaseMessagingService {

    AppPreferences sharedPref;

    @Override
    public void onNewToken(@NonNull String token) {
        sendRegistrationToServer(token);
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        sharedPref = new AppPreferences(this);
        super.onMessageReceived(message);
    }

    //    @Override
//    public void onTokenRefresh() {
//        sharedPref = new AppPreferences(this);
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        sendRegistrationToServer(refreshedToken);
//    }

    private void sendRegistrationToServer(String token) {
        sharedPref.set("DEVICE_KEY",token);
        Log.i("TOKEN =:",token+"");
        //sharedPref.setOpenAppCounter(SharedPref.MAX_OPEN_COUNTER);
    }
}
