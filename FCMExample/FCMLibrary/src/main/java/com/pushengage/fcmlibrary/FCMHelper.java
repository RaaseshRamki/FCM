package com.pushengage.fcmlibrary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMHelper {

    private static Context context;
    private static String TAG = "FCMHelper";
    static FcmInterface fcmInterface;
    public static final String PERMISSION_STATUS = "PERMISSION_STATUS", SUBSCRIBE = "SUBSCRIBE", SUBSCRIPTION_STATUS = "SUBSCRIPTION_STATUS", TOKEN = "TOKEN";

    public static void getPermissionStatus() {
        //Android doesn't require special permissions for push notifications. By default it is enabled.
        fcmInterface.callback(PERMISSION_STATUS, true, "");
    }

    public static void subscribe(String topic) {
        //Enables or disables auto-initialization of Firebase Cloud Messaging.
        //When enabled, Firebase Cloud Messaging generates a registration token on app startup if there is no valid one and periodically sends data to the Firebase backend to validate the token.
        //This setting is persisted across app restarts and overrides the setting specified in your manifest.
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        //Subscribes to topic in the background.
        //The subscribe operation is persisted and will be retried until successful.
        FirebaseMessaging.getInstance().subscribeToTopic("PE")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        fcmInterface.callback(SUBSCRIBE, task.isSuccessful(), "");
                    }
                });
    }

    public static void getSubscriptionStatus() {
        //Firebase Cloud Messaging doesn't explicity provide a method or callback to check status of subscription.
        //Hence we consider that our device is subscribed if we get a token or else if it failed to return a token we consider it as not subscribed.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        fcmInterface.callback(SUBSCRIBE, task.isSuccessful(), "");
                    }
                });

    }

    public static void getSubscriptionToken() {
        //Returns the FCM registration token for this Firebase project.
        //This creates a Firebase Installations ID, if one does not exist, and sends information about the application and the device where it's running to the Firebase backend.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            fcmInterface.callback(SUBSCRIBE, task.isSuccessful(), "");
                            return;
                        } else {
                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.d(TAG, token);
                            fcmInterface.callback(SUBSCRIBE, task.isSuccessful(), token);
                        }
                    }
                });
    }

    private FCMHelper(Context context, FcmInterface fcmInterface) {
        // Library Initialized here
        this.context = context;
        this.fcmInterface = fcmInterface;
        FirebaseApp.initializeApp(context);
    }

    public static class Builder {
        private Context context;
        private FcmInterface fcmInterface;

        public Builder addContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder addFCMInterface(FcmInterface fcmInterface) {
            this.fcmInterface = fcmInterface;
            return this;
        }

        public FCMHelper build() {
            return new FCMHelper(context, fcmInterface);
        }

    }

    public interface FcmInterface {
        void callback(String type, Boolean status, String token);
    }
}
