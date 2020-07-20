package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.spell.Spell;
import com.awkwardlydevelopedapps.unicharsheet.spell.SpellAdapter;
import com.awkwardlydevelopedapps.unicharsheet.spell.SpellsDialog;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.SpellsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awkwardlydevelopedapps.unicharsheet.DeleteDialog;
import com.awkwardlydevelopedapps.unicharsheet.R;

import java.util.List;
import java.util.Objects;

public class SpellsFragmentList extends Fragment
        implements
        DeleteDialog.NoticeDialogListener,
        SpellsDialog.NoticeDialogListener {

    private FloatingActionButton floatingActionButtonAdd;
    private FloatingActionButton floatingActionButtonDelete;

    private SpellsViewModel viewModel;

    //RecyclerView
    private RecyclerView recyclerView;
    private SpellAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private int charId;

    private changeFragmentCallback callback;

    public SpellsFragmentList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spells_list, container, false);

        charId = ((MainActivity) requireActivity()).characterId;

        floatingActionButtonAdd = rootView.findViewById(R.id.add_button);
        floatingActionButtonAdd.setImageResource(R.drawable.ic_add_black_24);
        floatingActionButtonAdd.setOnClickListener(new FABAddSpellOnClickListener());

        floatingActionButtonDelete = rootView.findViewById(R.id.floatingActionButton_spells_delete);
        floatingActionButtonDelete.setOnClickListener(new FABDeleteSpellOnClickListener());
        floatingActionButtonDelete.hide();

        adapter = new SpellAdapter();
        adapter.setOnItemLongClickListener(new OnSpellItemLongClickListener());
        adapter.setOnItemClickListener(new OnSpellItemClickListener());

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = rootView.findViewById(R.id.spells_recyclerView);
        recyclerView.addOnScrollListener(new FABHideListener());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        viewModel = new ViewModelProvider(this,
                new SpellsViewModel.SpellsViewModelFactory(requireActivity().getApplication(), charId))
                .get(SpellsViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllSpells().observe(getViewLifecycleOwner(), new Observer<List<Spell>>() {
            @Override
            public void onChanged(List<Spell> spells) {
                adapter.setSpells(spells);
                viewModel.setSpells(spells);
            }
        });
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
    public void onSpellCreateDialogPositiveClick(DialogFragment dialog, String spellName, String imageResId) {
        viewModel.insert(new Spell(spellName, "", "", "", "", "", "", imageResId, charId));
    }

    @Override
    public void onSpellCreateDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    public void setChangeFragmentCallback(changeFragmentCallback callback) {
        this.callback = callback;
    }

    /**
     * Interfaces
     */

    public interface changeFragmentCallback {
        void changeToDisplay(int spellId);
    }

    /**
     * Inner classes
     */

    private class FABAddSpellOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            SpellsDialog dialog = new SpellsDialog();
            dialog.setTargetFragment(SpellsFragmentList.this, 0);
            dialog.show(getParentFragmentManager(), "CREATE_SPELL_DIALOG");
        }
    }

    private class FABDeleteSpellOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.setTargetFragment(SpellsFragmentList.this, 0);
            deleteDialog.show(getParentFragmentManager(), "DELETE_SPELLS_DIALOG");
        }
    }

    private class OnSpellItemLongClickListener implements SpellAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(View view, int position) {
            Spell spell = viewModel.getSpells().get(position);
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
            Spell spell = viewModel.getSpells().get(position);
            callback.changeToDisplay(spell.id);
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
