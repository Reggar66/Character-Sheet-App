package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.DeleteDialog;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.adapters.BackpackAdapter;
import com.awkwardlydevelopedapps.unicharsheet.models.Item;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.ItemAddDialog;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.ItemEditDialog;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.BackpackViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class BackpackFragment extends Fragment
        implements ItemEditDialog.NoticeDialogListener,
        DeleteDialog.NoticeDialogListener,
        ItemAddDialog.NoticeDialogListener {

    private View rootView;
    private int charId;
    private FloatingActionButton floatingActionButtonAddItem;
    private FloatingActionButton floatingActionButtonDelete;

    private RecyclerView recyclerView;
    private BackpackAdapter adapter;

    private BackpackViewModel viewModel;

    public BackpackFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_backpack, container, false);

        charId = ((MainActivity) requireActivity()).characterId;

        floatingActionButtonAddItem = rootView.findViewById(R.id.floatingActionButton_add);
        floatingActionButtonAddItem.setOnClickListener(new FABAddOnClick());

        floatingActionButtonDelete = rootView.findViewById(R.id.floatingActionButton_delete);
        floatingActionButtonDelete.setOnClickListener(new FABDeleteOnClick());
        floatingActionButtonDelete.hide();

        adapter = new BackpackAdapter();
        adapter.setOnItemLongClickListener(new ItemLongClickListener());
        adapter.setOnItemClickListener(new ItemClickListener());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView = rootView.findViewById(R.id.recyclerView_BackpackItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new FABOnScrollListener());
        recyclerView.setAdapter(adapter);


        viewModel = new ViewModelProvider(this,
                new BackpackViewModel.BackpackViewModelFactory(requireActivity().getApplication(), charId))
                .get(BackpackViewModel.class);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                viewModel.setItems(items);
                adapter.setItems(items);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        removeChecks();
    }

    public void removeChecks() {
        if (adapter.isShowingChecks()) {
            adapter.setShowChecks();
            floatingActionButtonDelete.hide();
        }
    }

    @Override
    public void onEditDialogPositiveClick(DialogFragment dialog, String name, String quantity, int id) {
        viewModel.updateItem(name, quantity, charId, id);
    }

    @Override
    public void onEditDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        viewModel.checkItemsToDelete(adapter);
        floatingActionButtonDelete.hide();
        floatingActionButtonAddItem.show();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    @Override
    public void onItemAddDialogPositiveClick(DialogFragment dialog, String name, String quantity) {
        viewModel.insert(new Item(name, quantity, charId));
    }

    @Override
    public void onItemAddDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    /**
     * Listener private classes
     */
    private class FABAddOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ItemAddDialog dialog = new ItemAddDialog();
            dialog.setTargetFragment(BackpackFragment.this, 0);
            dialog.show(getParentFragmentManager(), "ADD_ITEM_DIALOG");
        }
    }

    private class FABDeleteOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.setTargetFragment(BackpackFragment.this, 0);
            deleteDialog.show(getParentFragmentManager(), "DELETE_DIALOG");
        }
    }

    private class ItemLongClickListener implements BackpackAdapter.OnItemLongClickListener {


        @Override
        public boolean onItemLongClick(View view, int position) {
            Item item = viewModel.getItems().get(position);
            item.setChecked(true);
            adapter.setShowChecks();
            adapter.notifyItemChanged(position);

            handleFABHiding();

            return true;
        }

        private void handleFABHiding() {
            if (floatingActionButtonDelete.isShown()) {
                floatingActionButtonDelete.hide();
                floatingActionButtonAddItem.show();
            } else {
                floatingActionButtonDelete.show();
                floatingActionButtonAddItem.hide();
            }
        }
    }

    private class ItemClickListener implements BackpackAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View itemView, int position) {
            ItemEditDialog dialog = new ItemEditDialog();
            dialog.setOldItem(viewModel.getItems().get(position));
            dialog.setTargetFragment(BackpackFragment.this, 0);
            dialog.show(getParentFragmentManager(), "EDIT_ITEM_DIALOG");
        }
    }

    // This one takes care of FAB hiding
    private class FABOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (adapter.isShowingChecks()) {
                return;
            }

            if (dy > 0 && floatingActionButtonAddItem.getVisibility() == View.VISIBLE) {
                floatingActionButtonAddItem.hide();
            } else if (dy < 0 && floatingActionButtonAddItem.getVisibility() != View.VISIBLE) {
                floatingActionButtonAddItem.show();
            }

        }
    }
}
