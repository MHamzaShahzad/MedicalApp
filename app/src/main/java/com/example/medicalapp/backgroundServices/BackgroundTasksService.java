package com.example.medicalapp.backgroundServices;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.medicalapp.CommonFunctionsClass;
import com.example.medicalapp.controllers.CustomNotificationGenerator;
import com.example.medicalapp.inventory.MedInventoryDatabaseHelper;
import com.example.medicalapp.inventory.MedInventoryDatabaseManagement;
import com.example.medicalapp.inventory.MedInventoryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundTasksService extends Service {

    private static final String TAG = BackgroundTasksJobService.class.getName();
    private MedInventoryDatabaseManagement medInventoryDatabaseManagement;
    private CustomNotificationGenerator notificationGenerator;

    public BackgroundTasksService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (notificationGenerator == null)
            notificationGenerator = CustomNotificationGenerator.getInstance(getApplicationContext());

        if (medInventoryDatabaseManagement == null)
            medInventoryDatabaseManagement = new MedInventoryDatabaseManagement(getApplicationContext());

        final List<MedInventoryModel> medInventoryModelList = new ArrayList<>(medInventoryDatabaseManagement.retrieveMedicineFromInventory());

        if (medInventoryModelList.size() > 0) {

            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.e(TAG, "run: ");

                            for (int i = 0; i < medInventoryModelList.size(); i++) {
                                try {
                                    MedInventoryModel medInventoryModel = medInventoryModelList.get(i);
                                    if (CommonFunctionsClass.isOutdated(medInventoryModel.getMedExpiryDate()) && medInventoryModel.getIsNotifiedAboutExpiry() == 0) {
                                        notificationGenerator.createNotification(
                                                CustomNotificationGenerator.getNextNotifId(getApplicationContext()),
                                                "Medicine has been expired!",
                                                medInventoryModel.getMedName() + "\n" + medInventoryModel.getMedExpiryDate(),
                                                null

                                        );
                                        boolean isUpdated = medInventoryDatabaseManagement.addMedicineDetails(MedInventoryDatabaseHelper.TABLE_MEDICINE_DETAILS,
                                                new MedInventoryModel(
                                                        medInventoryModel.getMedName(),
                                                        medInventoryModel.getMedDetail(),
                                                        medInventoryModel.getMedAddedAt(),
                                                        medInventoryModel.getMedExpiryDate(),
                                                        medInventoryModel.getMedType(),
                                                        medInventoryModel.getMedStock(),
                                                        medInventoryModel.getMedPerDay(),
                                                        1
                                                ));
                                        if (isUpdated) {
                                            Log.e(TAG, "run: " + isUpdated );
                                            medInventoryModelList.clear();
                                            medInventoryModelList.addAll(medInventoryDatabaseManagement.retrieveMedicineFromInventory());
                                        }
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 10000, 10000);

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
