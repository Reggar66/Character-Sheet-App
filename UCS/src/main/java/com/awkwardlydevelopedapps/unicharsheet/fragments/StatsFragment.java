package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.data.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.PresetAddBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.models.Preset;
import com.awkwardlydevelopedapps.unicharsheet.models.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.StatTabNameChangeDialog;
import com.awkwardlydevelopedapps.unicharsheet.adapters.StatTabsAdapter;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsFragment extends Fragment
        implements StatTabNameChangeDialog.NoticeDialogListener,
        PresetAddBottomSheetDialog.OnApplyListener {

    private StatTabsAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int characterId;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconId;

    private static final String NUMBER_OF_TABS = "NUMBER_OF_TABS";
    private static final String TAB_NAME = "TAB_NAME";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        characterId = ((MainActivity) requireActivity()).characterId;
        characterName = ((MainActivity) requireActivity()).characterName;
        characterClass = ((MainActivity) requireActivity()).characterClass;
        characterRace = ((MainActivity) requireActivity()).characterRace;
        characterIconId = ((MainActivity) requireActivity()).characterIconId;

        adapter = new StatTabsAdapter(getChildFragmentManager());
        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = rootView.findViewById(R.id.tabLayout);
        // Connect TabLayout with a ViewPager
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new OnTabSelected());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar(view);

        // Load tabs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String tabName;
        int numberOfTabs = Integer.parseInt(sharedPreferences.getString(NUMBER_OF_TABS, "3"));
        for (int position = 0; position < numberOfTabs; position++) {
            tabName = sharedPreferences.getString(TAB_NAME + position, "STATS " + position);
            StatsPageFragment statsPage = new StatsPageFragment();
            statsPage.setPage(position + 1);
            adapter.addFragment(statsPage, tabName, position);

        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar_statsFragment);
        toolbar.inflateMenu(R.menu.actionbar_menu);

        TextView textViewName = view.findViewById(R.id.toolbar_textView_characterName);
        TextView textViewClass = view.findViewById(R.id.toolbar_textView_characterClass);
        TextView textViewRace = view.findViewById(R.id.toolbar_textView_characterRace);
        ImageView imageView = view.findViewById(R.id.toolbar_statsFragment_icon);

        imageView.setImageResource(characterIconId);
        textViewName.setText(characterName);
        textViewClass.setText(characterClass);
        textViewRace.setText(characterRace);

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(new ToolbarOnMenuClickListener());

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(
                toolbar, navController);
    }

    public int getSelectedTabNumber() {
        return tabLayout.getSelectedTabPosition();
    }

    public void changeCurrentTabName() {
        StatTabNameChangeDialog dialog = new StatTabNameChangeDialog();
        dialog.setTargetFragment(this, 0); //Setting target Fragment to THIS as we call dialog form THIS FRAGMENT.
        dialog.show(getParentFragmentManager(), "STAT_TAB_CHANGE_DIALOG");
    }

    public void createNewTab() {
        // Create new tab
        int pos = adapter.getCount();
        StatsPageFragment statsPage = new StatsPageFragment();
        statsPage.setPage(pos + 1);
        adapter.addFragment(statsPage, "STATS " + pos, pos);

        // Save number of tabs in SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NUMBER_OF_TABS, String.valueOf(adapter.getCount()));
        editor.apply();

        // Notify about changes
        adapter.notifyDataSetChanged();
        // Go to new tab
        viewPager.setCurrentItem(adapter.getCount());
        // Load tab names
        loadTabNames();
    }

    public void removeTab() {
        if (adapter.getCount() == 3)
            return;

        // Remove
        int pos = viewPager.getCurrentItem();
        adapter.removeFragment(pos);

        // Save number of tabs
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NUMBER_OF_TABS, String.valueOf(adapter.getCount()));
        editor.apply();

        // Notify about changes
        adapter.notifyDataSetChanged();
        // Go one tab back from deleted one
        viewPager.setCurrentItem(pos - 1);
        // Load tab names
        loadTabNames();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String tabName) {
        int idx = tabLayout.getSelectedTabPosition();
        Objects.requireNonNull(tabLayout.getTabAt(idx)).setText(tabName);
        saveTabNameToSharedPrefs(idx, tabName);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    private void saveTabNameToSharedPrefs(int tabNumber, String stringValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TAB_NAME + tabNumber, stringValue);
        editor.apply();
    }

    private void loadTabNames() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String name;

        for (int i = 0; i < adapter.getCount(); i++) {
            name = sharedPreferences.getString(TAB_NAME + i, "STATS " + i);
            Objects.requireNonNull(tabLayout.getTabAt(i)).setText(name);
        }
    }

    private void showPresetBottomDialog() {
        PresetAddBottomSheetDialog bottomSheetDialog =
                new PresetAddBottomSheetDialog();
        bottomSheetDialog.setListener(this);
        bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_ADD_PRESET");


    }

    private void addToPreset(String presetName) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                StatDao statDao = DbSingleton.Instance(requireContext()).getStatDao();
                PresetDao presetDao = DbSingleton.Instance(requireContext()).getPresetDao();
                List<Stat> listOfAllStats = new ArrayList<>(statDao.getAllStats(characterId));
                presetDao.insertPresetListName(new PresetList(presetName));
                for (Stat stat : listOfAllStats) {
                    presetDao.insertPresetStats(new Preset(stat.getName(), stat.getPage(), presetName));
                }
            }
        });
    }

    @Override
    public void applyPreset(@NotNull String presetName) {
        addToPreset(presetName);
    }

    /**
     * Inner Classes
     */

    private class OnTabSelected implements TabLayout.OnTabSelectedListener {
        // Takes care of:
        // * Hiding Delete Button when tab is changed
        // * Clearing stat selection when tab is changed

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            try {
                adapter.getStatPage(viewPager.getCurrentItem()).clearChecks();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_addPreset:
                    showPresetBottomDialog();
                    return true;

                case R.id.action_changeTabName:
                    changeCurrentTabName();
                    return true;

                case R.id.action_about:
                    NavHostFragment
                            .findNavController(StatsFragment.this)
                            .navigate(StatsFragmentDirections.actionStatsFragmentToAboutFragment());
                    return true;

                case R.id.action_createTab:
                    createNewTab();
                    return true;

                case R.id.action_removeTab:
                    removeTab();
                    return true;

                case R.id.action_settings:
                    NavHostFragment
                            .findNavController(StatsFragment.this)
                            .navigate(StatsFragmentDirections.actionStatsFragmentToSettingsFragment());
                    return true;
                default:
                    return false;
            }
        }
    }
}
