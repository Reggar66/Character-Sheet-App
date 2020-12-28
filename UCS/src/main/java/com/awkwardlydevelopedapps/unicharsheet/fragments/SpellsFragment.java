package com.awkwardlydevelopedapps.unicharsheet.fragments;

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

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.DataHolderViewModel;

public class SpellsFragment extends Fragment
        implements SpellsFragmentList.ChangeFragmentCallback,
        SpellsFragmentDisplay.ChangeFragmentCallback {

    private View rootView;

    private int characterID;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconID;

    private FragmentManager fragmentManager;

    public SpellsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_spells, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterId();
        characterName = dataHolderViewModel.getCharacterName();
        characterClass = dataHolderViewModel.getClassName();
        characterRace = dataHolderViewModel.getRaceName();
        characterIconID = dataHolderViewModel.getImageResourceId();

        fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_spells_fragment_container, getNewSpellsFragmentList());
        fragmentTransaction.commit();


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

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(new ToolbarOnMenuClickListener());

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(
                toolbar, navController);
    }

    private Fragment getNewSpellsFragmentList() {
        SpellsFragmentList spellsFragmentList = new SpellsFragmentList();
        spellsFragmentList.setChangeFragmentCallback(SpellsFragment.this);
        return spellsFragmentList;
    }

    private Fragment getNewSpellsFragmentDisplay() {
        SpellsFragmentDisplay spellsFragmentDisplay = new SpellsFragmentDisplay();
        spellsFragmentDisplay.setChangeFragmentCallback(SpellsFragment.this);
        return spellsFragmentDisplay;
    }

    @Override
    public void changeToDisplay() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_spells_fragment_container, getNewSpellsFragmentDisplay());
        fragmentTransaction.commit();
    }

    @Override
    public void changeToList() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_spells_fragment_container, getNewSpellsFragmentList());
        fragmentTransaction.commit();
    }


    /**
     * Inner classes
     */

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_about:
                    NavHostFragment
                            .findNavController(SpellsFragment.this)
                            .navigate(SpellsFragmentDirections.actionSpellsFragmentToAboutFragment());
                    return true;
                case R.id.action_settings:
                    NavHostFragment
                            .findNavController(SpellsFragment.this)
                            .navigate(SpellsFragmentDirections.actionSpellsFragmentToSettingsFragment());
                    return true;
                default:
                    return false;
            }
        }
    }
}
