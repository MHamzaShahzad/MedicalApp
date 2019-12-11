package com.example.medicalapp.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.medicalapp.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendPushNotificationFirebase {

    private static final String TAG = SendPushNotificationFirebase.class.getName();

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String serverKey = "key=" + "AAAAVjeX-10:APA91bEgSg7x6UiYfM6dXVubchDfCR1nMh_dMMkfWJhoIo59tIym2bbZwPRkPJtJBl7cLp5R5lL61IXyX3bwoPQApSR5K0x_FJFKl_TZ9EnagcsVsDJGCjzi_9-ug4sxAB9xjxCKAIMf";
    private static final String contentType = "application/json";

    public static void buildAndSendNotification(Context context, String TOPIC_SEND_TO, String notificationTitle, String notificationMsg){
        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", notificationTitle);
            notificationBody.put("message", notificationMsg);

            notification.put("to", "/topics/" + TOPIC_SEND_TO);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(context, notification);
    }

    private static void sendNotification(final Context context, JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
