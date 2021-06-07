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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.inventory.adapters.InventoryTabsAdapter;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.viewModel.ItemSortStateViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class InventoryFragment extends Fragment {

    private View rootView;
    private Menu popupMenu;

    private int characterID;
    private String characterName;
    private String characterClass;
    private String characterRace;
    private int characterIconID;

    private final static int TAB_EQUIPMENT = 0;
    private final static int TAB_POCKET = 1;
    private final static int TAB_BACKPACK = 2;

    private ItemSortStateViewModel itemSortStateViewModel;

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

        // Inflating custom views that contains icons for tabs
        View viewEquipment = inflater
                .inflate(R.layout.inventory_custom_tab_view, container, false);
        viewEquipment.findViewById(R.id.icon)
                .setBackgroundResource(R.drawable.inventory_tabs_eq_selector);

        View viewSack = inflater
                .inflate(R.layout.inventory_custom_tab_view, container, false);
        viewSack.findViewById(R.id.icon)
                .setBackgroundResource(R.drawable.inventory_tabs_pocket_selector);

        View viewBackpack = inflater
                .inflate(R.layout.inventory_custom_tab_view, container, false);
        viewBackpack.findViewById(R.id.icon)
                .setBackgroundResource(R.drawable.inventory_tabs_backpack_selector);

        // ViewPager setup with TabLayout
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout_inventory);
        InventoryTabsAdapter inventoryTabsAdapter = new InventoryTabsAdapter(this);
        ViewPager2 viewPager = rootView.findViewById(R.id.fragmentContainer_inventory);
        viewPager.setAdapter(inventoryTabsAdapter);
        viewPager.registerOnPageChangeCallback(new PageChangeListener());

        // Setting tab icons by CustomView with mediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setCustomView(viewEquipment);
                    break;
                case 1:
                    tab.setCustomView(viewSack);
                    break;
                case 2:
                    tab.setCustomView(viewBackpack);
                    break;
            }
        }).attach();

        itemSortStateViewModel = new ViewModelProvider(requireActivity())
                .get(ItemSortStateViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);
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

    private void popupMenuVisibilityHandler(int position) {
        if (popupMenu == null)
            return;

        MenuItem sortGroupItem = popupMenu.findItem(R.id.action_group_sortOrder);

        sortGroupItem.setVisible(position == TAB_BACKPACK);
    }

    /**
     * Inner Classes
     */

    private class PageChangeListener extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageSelected(int position) {
            popupMenuVisibilityHandler(position);
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
                itemSortStateViewModel.changeSortOrder(Sort.BY_NAME_ASC);
                return true;
            } else if (itemId == R.id.action_sort_nameDesc) {
                itemSortStateViewModel.changeSortOrder(Sort.BY_NAME_DESC);
                return true;
            } else if (itemId == R.id.action_sort_valueAsc) {
                itemSortStateViewModel.changeSortOrder(Sort.BY_VALUE_ASC);
                return true;
            } else if (itemId == R.id.action_sort_valueDesc) {
                itemSortStateViewModel.changeSortOrder(Sort.BY_VALUE_DESC);
                return true;
            }

            return false;
        }
    }
}
