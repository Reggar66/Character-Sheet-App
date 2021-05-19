package com.awkwardlydevelopedapps.unicharsheet.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.awkwardlydevelopedapps.unicharsheet.service.AdSingleton;
import com.google.ads.consent.ConsentInformation;
import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.awkwardlydevelopedapps.unicharsheet.R;

public class PreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private final static String NUMBER_OF_TABS = "NUMBER_OF_TABS";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference editTextPreferenceTabsNumber = findPreference(NUMBER_OF_TABS);
        if (editTextPreferenceTabsNumber != null)
            editTextPreferenceTabsNumber.setOnPreferenceChangeListener(PreferenceFragment.this);

        Preference preferenceConsent = findPreference("consent");
        if (preferenceConsent != null) {
            preferenceConsent.setOnPreferenceClickListener(new ConsentOnClickListener());
            if (ConsentInformation.getInstance(requireContext()).isRequestLocationInEeaOrUnknown())
                preferenceConsent.setVisible(true);

        }

        Preference preferenceFeedback = findPreference("feedback");
        if (preferenceFeedback != null) {
            preferenceFeedback.setOnPreferenceClickListener(new SendFeedbackListener());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (NUMBER_OF_TABS.equals(preference.getKey())) {
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

    /**
     * Inner classes
     */

    private class ConsentOnClickListener implements Preference.OnPreferenceClickListener {

        @Override
        public boolean onPreferenceClick(Preference preference) {
            AdSingleton.Instance().resetConsentOption(requireContext());
            return true;
        }
    }

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
}
