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
        return true;
    }

    public static boolean subscribe() {
        FirebaseApp.initializeApp(context);
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
        return true;
    }

    public static boolean getSubscriptionStatus() {
        return true;
    }

    public static String getSubscriptionToken() {
        return "";
    }


    private FCMHelper(Context context) {
        // Initialize your Library here
        this.context = context;
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
