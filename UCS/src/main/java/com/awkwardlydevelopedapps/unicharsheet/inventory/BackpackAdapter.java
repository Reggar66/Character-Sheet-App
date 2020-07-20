package com.awkwardlydevelopedapps.unicharsheet.inventory;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;

import java.util.ArrayList;
import java.util.List;

public class BackpackAdapter extends RecyclerView.Adapter<BackpackAdapter.ViewHolder> {

    /**
     * Listeners
     */
    //LongClick
    //Define listener member
    private BackpackAdapter.OnItemLongClickListener longClickListener;

    //define the listener interface
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemLongClickListener(BackpackAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    //Click
    //Define listener member
    private BackpackAdapter.OnItemClickListener listener;

    //define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(BackpackAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * BackpackAdapter implementations
     */
    private Context context;
    private ArrayList<Item> items;
    private boolean showChecks;

    public BackpackAdapter() {
        this.items = new ArrayList<>();
    }

    public void setItems(List<Item> items) {
        final BackpackItemDiffUtilCallback itemDiffUtilCallback = new BackpackItemDiffUtilCallback(this.items, items);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(itemDiffUtilCallback);

        this.items.clear();
        this.items.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_items, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        viewHolder.mItemName.setText(item.getName());
        viewHolder.mItemQuantity.setText(item.getQuantity());

        viewHolder.bindCheckBox();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setShowChecks() {
        if (!showChecks) {
            showChecks = true;
        } else {
            showChecks = false;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isChecked()) {
                    items.get(i).setChecked(false);
                    notifyItemChanged(i);
                }
            }
        }
    }

    public boolean isShowingChecks() {
        return showChecks;
    }


    /**
     * View Holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mItemName;
        TextView mItemQuantity;
        ConstraintLayout mConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemName = itemView.findViewById(R.id.item_name);
            mItemQuantity = itemView.findViewById(R.id.item_quantity);
            mConstraintLayout = itemView.findViewById(R.id.linearLayout4);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isShowingChecks()) {
                int position = getAdapterPosition();
                if (items.get(position).isChecked()) {
                    items.get(position).setChecked(false);
                    mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable));
                } else {
                    items.get(position).setChecked(true);
                    mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable_selected));
                }
            } else {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v, position);
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(v, position);
                }
            }
            return true;
        }

        public void bindCheckBox() {
            if (items.get(getAdapterPosition()).isChecked()) {
                mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable_selected));
            } else {
                mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable));
            }
        }

    }
}
