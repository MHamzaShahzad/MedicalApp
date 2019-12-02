package com.example.medicalapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.medicalapp.Constants;
import com.example.medicalapp.controllers.MyFirebaseDatabase;
import com.example.medicalapp.doc_pat_interaction.FragmentMyDoctors;
import com.example.medicalapp.doc_pat_interaction.FragmentPatientsList;
import com.example.medicalapp.doc_pat_interaction.FragmentRegisterPatient;
import com.example.medicalapp.emergency.FirstAidFragment;
import com.example.medicalapp.communicate.FragmentContactUs;
import com.example.medicalapp.inventory.MedInventoryMainActivity;
import com.example.medicalapp.models.User;
import com.example.medicalapp.nearby.FragmentNearbyPlaces;
import com.example.medicalapp.R;
import com.example.medicalapp.interfaces.FragmentInteractionListenerInterface;
import com.example.medicalapp.reminder.activity.SplashActivityPillsReminder;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;
import java.util.Locale;

public class HomeDrawerActivity extends AppCompatActivity
        implements View.OnClickListener, FragmentInteractionListenerInterface {

    private Context context;
    private CardView cardNearByHospitals, cardNearByPharmacies, cardEmergencyTraining,
            cardMedicineReminder, cardMedicineInventory, cardShareLocation;

    public static final int LOCATION_PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_home_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(Constants.TITLE_HOME);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        findViewById();
    }

    private void findViewById() {
        cardNearByHospitals = findViewById(R.id.cardNearByHospitals);
        cardNearByPharmacies = findViewById(R.id.cardNearByPharmacies);
        cardEmergencyTraining = findViewById(R.id.cardEmergencyTraining);
        cardMedicineReminder = findViewById(R.id.cardMedicineReminder);
        cardMedicineInventory = findViewById(R.id.cardMedicineInventory);
        cardShareLocation = findViewById(R.id.cardShareLocation);

        initClickListeners();
    }

    private void initClickListeners() {
        cardNearByHospitals.setOnClickListener(this);
        cardNearByPharmacies.setOnClickListener(this);
        cardEmergencyTraining.setOnClickListener(this);
        cardMedicineReminder.setOnClickListener(this);
        cardMedicineInventory.setOnClickListener(this);
        cardShareLocation.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(Constants.TITLE_HOME);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {

                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getAccountType().equals(Constants.ACCOUNT_TYPE_DOCTOR))
                                getMenuInflater().inflate(R.menu.home_menu_doctor, menu);
                            else if (user.getAccountType().equals(Constants.ACCOUNT_TYPE_PATIENT))
                                getMenuInflater().inflate(R.menu.home_menu_patient, menu);
                            else
                                getMenuInflater().inflate(R.menu.home_drawer, menu);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            return true;
        } else if (id == R.id.action_about_us) {
            return true;
        } else if (id == R.id.action_register_patient) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentRegisterPatient.newInstance(), Constants.TITLE_REGISTER_PATIENT).addToBackStack(Constants.TITLE_REGISTER_PATIENT).commit();
            return true;
        } else if (id == R.id.action_patients_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentPatientsList.newInstance(), Constants.TITLE_MY_PATIENTS).addToBackStack(Constants.TITLE_MY_PATIENTS).commit();
            return true;
        } else if (id == R.id.action_my_doctors) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentMyDoctors.newInstance(), Constants.TITLE_MY_DOCTORS).addToBackStack(Constants.TITLE_MY_DOCTORS).commit();
            return true;
        } else if (id == R.id.action_contact_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home, FragmentContactUs.newInstance(), Constants.TITLE_CONTACT_US).addToBackStack(Constants.TITLE_CONTACT_US).commit();
            return true;
        } else if (id == R.id.action_logout) {
            SignOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SignOut() {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    moveToMain();
                }
            }
        });
    }

    private void moveToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {

        if (view == cardNearByHospitals) {

            FragmentNearbyPlaces fragmentNearbyPlaces = new FragmentNearbyPlaces();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.STRING_ARGUE_NEARBY_ENTITY, "hospital");
            fragmentNearbyPlaces.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home, fragmentNearbyPlaces, Constants.TITLE_NEARBY_HOSPITALS).addToBackStack(Constants.TITLE_NEARBY_HOSPITALS).commit();

        } else if (view == cardNearByPharmacies) {

            FragmentNearbyPlaces fragmentNearbyPlaces = new FragmentNearbyPlaces();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.STRING_ARGUE_NEARBY_ENTITY, "pharmacy");
            fragmentNearbyPlaces.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home, fragmentNearbyPlaces, Constants.TITLE_NEARBY_PHARMACIES).addToBackStack(Constants.TITLE_NEARBY_PHARMACIES).commit();

        } else if (view == cardEmergencyTraining) {

            FirstAidFragment firstAidFragment = new FirstAidFragment();
            Bundle bundle = new Bundle();
            firstAidFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.home, firstAidFragment, Constants.TITLE_FIRST_AID).addToBackStack(Constants.TITLE_FIRST_AID).commit();

        } else if (view == cardMedicineReminder) {

            startActivity(new Intent(context, SplashActivityPillsReminder.class));

        } else if (view == cardMedicineInventory) {

            startActivity(new Intent(context, MedInventoryMainActivity.class));

        } else if (view == cardShareLocation) {

            getLastLocation();

        }

    }

    @Override
    public void onFragmentInteraction(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled())
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    String address = getCompleteAddressString(context, location.getLatitude(), location.getLongitude());
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_TEXT, "My Location  : " + "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude() + "  Address is " + address);
                                    startActivity(i.createChooser(i, "Share Using: "));
                                }
                            }
                        }
                );
        } else
            requestPermissions();

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location mLastLocation = locationResult.getLastLocation();
            String address = getCompleteAddressString(context, mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "My Location  : " + "http://maps.google.com/maps?saddr=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "  Address is " + address);
            startActivity(i.createChooser(i, "Share Using: "));

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                ((Activity) context),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                //cityName = returnedAddress.getLocality();
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("@LocationAddress", "My Current location address : \n" + addresses + "\n" + returnedAddress + "\n" + strReturnedAddress.toString());
            } else {
                Log.e("@AddressNotFound", "My Current location address No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("@ErrorInAAddress", "My Current location address Cannot get Address!");
        }
        return strAdd;
    }

}
