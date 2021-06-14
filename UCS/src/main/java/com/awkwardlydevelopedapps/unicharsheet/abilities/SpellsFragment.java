package com.awkwardlydevelopedapps.unicharsheet.abilities;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.awkwardlydevelopedapps.unicharsheet.abilities.viewModel.SpellSortStateViewModel;
import com.awkwardlydevelopedapps.unicharsheet.common.PopupOnSortClickListener;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;

public class SpellsFragment extends Fragment
        implements SpellsFragmentList.ChangeFragmentCallback,
        SpellsFragmentDisplay.ChangeFragmentCallback {

    private View rootView;

    private int characterID;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconID;
    private MenuItem sortItemGroup;

    private FragmentManager fragmentManager;

    private SpellSortStateViewModel spellSortStateViewModel;

    public SpellsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_spells, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterID();
        characterName = dataHolderViewModel.getCharacterName();
        characterClass = dataHolderViewModel.getClassName();
        characterRace = dataHolderViewModel.getRaceName();
        characterIconID = dataHolderViewModel.getImageResourceID();

        fragmentManager = getChildFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .setReorderingAllowed(true)
                    .replace(R.id.frameLayout_spells_fragment_container, SpellsFragmentList.class, null)
                    .commit();
        }

        spellSortStateViewModel = new ViewModelProvider(requireActivity())
                .get(SpellSortStateViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar_spellsFragment);
        toolbar.inflateMenu(R.menu.toolbar_menu_character_list);

        TextView textViewName = view.findViewById(R.id.toolbar_textView_characterName);
        TextView textViewClass = view.findViewById(R.id.toolbar_textView_characterClass);
        TextView textViewRace = view.findViewById(R.id.toolbar_textView_characterRace);
        ImageView imageView = view.findViewById(R.id.toolbar_statsFragment_icon);

        imageView.setImageResource(characterIconID);
        textViewName.setText(characterName);
        textViewClass.setText(characterClass);
        textViewRace.setText(characterRace);

        sortItemGroup = toolbar.getMenu().findItem(R.id.action_group_sortOrder);

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(new ToolbarOnMenuClickListener());

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(
                toolbar, navController);
    }

    @Override
    public void changeToDisplay() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.frameLayout_spells_fragment_container, SpellsFragmentDisplay.class, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        sortItemGroup.setVisible(false);
    }

    @Override
    public void changeToList() {
        // Just popping backstack
        fragmentManager.popBackStack();
        sortItemGroup.setVisible(true);
    }


    /**
     * Inner classes
     */

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_about) {
                NavHostFragment
                        .findNavController(SpellsFragment.this)
                        .navigate(SpellsFragmentDirections.actionSpellsFragmentToAboutFragment());
                return true;
            } else if (itemId == R.id.action_settings) {
                NavHostFragment
                        .findNavController(SpellsFragment.this)
                        .navigate(SpellsFragmentDirections.actionSpellsFragmentToSettingsFragment());
                return true;
            } else if (itemId == R.id.action_sort_nameAsc) {
                spellSortStateViewModel.changeSortOrder(Sort.BY_NAME_ASC);
                return true;
            } else if (itemId == R.id.action_sort_nameDesc) {
                spellSortStateViewModel.changeSortOrder(Sort.BY_NAME_DESC);
                return true;
            }
            return false;
        }
    }
}
