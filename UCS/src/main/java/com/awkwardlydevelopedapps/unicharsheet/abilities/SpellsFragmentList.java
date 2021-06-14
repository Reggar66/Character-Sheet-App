package com.awkwardlydevelopedapps.unicharsheet.abilities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.awkwardlydevelopedapps.unicharsheet.abilities.viewModel.SpellSortStateViewModel;
import com.awkwardlydevelopedapps.unicharsheet.common.PopupOnSortClickListener;
import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;
import com.awkwardlydevelopedapps.unicharsheet.abilities.viewModel.SpellsViewModel;
import com.awkwardlydevelopedapps.unicharsheet.abilities.adapters.SpellAdapter;
import com.awkwardlydevelopedapps.unicharsheet.abilities.dialogs.SpellCreateBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awkwardlydevelopedapps.unicharsheet.common.DeleteDialog;
import com.awkwardlydevelopedapps.unicharsheet.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SpellsFragmentList extends Fragment
        implements
        DeleteDialog.NoticeDialogListener,
        PopupOnSortClickListener,
        SpellCreateBottomSheetDialog.NoticeDialogListener {

    private FloatingActionButton floatingActionButtonAdd;
    private FloatingActionButton floatingActionButtonDelete;

    private SpellsViewModel viewModel;
    private DataHolderViewModel dataHolderViewModel;
    private SpellSortStateViewModel spellSortStateViewModel;

    private SpellAdapter adapter;

    private int characterID;

    private ChangeFragmentCallback callback;

    public interface ChangeFragmentCallback {
        void changeToDisplay();
    }

    public SpellsFragmentList() {

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        try {
            callback = (ChangeFragmentCallback) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    getParentFragment().toString()
                            + " must implement SpellsFragmentList.ChangeFragmentCallback"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spells_list, container, false);

        dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);
        characterID = dataHolderViewModel.getCharacterID();

        floatingActionButtonAdd = rootView.findViewById(R.id.add_button);
        floatingActionButtonAdd.setImageResource(R.drawable.ic_add_black_24);
        floatingActionButtonAdd.setOnClickListener(new FABAddSpellOnClickListener());

        floatingActionButtonDelete = rootView.findViewById(R.id.floatingActionButton_spells_delete);
        floatingActionButtonDelete.setOnClickListener(new FABDeleteSpellOnClickListener());
        floatingActionButtonDelete.hide();

        adapter = new SpellAdapter();
        adapter.setOnItemLongClickListener(new OnSpellItemLongClickListener());
        adapter.setOnItemClickListener(new OnSpellItemClickListener());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.spells_recyclerView);
        recyclerView.addOnScrollListener(new FABHideListener());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        viewModel = new ViewModelProvider(this,
                new SpellsViewModel.SpellsViewModelFactory(requireActivity().getApplication(), characterID))
                .get(SpellsViewModel.class);

        spellSortStateViewModel = new ViewModelProvider(requireActivity())
                .get(SpellSortStateViewModel.class);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllSpells().observe(getViewLifecycleOwner(), spells -> {
            adapter.setSpells(spells);
            viewModel.setSpellList(spells);
        });

        spellSortStateViewModel.getSortOrderLiveData()
                .observe(getViewLifecycleOwner(), sortOrder -> viewModel.orderBy(sortOrder));
    }

    @Override
    public void onPause() {
        super.onPause();
        clearChecks();
    }

    private void clearChecks() {
        if (adapter.isShowingChecks()) {
            adapter.setShowChecks();
            floatingActionButtonDelete.hide();
            floatingActionButtonAdd.show();
        }
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        viewModel.checkSpellsAndDelete(adapter);
        floatingActionButtonDelete.hide();
        floatingActionButtonAdd.show();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    @Override
    public void onPopupSortByNameAsc() {
        viewModel.orderBy(Sort.BY_NAME_ASC);
    }

    @Override
    public void onPopupSortByNameDesc() {
        viewModel.orderBy(Sort.BY_NAME_DESC);
    }

    @Override
    public void onPopupSortByValueAsc() {
        // No values to sort by
    }

    @Override
    public void onPopupSortByValueDesc() {
        // No values to sort by
    }

    @Override
    public void onPositiveClickListenerSpellCreate(@NotNull String spellName, @NotNull String iconName) {
        viewModel.insert(new Spell(
                spellName,
                "",
                "",
                "",
                "",
                "",
                "",
                iconName,
                characterID
        ));
    }

    /**
     * Inner classes
     */

    private class FABAddSpellOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showCreationBottomDialog();
        }

        private void showCreationBottomDialog() {
            SpellCreateBottomSheetDialog bottomSheetDialog =
                    new SpellCreateBottomSheetDialog();
            bottomSheetDialog.show(getChildFragmentManager(), "BOTTOM_DIALOG_CREATE_SPELL");
        }
    }

    private class FABDeleteSpellOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.setNoticeDialogListener(SpellsFragmentList.this)
                    .show(getParentFragmentManager(), "DELETE_SPELLS_DIALOG");
        }
    }

    private class OnSpellItemLongClickListener implements SpellAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(View view, int position) {
            Spell spell = viewModel.getSpellList().get(position);
            spell.setChecked(true);
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

    private class OnSpellItemClickListener implements SpellAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View itemView, int position) {
            Spell spell = viewModel.getSpellList().get(position);
            dataHolderViewModel.setSelectedSpellID(spell.id);
            callback.changeToDisplay();
        }
    }

    private class FABHideListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (adapter.isShowingChecks()) {
                floatingActionButtonAdd.hide();
            } else {
                if (dy > 0 && floatingActionButtonAdd.getVisibility() == View.VISIBLE) {
                    floatingActionButtonAdd.hide();
                } else if (dy < 0 && floatingActionButtonAdd.getVisibility() != View.VISIBLE) {
                    floatingActionButtonAdd.show();
                }
            }
        }
    }
}
