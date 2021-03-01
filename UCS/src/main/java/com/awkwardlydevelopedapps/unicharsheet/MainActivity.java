package com.awkwardlydevelopedapps.unicharsheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

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

        //AdSingleton.Instance().enableTestDevice();
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

    /**
     * Inner classes
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
