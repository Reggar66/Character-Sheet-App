package com.awkwardlydevelopedapps.unicharsheet.stats;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper;
import com.awkwardlydevelopedapps.unicharsheet.service.AppReviewSingleton;
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.PresetDao;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Preset;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.PresetList;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.stats.adapters.StatTabsAdapter;
import com.awkwardlydevelopedapps.unicharsheet.stats.dao.StatDao;
import com.awkwardlydevelopedapps.unicharsheet.stats.dialogs.PresetAddBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.stats.dialogs.StatTabNameChangeBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatsFragment extends Fragment
        implements PresetAddBottomSheetDialog.OnApplyListener,
        StatTabNameChangeBottomSheetDialog.OnApplyStatTabNameListener {

    private StatTabsAdapter adapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private int characterId;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconId;

    private static final String KEY_NUMBER_OF_TABS = "NUMBER_OF_TABS";
    private static final String KEY_TAB_NAME = "TAB_NAME";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        DataHolderViewModel dataHolderViewModel =
                new ViewModelProvider(requireActivity()).get(DataHolderViewModel.class);

        characterId = dataHolderViewModel.getCharacterID();
        characterName = dataHolderViewModel.getCharacterName();
        characterClass = dataHolderViewModel.getClassName();
        characterRace = dataHolderViewModel.getRaceName();
        characterIconId = dataHolderViewModel.getImageResourceID();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int numberOfTabs = Integer.parseInt(sharedPreferences.getString(KEY_NUMBER_OF_TABS, "3"));
        adapter = new StatTabsAdapter(requireContext(), requireActivity(), numberOfTabs);
        viewPager = rootView.findViewById(R.id.stat_viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = rootView.findViewById(R.id.tabLayout);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setupToolbar(view);

        viewPager.setAdapter(adapter);

        // Connect viewPager with TabLayout via mediator.
        // Also it is responsible of setting tab names.
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getTitle(position)))
                .attach();

        // Initialize app review
        AppReviewSingleton.INSTANCE.inAppReview(requireContext(), requireActivity());


        LogWrapper
                .Companion
                .v("INFO", adapter.toStringTitleList());
        LogWrapper
                .Companion
                .v("INFO", "StatsFragment: " + "onViewCreated() - adapter: "
                        + adapter.toString());
        LogWrapper
                .Companion
                .v("INFO", "StatsFragment: " + "onViewCreated() - end.");
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

    public void changeCurrentTabName() {
        StatTabNameChangeBottomSheetDialog bottomSheetDialog =
                new StatTabNameChangeBottomSheetDialog();
        bottomSheetDialog.setListener(this);
        bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_CHANGE_TAB_NAME");

    }

    public void createNewTab() {
        adapter.addPage();
        // Go to created tab
        viewPager.setCurrentItem(adapter.getItemCount());
    }

    public void removeTab() {
        adapter.removePage();
    }

    private void saveTabNameToSharedPrefs(int tabNumber, String stringValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_TAB_NAME + tabNumber, stringValue);
        editor.apply();
    }

    private void showPresetBottomDialog() {
        PresetAddBottomSheetDialog bottomSheetDialog =
                new PresetAddBottomSheetDialog();
        bottomSheetDialog.setListener(this);
        bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_ADD_PRESET");
    }

    private void addToPreset(String presetName) {
        ExecSingleton.getInstance().execute(() -> {
            StatDao statDao = DbSingleton.Instance(requireContext()).getStatDao();
            PresetDao presetDao = DbSingleton.Instance(requireContext()).getPresetDao();
            List<Stat> listOfAllStats = new ArrayList<>(statDao.getAllStats(characterId));
            presetDao.insertPresetListName(new PresetList(presetName));
            for (Stat stat : listOfAllStats) {
                presetDao.insertPresetStats(new Preset(stat.getName(), stat.getPage(), presetName));
            }
        });
    }

    @Override
    public void applyPreset(@NotNull String presetName) {
        addToPreset(presetName);
    }

    @Override
    public void applyStatTabName(@NotNull String tabName) {
        int idx = tabLayout.getSelectedTabPosition();
        Objects.requireNonNull(tabLayout.getTabAt(idx)).setText(tabName);
        saveTabNameToSharedPrefs(idx, tabName);
    }

    // ****
    // Inner classes
    // ****

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_addPreset) {
                showPresetBottomDialog();
                return true;
            } else if (itemId == R.id.action_changeTabName) {
                changeCurrentTabName();
                return true;
            } else if (itemId == R.id.action_about) {
                NavHostFragment
                        .findNavController(StatsFragment.this)
                        .navigate(StatsFragmentDirections.actionStatsFragmentToAboutFragment());
                return true;
            } else if (itemId == R.id.action_createTab) {
                createNewTab();
                return true;
            } else if (itemId == R.id.action_removeTab) {
                removeTab();
                return true;
            } else if (itemId == R.id.action_settings) {
                NavHostFragment
                        .findNavController(StatsFragment.this)
                        .navigate(StatsFragmentDirections.actionStatsFragmentToSettingsFragment());
                return true;
            } else if (itemId == R.id.action_sort_nameAsc) {
                // TODO right now any of sorting won't work since we have no reference to given fragment
//                adapter.getStatPage(viewPager.getCurrentItem()).sortStatsBy(Sort.BY_NAME_ASC);
                return true;
            } else if (itemId == R.id.action_sort_nameDesc) {
//                adapter.getStatPage(viewPager.getCurrentItem()).sortStatsBy(Sort.BY_NAME_DESC);
                return true;
            } else if (itemId == R.id.action_sort_valueAsc) {
//                adapter.getStatPage(viewPager.getCurrentItem()).sortStatsBy(Sort.BY_VALUE_ASC);
                return true;
            } else if (itemId == R.id.action_sort_valueDesc) {
//                adapter.getStatPage(viewPager.getCurrentItem()).sortStatsBy(Sort.BY_VALUE_DESC);
                return true;
            }
            return false;
        }
    }
}
