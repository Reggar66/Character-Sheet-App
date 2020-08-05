package com.awkwardlydevelopedapps.unicharsheet.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.awkwardlydevelopedapps.unicharsheet.models.Item;

import java.util.List;

public class BackpackItemDiffUtilCallback extends DiffUtil.Callback {

    private final List<Item> oldItems;
    private final List<Item> newItems;

    public BackpackItemDiffUtilCallback(List<Item> oldItems, List<Item> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).id == newItems.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Item oldItem = oldItems.get(oldItemPosition);
        final Item newItem = newItems.get(newItemPosition);

        return oldItem.getName().equals(newItem.getName()) && oldItem.getQuantity().equals(newItem.getQuantity());
    }
}
