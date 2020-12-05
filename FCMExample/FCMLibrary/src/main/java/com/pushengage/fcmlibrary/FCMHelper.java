package com.pushengage.fcmlibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMHelper {

    private static Context context;
    private static String TAG = "FCMHelper";

    public static boolean getPermissionStatus() {
        //Android doesn't require special permissions for push notifications. By default it is enabled.
        return true;
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
                        Boolean msg = true;
                        if (!task.isSuccessful()) {
                            msg = false;
                        }
                        Log.d(TAG, String.valueOf(msg));
                        Toast.makeText(context, String.valueOf(msg), Toast.LENGTH_SHORT).show();
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
                        Boolean subscriptionStatus = task.isSuccessful();
                        if (subscriptionStatus) {
                            Toast.makeText(context, "Subscribed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Not subscribed", Toast.LENGTH_SHORT).show();
                        }
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
                            Toast.makeText(context, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // Get new FCM registration token
                            String token = "FCM registration Token = " + task.getResult();
                            Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, token);
                        }
                    }
                });
    }

    private FCMHelper(Context context) {
        // Library Initialized here
        this.context = context;
        FirebaseApp.initializeApp(context);
    }

    public static class Builder {
        private Context context;

        public Builder addContext(Context context) {
            this.context = context;
            return this;
        }

        public FCMHelper build() {
            return new FCMHelper(context);
        }

    }
}
