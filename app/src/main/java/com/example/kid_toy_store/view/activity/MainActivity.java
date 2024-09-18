package com.example.kid_toy_store.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.databinding.ActivityMainBinding;
import com.example.kid_toy_store.view.fragment.NotifyFragment;
import com.example.kid_toy_store.view.fragment.HomeFragment;
import com.example.kid_toy_store.view.fragment.ProfileFragment;
import com.example.kid_toy_store.view.fragment.SearchFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";

    ActivityMainBinding mBinding;
    View mView;

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    NotifyFragment notifyFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        notifyFragment = new NotifyFragment();
        profileFragment = new ProfileFragment();

        setCurrentFragment(homeFragment);

        mBinding.bottomNavigation
                .setOnNavigationItemSelectedListener(this);

        if (getIntent().hasExtra("fragment")) {
            String fragmentName = getIntent().getStringExtra("fragment");
            if ("ProfileFragment".equals(fragmentName)) {
                // Chuyển đến ProfileFragment
                setCurrentFragment(profileFragment);
            }
        }

    }



    private void badgeSetup(int id, int alerts) {
        BadgeDrawable badge = mBinding.bottomNavigation.getOrCreateBadge(id);
        badge.setVisible(true);
        badge.setNumber(alerts);
    }

    private void badgeClear(int id) {
        BadgeDrawable badgeDrawable = mBinding.bottomNavigation.getBadge(id);
        if (badgeDrawable != null) {
            badgeDrawable.setVisible(false);
            badgeDrawable.clearNumber();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_home){
            setCurrentFragment(homeFragment);
            Log.i(TAG, "Home Selected");
            badgeClear(R.id.action_home);
            return true;
        } else if (id == R.id.action_search) {
            setCurrentFragment(searchFragment);
            Log.i(TAG, "Search Selected");
            badgeClear(R.id.action_search);
            return true;
        } else if (id == R.id.action_notify) {
            setCurrentFragment(notifyFragment);
            Log.i(TAG, "Notify Selected");
            badgeClear(R.id.action_notify);
            return true;
        } else if (id == R.id.action_profile) {
            setCurrentFragment(profileFragment);
            Log.i(TAG, "Profile Selected");
            badgeClear(R.id.action_profile);
            return true;
        }

        return false;
    }

    private void setCurrentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fl_wrapper, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}