package com.awkwardlydevelopedapps.unicharsheet.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper;
import com.awkwardlydevelopedapps.unicharsheet.service.AdSingleton;
import com.google.ads.consent.ConsentInformation;
import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.takisoft.preferencex.SwitchPreferenceCompat;

public class PreferenceFragment
        extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    private final static String KEY_NUMBER_OF_TABS = "NUMBER_OF_TABS";
    private final static String KEY_CONSENT = "CONSENT";
    private final static String KEY_FEEDBACK = "FEEDBACK";
    private final static String KEY_ABOUT = "ABOUT_SCREEN";

    public final static String KEY_APP_THEME = "APP_THEME";
    public final static String THEME_OPTION_LIGHT = "THEME_LIGHT";
    public final static String THEME_OPTION_DARK = "THEME_DARK";
    public final static String THEME_OPTION_NIGHT = "THEME_NIGHT";
    public final static String THEME_OPTION_DEVICE = "THEME_DEVICE";

    public final static String KEY_ORIENTATION_LOCK = "ORIENTATION_LOCK";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SwitchPreferenceCompat switchPreferenceCompatOrientationLock = findPreference(KEY_ORIENTATION_LOCK);
        if (switchPreferenceCompatOrientationLock != null) {
            switchPreferenceCompatOrientationLock.setOnPreferenceChangeListener(new OrientationLockListener());
        }

        ListPreference listPreferenceAppTheme = findPreference(KEY_APP_THEME);
        if (listPreferenceAppTheme != null) {
            listPreferenceAppTheme.setOnPreferenceChangeListener(new AppThemeOptionListener());
        }

        EditTextPreference editTextPreferenceTabsNumber = findPreference(KEY_NUMBER_OF_TABS);
        if (editTextPreferenceTabsNumber != null)
            editTextPreferenceTabsNumber.setOnPreferenceChangeListener(PreferenceFragment.this);

        Preference preferenceConsent = findPreference(KEY_CONSENT);
        if (preferenceConsent != null) {
            preferenceConsent.setOnPreferenceClickListener(new ConsentOnClickListener());
            if (ConsentInformation.getInstance(requireContext()).isRequestLocationInEeaOrUnknown())
                preferenceConsent.setVisible(true);
        }

        Preference preferenceFeedback = findPreference(KEY_FEEDBACK);
        if (preferenceFeedback != null) {
            preferenceFeedback.setOnPreferenceClickListener(new SendFeedbackListener());
        }

        Preference preferenceAboutScreen = findPreference(KEY_ABOUT);
        if (preferenceAboutScreen != null) {
            preferenceAboutScreen.setOnPreferenceClickListener(preference -> {
                // Navigates to 'About' screen when option is clicked
                NavHostFragment
                        .findNavController(PreferenceFragment.this)
                        .navigate(SettingsFragmentDirections.actionSettingsFragmentToAboutFragment());
                return true;
            });
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (KEY_NUMBER_OF_TABS.equals(preference.getKey())) {
            if (Integer.parseInt((String) newValue) >= 3) {
                Toast.makeText(
                        requireContext(),
                        "Changed to " + newValue + " pages.",
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            } else {
                Toast.makeText(
                        requireContext(),
                        "Not changed. Minimum number is 3.",
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }
        return false;
    }

    //****
    //Inner classes
    //****

    /**
     * Responsible for handling clicks on "Reset Consent" option
     */
    private class ConsentOnClickListener implements Preference.OnPreferenceClickListener {

        @Override
        public boolean onPreferenceClick(Preference preference) {
            AdSingleton.Instance().resetConsentOption(requireContext());
            return true;
        }
    }

    /**
     * Responsible for handling clicks on "Send Feedback" option
     */
    private class SendFeedbackListener implements Preference.OnPreferenceClickListener {

        String[] addresses = {"awkwardly.developed.apps@gmail.com"};
        String subject = "UCS - feedback";
        String extraText = "Hello there,\n";

        @Override
        public boolean onPreferenceClick(Preference preference) {
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
            mailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            mailIntent.putExtra(Intent.EXTRA_TEXT, extraText);
            if (mailIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(mailIntent);
            }
            return true;
        }
    }

    /**
     * Responsible for handling preference change for "Lock orientation" option
     */
    private class OrientationLockListener implements Preference.OnPreferenceChangeListener {

        // Since we let user to choose either to lock orientation or not, we suppress code check.
        @SuppressLint("SourceLockedOrientationActivity")
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if ((boolean) newValue) {
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LogWrapper.Companion.v(
                        "INFO",
                        "Preference Orientation Lock: now should lock orientation"
                );
            } else {
                requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                LogWrapper.Companion.v(
                        "INFO",
                        "Preference Orientation Lock: now should unlock orientation"
                );
            }
            return true;
        }
    }

    /**
     * Responsible for handling preference change for "App theme" option
     */
    private class AppThemeOptionListener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch ((String) newValue) {
                case THEME_OPTION_LIGHT:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case THEME_OPTION_DARK:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case THEME_OPTION_DEVICE:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
            }
            return true;
        }
    }
}
