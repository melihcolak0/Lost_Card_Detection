package com.colak.sukkb.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.colak.sukkb.Fragment.EkleFragment;
import com.colak.sukkb.Fragment.IhbarFragment;
import com.colak.sukkb.Fragment.ProfileFragment;
import com.colak.sukkb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomView;
    private IhbarFragment ihbarFragment;
    private EkleFragment ekleFragment;
    private ProfileFragment profileFragment;
    private FragmentTransaction transaction;
    private Toolbar mToolbar;


    private void init(){

        mBottomView = (BottomNavigationView)findViewById(R.id.main_activity_bottomView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);


        ihbarFragment = new IhbarFragment();
        ekleFragment = new EkleFragment();
        profileFragment = new ProfileFragment();

        fragmentiAyarla(ihbarFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mBottomView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();

                if (itemId == R.id.bottom_nav_ic_ihbarlar) {
                    fragmentiAyarla(ihbarFragment);
                    return true;
                } else if (itemId == R.id.bottom_nav_ic_ihbarEkle) {
                    fragmentiAyarla(ekleFragment);
                    return true;
                } else if (itemId == R.id.bottom_nav_ic_profile) {
                    fragmentiAyarla(profileFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void fragmentiAyarla(Fragment fragment){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_frameLayout, fragment);
        transaction.commit();
    }



}