package com.awkwardlydevelopedapps.unicharsheet.stat;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class StatDiffUtilCallback extends DiffUtil.Callback {

    private final List<Stat> oldStats;
    private final List<Stat> newStats;

    public StatDiffUtilCallback(List<Stat> oldStats, List<Stat> newStats) {
        this.oldStats = oldStats;
        this.newStats = newStats;
    }

    @Override
    public int getOldListSize() {
        return oldStats.size();
    }

    @Override
    public int getNewListSize() {
        return newStats.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldStats.get(oldItemPosition).id == newStats.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Stat oldStat = oldStats.get(oldItemPosition);
        final Stat newStat = newStats.get(newItemPosition);

        return oldStat.getName().equals(newStat.getName()) && oldStat.getValue().equals(newStat.getValue());
    }
}
