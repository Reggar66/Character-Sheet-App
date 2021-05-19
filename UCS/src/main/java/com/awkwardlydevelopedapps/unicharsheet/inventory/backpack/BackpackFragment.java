package com.awkwardlydevelopedapps.unicharsheet.inventory.backpack;

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

import com.awkwardlydevelopedapps.unicharsheet.common.PopupOnSortClickListener;
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort;
import com.awkwardlydevelopedapps.unicharsheet.common.DeleteDialog;
import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.inventory.InventoryFragment;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.adapters.BackpackAdapter;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.model.Item;
import com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.viewModel.BackpackViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BackpackFragment extends Fragment
        implements DeleteDialog.NoticeDialogListener,
        PopupOnSortClickListener,
        ItemBottomSheetDialog.NoticeDialogListener {

    private View rootView;
    private int characterID;
    private FloatingActionButton floatingActionButtonAddItem;
    private FloatingActionButton floatingActionButtonDelete;

    private RecyclerView recyclerView;
    private BackpackAdapter adapter;

    private BackpackViewModel viewModel;

    private InventoryFragment parentInventoryFragment;

    public BackpackFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_backpack, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterID();

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
                new BackpackViewModel.BackpackViewModelFactory(requireActivity().getApplication(), characterID))
                .get(BackpackViewModel.class);

        parentInventoryFragment.setPopupOnSortClickListener(this);
        
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //((InventoryFragment) getTargetFragment()).setPopupOnSortClickListener(this);

        viewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                viewModel.setItemList(items);
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
    public void onPopupSortByNameAsc() {
        viewModel.orderBy(Sort.BY_NAME_ASC);
    }

    @Override
    public void onPopupSortByNameDesc() {
        viewModel.orderBy(Sort.BY_NAME_DESC);
    }

    @Override
    public void onPopupSortByValueAsc() {
        viewModel.orderBy(Sort.BY_VALUE_ASC);
    }

    @Override
    public void onPopupSortByValueDesc() {
        viewModel.orderBy(Sort.BY_VALUE_DESC);
    }

    //BottomDialog
    @Override
    public void onPositiveClickListener(int option, @NotNull String itemName, @NotNull String quantity, Item oldItem) {
        switch (option) {
            case ItemBottomSheetDialog.ADD:
                viewModel.insert(new Item(itemName, quantity, characterID));
                break;
            case ItemBottomSheetDialog.EDIT:
                viewModel.updateItem(itemName, quantity, characterID, oldItem.id);
                break;
        }
    }

    public void setParentInventoryFragment(InventoryFragment parentInventoryFragment) {
        this.parentInventoryFragment = parentInventoryFragment;
    }

    /**
     * Listener private classes
     */
    private class FABAddOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showBottomDialog();
        }

        private void showBottomDialog() {
            ItemBottomSheetDialog bottomSheetDialog =
                    new ItemBottomSheetDialog();
            bottomSheetDialog.setNoticeDialogListener(BackpackFragment.this);
            bottomSheetDialog.setOption(ItemBottomSheetDialog.ADD);
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_ADD_ITEM");
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
            Item item = viewModel.getItemList().get(position);
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
            showBottomDialog(position);
        }

        private void showBottomDialog(int position) {
            ItemBottomSheetDialog bottomSheetDialog =
                    new ItemBottomSheetDialog();
            bottomSheetDialog.setNoticeDialogListener(BackpackFragment.this);
            bottomSheetDialog.setOption(ItemBottomSheetDialog.EDIT);
            bottomSheetDialog.setOldItem(viewModel.getItemList().get(position));
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_ITEM_EDIT");
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
