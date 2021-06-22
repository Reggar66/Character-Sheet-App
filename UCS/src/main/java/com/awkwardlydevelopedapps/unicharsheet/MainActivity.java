package com.awkwardlydevelopedapps.unicharsheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.awkwardlydevelopedapps.unicharsheet.service.AdSingleton;
import com.awkwardlydevelopedapps.unicharsheet.settings.PreferenceFragment;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientationCheck();
        appThemeCheck();

        AdSingleton.Instance().enableTestDevice();
        AdSingleton.Instance().consentInfoUpdate(this);
        AdSingleton.Instance().adInit(this);
        AdView adView = findViewById(R.id.adView);
        AdSingleton.Instance().adLoad(this, adView);

        navController = Navigation.findNavController(this, R.id.main_container);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        navController.addOnDestinationChangedListener(new BottomNavAppearanceListener());

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // No need to set manually fragment transaction. Starting point is set automatically by NavComponent.
    }

    // Since we let user to choose either to lock orientation or not, we suppress code check.
    @SuppressLint("SourceLockedOrientationActivity")
    private void orientationCheck() {
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        boolean orientationLock = sharedPreferences
                .getBoolean(PreferenceFragment.KEY_ORIENTATION_LOCK, true);

        if (orientationLock) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    private void appThemeCheck() {
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);

        // Get chosen theme from prefs with default value same as device
        String themeOption = sharedPreferences
                .getString(PreferenceFragment.KEY_APP_THEME, PreferenceFragment.THEME_OPTION_DEVICE);

        switch (themeOption) {
            case PreferenceFragment.THEME_OPTION_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case PreferenceFragment.THEME_OPTION_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case PreferenceFragment.THEME_OPTION_DEVICE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    /**
     * Inner listener class responsible for managing visibility of navigation bar.
     */
    private class BottomNavAppearanceListener implements NavController.OnDestinationChangedListener {

        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            if (destination.getId() == R.id.characterListFragment
                    || destination.getId() == R.id.aboutFragment
                    || destination.getId() == R.id.characterCreationFragment
                    || destination.getId() == R.id.settingsFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }
}
