package com.awkwardlydevelopedapps.unicharsheet.characterList.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awkwardlydevelopedapps.unicharsheet.characterList.adapters.CharacterListAdapter;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;
import com.awkwardlydevelopedapps.unicharsheet.characterList.viewModel.CharacterListViewModel;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.common.DeleteDialog;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class CharacterListFragment extends Fragment
        implements DeleteDialog.NoticeDialogListener {

    private View rootView;

    private DeleteDialog deleteDialog;
    private FloatingActionButton floatingActionButtonAdd;
    private FloatingActionButton floatingActionButtonDelete;
    private CharacterListViewModel viewModel;
    private DataHolderViewModel dataHolderViewModel;

    private RecyclerView recyclerView;
    private CharacterListAdapter adapter;

    public CharacterListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_character_list, container, false);

        // Creating float action button
        floatingActionButtonAdd = rootView.findViewById(R.id.add_button);
        floatingActionButtonAdd.setImageResource(R.drawable.ic_add_black_24);
        floatingActionButtonAdd.setOnClickListener(new FABOnClickListenerAdd());

        floatingActionButtonDelete = rootView.findViewById(R.id.floatingActionButton_character_delete);
        floatingActionButtonDelete.setVisibility(View.GONE);
        floatingActionButtonDelete.setOnClickListener(new FABOnClickListenerDelete());

        // Adapter
        adapter = new CharacterListAdapter(requireContext());
        adapter.setOnItemClickListener(new CharacterItemOnClickListener());
        adapter.setOnItemLongClickListener(new CharacterItemOnLongClickListener());


        // RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        // Adding divider between items
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        //recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new FABOnScrollListener());
        recyclerView.setAdapter(adapter);


        // ViewModel
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(CharacterListViewModel.class);

        dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar(view);

        viewModel.getAllCharacters().observe(getViewLifecycleOwner(), characters -> {
            adapter.setCharacters(characters);
            viewModel.setCharacterList(characters);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // Setups toolbar
    private void setupToolbar(View view) {
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Universal Character Sheet");
        toolbar.setSubtitle("List of characters");
        toolbar.inflateMenu(R.menu.toolbar_menu_character_list);

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(new ToolbarOnMenuClickListener());

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        viewModel.checkCharsAndDelete(adapter);
        floatingActionButtonAdd.show();
        floatingActionButtonDelete.hide();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    /**
     * Inner classes
     */

    private class CharacterItemOnClickListener implements CharacterListAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View itemView, int position) {
            if (!adapter.isShowingChecks()) {
                //Set Character data in MainActivity
                Character character = viewModel.getCharacterList().get(position);

                dataHolderViewModel.setCharacterID(character.id);
                dataHolderViewModel.setCharacterName(character.getCharacterName());
                dataHolderViewModel.setClassName(character.getClassName());
                dataHolderViewModel.setRaceName(character.getRaceName());
                dataHolderViewModel.setImageResourceID(character.getImageResourceId());

                //Navigate to character
                NavHostFragment
                        .findNavController(CharacterListFragment.this)
                        .navigate(CharacterListFragmentDirections.actionCharacterListFragmentToStatsFragment());
            }
        }
    }

    private class CharacterItemOnLongClickListener implements CharacterListAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(View view, int position) {
            Character character = viewModel.getCharacterList().get(position);
            character.setChecked(true);
            adapter.setShowChecks();
            adapter.notifyItemChanged(position);

            handleFABHiding();
            return true;
        }

        private void handleFABHiding() {
            if (floatingActionButtonDelete.isShown()) {
                floatingActionButtonAdd.show();
                floatingActionButtonDelete.hide();
            } else {
                floatingActionButtonDelete.show();
                floatingActionButtonAdd.hide();
            }
        }
    }

    private class FABOnClickListenerAdd implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            navigateToCharacterCreation();
        }

        private void navigateToCharacterCreation() {
            NavHostFragment
                    .findNavController(CharacterListFragment.this)
                    .navigate(CharacterListFragmentDirections.actionCharacterListFragmentToCharacterCreationFragment());
        }
    }

    private class FABOnClickListenerDelete implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            deleteDialog = new DeleteDialog();
            deleteDialog.setNoticeDialogListener(CharacterListFragment.this)
                    .show(getParentFragmentManager(), "DELETE_CHAR_DIALOG");
        }
    }

    private class FABOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (adapter.isShowingChecks()) {
                floatingActionButtonAdd.hide();
            } else if (dy > 0 && floatingActionButtonAdd.getVisibility() == View.VISIBLE) {
                floatingActionButtonAdd.hide();
            } else if (dy < 0 && floatingActionButtonAdd.getVisibility() != View.VISIBLE) {
                floatingActionButtonAdd.show();
            }
        }
    }

    private class ToolbarOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_about) {
                NavHostFragment
                        .findNavController(CharacterListFragment.this)
                        .navigate(CharacterListFragmentDirections.actionCharacterListFragmentToAboutFragment());
                return true;
            } else if (itemId == R.id.action_settings) {
                NavHostFragment
                        .findNavController(CharacterListFragment.this)
                        .navigate(CharacterListFragmentDirections.actionCharacterListFragmentToSettingsFragment());
                return true;
            } else if (itemId == R.id.action_sort_nameAsc) {
                viewModel.orderBy(Sort.BY_NAME_ASC);
            } else if (itemId == R.id.action_sort_nameDesc) {
                viewModel.orderBy(Sort.BY_NAME_DESC);
            }
            return false;
        }
    }
}
