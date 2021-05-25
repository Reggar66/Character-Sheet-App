package com.awkwardlydevelopedapps.unicharsheet.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.awkwardlydevelopedapps.unicharsheet.common.PopupOnSortClickListener;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.BackpackFragment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.equipment.EquipmentFragment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.PocketFragment;
import com.google.android.material.tabs.TabLayout;

public class InventoryFragment extends Fragment {

    private View rootView;
    private TabLayout tabLayout;
    private ImageView lastTabIcon;
    private Menu popupMenu;

    private int characterID;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconID;

    private final static int TAB_EQUIPMENT = 0;
    private final static int TAB_POCKET = 1;
    private final static int TAB_BACKPACK = 2;

    private final EquipmentFragment equipmentFragment = new EquipmentFragment();
    private final PocketFragment pocketFragment = new PocketFragment();
    private final BackpackFragment backpackFragment = new BackpackFragment();

    private PopupOnSortClickListener popupOnSortClickListener;

    public void setPopupOnSortClickListener(PopupOnSortClickListener popupOnSortClickListener) {
        this.popupOnSortClickListener = popupOnSortClickListener;
    }

    public InventoryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inventory, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterID();
        characterName = dataHolderViewModel.getCharacterName();
        characterClass = dataHolderViewModel.getClassName();
        characterRace = dataHolderViewModel.getRaceName();
        characterIconID = dataHolderViewModel.getImageResourceID();

        tabLayout = rootView.findViewById(R.id.tabLayout_inventory);
        tabLayout.addOnTabSelectedListener(new TabOnClick());

        View viewEquipment = getLayoutInflater().inflate(R.layout.inventory_custom_tab_view, null);
        viewEquipment.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_robe);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewEquipment));

        View viewSack = getLayoutInflater().inflate(R.layout.inventory_custom_tab_view, null);
        viewSack.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_cash);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewSack));

        View viewBackpack = getLayoutInflater().inflate(R.layout.inventory_custom_tab_view, null);
        viewBackpack.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_backpack);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewBackpack));

        //sets parent fragment, so we can retrieve it inside backpack and set listener
        backpackFragment.setParentInventoryFragment(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer_inventory, new EquipmentFragment()).commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        lastTabIcon.getBackground().setTintList(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // So it wont clear icon tint color selected when screen gets locked and unlocked.
        int iconColorSelected = ContextCompat.getColor(requireContext(), R.color.colorAccent);
        lastTabIcon.getBackground().setTint(iconColorSelected);
    }

    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar_inventoryFragment);
        toolbar.inflateMenu(R.menu.toolbar_menu_character_list);

        TextView textViewName = view.findViewById(R.id.toolbar_textView_characterName);
        TextView textViewClass = view.findViewById(R.id.toolbar_textView_characterClass);
        TextView textViewRace = view.findViewById(R.id.toolbar_textView_characterRace);
        ImageView imageView = view.findViewById(R.id.toolbar_statsFragment_icon);

        imageView.setImageResource(characterIconID);
        textViewName.setText(characterName);
        textViewClass.setText(characterClass);
        textViewRace.setText(characterRace);

        // Set visibility of sorting by value
        popupMenu = toolbar.getMenu();
        popupMenu.findItem(R.id.action_sort_valueAsc).setVisible(true);
        popupMenu.findItem(R.id.action_sort_valueDesc).setVisible(true);
        popupMenu.findItem(R.id.action_group_sortOrder).setVisible(false);

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(new ToolbarOnMenuClickListener());

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(
                toolbar, navController);
    }

    /**
     * Inner Classes
     */

    private class TabOnClick implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // Icon color change (indicator)
            ImageView icon = tab.getCustomView().findViewById(R.id.icon);
            lastTabIcon = icon;
            int iconColorSelected = ContextCompat.getColor(requireContext(), R.color.colorAccent);
            icon.getBackground().setTint(iconColorSelected);

            Fragment fragmentToReplaceWith = equipmentFragment;
            switch (tab.getPosition()) {
                case TAB_EQUIPMENT:
                    fragmentToReplaceWith = equipmentFragment;
                    break;
                case TAB_POCKET:
                    fragmentToReplaceWith = pocketFragment;
                    break;
                case TAB_BACKPACK:
                    fragmentToReplaceWith = backpackFragment;
                    break;
            }

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer_inventory, fragmentToReplaceWith).commit();

            popupMenuVisibilityHandler(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // Remove icon color(indicator)
            ImageView icon = tab.getCustomView().findViewById(R.id.icon);
            icon.getBackground().setTintList(null);

            // Disable checks on items to delete when changing tab
            if (tab.getPosition() == 2) {
                backpackFragment.removeChecks();
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }

        private void popupMenuVisibilityHandler(TabLayout.Tab tab) {
            if (popupMenu == null)
                return;

            MenuItem sortGroupItem = popupMenu.findItem(R.id.action_group_sortOrder);

            if (tab.getPosition() == TAB_BACKPACK) {
                sortGroupItem.setVisible(true);
            } else {
                sortGroupItem.setVisible(false);
            }
        }
    }

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_about) {
                NavHostFragment
                        .findNavController(InventoryFragment.this)
                        .navigate(InventoryFragmentDirections.actionInventoryFragmentToAboutFragment());
                return true;
            } else if (itemId == R.id.action_settings) {
                NavHostFragment
                        .findNavController(InventoryFragment.this)
                        .navigate(InventoryFragmentDirections.actionInventoryFragmentToSettingsFragment());
                return true;
            } else if (itemId == R.id.action_sort_nameAsc) {
                popupOnSortClickListener.onPopupSortByNameAsc();
                return true;
            } else if (itemId == R.id.action_sort_nameDesc) {
                popupOnSortClickListener.onPopupSortByNameDesc();
                return true;
            } else if (itemId == R.id.action_sort_valueAsc) {
                popupOnSortClickListener.onPopupSortByValueAsc();
                return true;
            } else if (itemId == R.id.action_sort_valueDesc) {
                popupOnSortClickListener.onPopupSortByValueDesc();
                return true;
            }


            return false;
        }
    }
}