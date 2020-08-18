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

    public int characterId;
    public String characterName;
    public String characterClass;
    public String characterRace;
    public int characterIconId;

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


    public void inAppReview() {
        // Right now shown inside of statistic fragment. Maybe there is a better place?
        InAppReview inAppReview = new InAppReview(this, this);
        if (inAppReview.checkIfWeeksPassed(InAppReview.ZERO_WEEKS)) {

            if (inAppReview.getCurrentWeek() != 0) {
                inAppReview.requestReview();
            }
            inAppReview.updateWeekToCheck();
        }
    }

    public void setSelectedCharacterData(int characterId, int characterIconId, String characterName, String characterClass, String characterRace) {
        this.characterId = characterId;
        this.characterIconId = characterIconId;
        this.characterName = characterName;
        this.characterClass = characterClass;
        this.characterRace = characterRace;
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
