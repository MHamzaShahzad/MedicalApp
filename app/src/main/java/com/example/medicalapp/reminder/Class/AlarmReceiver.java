package com.example.medicalapp.reminder.Class;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.WindowManager;

import com.example.medicalapp.Constants;
import com.example.medicalapp.controllers.CustomNotificationGenerator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlarmReceiver extends BroadcastReceiver {
    Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private static Ringtone ringtoneAlarm;
    private static AlertDialog.Builder builder;
    private static AlertDialog alert;
    private FirebaseUser firebaseUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (firebaseUser == null) {
            FirebaseApp.initializeApp(context);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }

        // Intent x = new Intent(context, Alert.class);
        //  x.putExtra(context.getString(R.string.titttle), Title);
        // x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  context.startActivity(x);
        // intent = new Intent(context, MyService.class);
        //  context.startService(intent);

        if (ringtoneAlarm == null)
            ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone);

        if (intent.getAction() != null)
            switch (intent.getAction()) {
                case Constants.STRING_EXTRA_STOP_RINGTONE:
                    if (ringtoneAlarm.isPlaying())
                        ringtoneAlarm.stop();
                    break;
                default:
                    remind(context);
            }
        else
            remind(context);

    }

    private void remind(Context context) {
        if (firebaseUser != null) {

            if (!ringtoneAlarm.isPlaying())
                ringtoneAlarm.play();

            try {
                if (builder == null) {
                    builder = new AlertDialog.Builder(context);
                    String msg = "Time to take a medicine";
                    builder.setMessage(msg).setCancelable(
                            false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    ringtoneAlarm.stop();
                                }
                            });
                }
                if (alert == null)
                    alert = builder.create();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                else
                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

                if (!alert.isShowing())
                    alert.show();

            } catch (Exception e) {
                e.printStackTrace();

                new CustomNotificationGenerator(context)
                        .createNotification(
                                CustomNotificationGenerator.getNextNotifId(context),
                                "Medicine Reminder",
                                "Time to take medicine.",
                                null
                        );

            }

        }
    }
}
