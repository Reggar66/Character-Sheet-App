package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.awkwardlydevelopedapps.unicharsheet.models.Stat;
import com.awkwardlydevelopedapps.unicharsheet.adapters.StatAdapter;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.StatAddBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.StatDialog;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.StatsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class StatsPageFragment extends Fragment
        implements StatAdapter.StatUpdateListener,
        StatDialog.NoticeDialogListener,
        DeleteDialog.NoticeDialogListener {

    private View rootView;
    private FloatingActionButton floatingActionButtonAdd;
    private FloatingActionButton floatingActionButtonDelete;
    private StatsViewModel viewModel;

    private int charId;
    private int pageNumber;
    private int position;
    private List<Stat> statList;

    //RecyclerView
    private RecyclerView recyclerView;
    private StatAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stats_page, container, false);
        charId = ((MainActivity) requireActivity()).characterId;

        floatingActionButtonAdd = rootView.findViewById(R.id.add_button);
        floatingActionButtonAdd.setImageResource(R.drawable.ic_add_black_24);
        floatingActionButtonAdd.setOnClickListener(new FABAddOnClickListener());

        floatingActionButtonDelete = rootView.findViewById(R.id.floatingActionButton_deleteStats);
        floatingActionButtonDelete.setOnClickListener(new FABDeleteOnClickListener());
        floatingActionButtonDelete.hide();

        adapter = new StatAdapter(getContext(), this);
        adapter.setOnItemLongClickListener(new ItemLongClickListener());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = rootView.findViewById(R.id.stat_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new FABOnScrollListener()); //handling hide of FAB
        recyclerView.setAdapter(adapter);

        //load stats
        viewModel = new ViewModelProvider(this,
                new StatsViewModel.CAViewModelFactory(requireActivity().getApplication(), charId))
                .get(StatsViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllStatsOfPage(charId, pageNumber).observe(getViewLifecycleOwner(), new Observer<List<Stat>>() {
            @Override
            public void onChanged(List<Stat> stats) {
                // UI update
                adapter.setStats(stats);
                statList = stats;
            }
        });
    }

    public void clearChecks() {
        if (adapter.isShowingChecks()) {
            adapter.setShowChecks();
            floatingActionButtonDelete.hide();
            floatingActionButtonAdd.show();
        }
    }

    public void setPage(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void IncAndDecStatValue(Stat stat, int value) {
        viewModel.updateStatValue(stat.getName(),
                String.valueOf(value),
                stat.getCharId(),
                stat.id);
    }

    @Override
    public void updateStatValue(int position) {
        this.position = position;
        StatDialog statDialog = new StatDialog();
        statDialog.setTargetFragment(this, 0);
        statDialog.show(getParentFragmentManager(), "stat edit dialog");
    }

    @Override
    public void playAnim() {
        recyclerView.scheduleLayoutAnimation();
    }

    // StatDialog - to edit values by clicking on number
    @Override
    public void onStatDialogPositiveClick(DialogFragment dialog, String value) {
        Stat stat = statList.get(position);
        viewModel.updateStatValue(stat.getName(), value, stat.getCharId(), stat.id);
    }

    @Override
    public void onStatDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    // DeleteDialog
    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        viewModel.checkStatsAndDelete(adapter, statList);
        floatingActionButtonDelete.hide();
        floatingActionButtonAdd.show();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Objects.requireNonNull(dialog.getDialog()).cancel();
    }

    /**
     * Inner classes
     */
    // This one takes care of FAB hiding
    private class FABOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (adapter.isShowingChecks()) {
                return;
            }

            if (dy > 0 && floatingActionButtonAdd.getVisibility() == View.VISIBLE) {
                floatingActionButtonAdd.hide();
            } else if (dy < 0 && floatingActionButtonAdd.getVisibility() != View.VISIBLE) {
                floatingActionButtonAdd.show();
            }

        }
    }

    // Takes care of creating StatAddDialog
    private class FABAddOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showAddStatBottomSheet();
        }

        private void showAddStatBottomSheet() {
            StatAddBottomSheetDialog bottomSheetDialog =
                    new StatAddBottomSheetDialog(viewModel, charId, pageNumber);
            bottomSheetDialog.setTitle("Stat creation");
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_ADD_STAT");
        }
    }

    private class FABDeleteOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.setTargetFragment(StatsPageFragment.this, 0);
            deleteDialog.show(getParentFragmentManager(), "DELETE_STATS_DIALOG");
        }
    }

    private class ItemLongClickListener implements StatAdapter.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(View view, int position) {
            Stat stat = statList.get(position);
            stat.setChecked(true);
            adapter.setShowChecks();
            adapter.notifyItemChanged(position);

            handleFABHiding();

            return true;
        }

        private void handleFABHiding() {
            if (floatingActionButtonDelete.isShown()) {
                floatingActionButtonDelete.hide();
                floatingActionButtonAdd.show();
            } else {
                floatingActionButtonDelete.show();
                floatingActionButtonAdd.hide();
            }
        }
    }
}
