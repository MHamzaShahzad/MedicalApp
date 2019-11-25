package com.example.medicalapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.medicalapp.Constants;
import com.example.medicalapp.FirstAidFragment;
import com.example.medicalapp.FragmentContactUs;
import com.example.medicalapp.FragmentNearbyPlaces;
import com.example.medicalapp.R;
import com.example.medicalapp.reminder.activity.SplashActivityPillsReminder;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;

public class HomeDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_home_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            FirstAidFragment firstAidFragment = new FirstAidFragment();
            Bundle bundle = new Bundle();
            firstAidFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, firstAidFragment).addToBackStack(null).commit();


        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(context, SplashActivityPillsReminder.class));

        } else if (id == R.id.nav_tools) {

            SignOut();

        } else if (id == R.id.nav_nearby_hospitals) {
            FragmentNearbyPlaces fragmentNearbyPlaces = new FragmentNearbyPlaces();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.STRING_ARGUE_NEARBY_ENTITY, "hospital");
            fragmentNearbyPlaces.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragmentNearbyPlaces).addToBackStack(null).commit();

        } else if (id == R.id.nav_nearby_pharmacies) {
            FragmentNearbyPlaces fragmentNearbyPlaces = new FragmentNearbyPlaces();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.STRING_ARGUE_NEARBY_ENTITY, "pharmacy");
            fragmentNearbyPlaces.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragmentNearbyPlaces).addToBackStack(null).commit();

        }  else if (id == R.id.nav_contact_us) {
            FragmentContactUs.newInstance().show(getSupportFragmentManager(), "Contact Us");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
