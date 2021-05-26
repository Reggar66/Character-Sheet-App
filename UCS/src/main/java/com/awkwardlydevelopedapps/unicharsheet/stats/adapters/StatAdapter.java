package com.awkwardlydevelopedapps.unicharsheet.stats.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.stats.utils.StatDiffUtilCallback;
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat;

import java.util.ArrayList;
import java.util.List;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.ViewHolder> {

    /**
     * Listeners
     */
    //LongClick
    //Define listener member
    private StatAdapter.OnItemLongClickListener longClickListener;

    //define the listener interface
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemLongClickListener(StatAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    //Click
    //Define listener member
    private StatAdapter.OnItemClickListener listener;

    //define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(StatAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface StatUpdateListener {
        void incAndDecStatValue(Stat stat, int value);

        void openStatEditDialog(int position);

        void playAnim();
    }

    /**
     * StatAdapter implementations
     */
    private Context context;
    private final ArrayList<Stat> stats;
    private boolean showChecks;


    private StatUpdateListener statUpdateListener;
    private boolean isInit = false;

    public StatAdapter(StatUpdateListener statUpdateListener) {
        this.statUpdateListener = statUpdateListener;
        this.stats = new ArrayList<>();
    }

    public void setStats(List<Stat> stats) {
        final StatDiffUtilCallback statDiffUtilCallback = new StatDiffUtilCallback(this.stats, stats);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(statDiffUtilCallback);

        this.stats.clear();
        this.stats.addAll(stats);
        diffResult.dispatchUpdatesTo(this);

        if (!isInit) {
            statUpdateListener.playAnim();
            isInit = true;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_stat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Stat stat = stats.get(position);
        viewHolder.mStatName.setText(stat.getName());

        String value = stat.getValue();
        if (value.isEmpty()) {
            value = "0";
        }
        viewHolder.mStatValue.setText(value);

        viewHolder.bindCheckBox();
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void setShowChecks() {
        if (!showChecks) {
            showChecks = true;
        } else {
            showChecks = false;
            for (int i = 0; i < stats.size(); i++) {
                if (stats.get(i).isChecked()) {
                    stats.get(i).setChecked(false);
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

        TextView mStatName;
        TextView mStatValue;
        ImageView mAddImage;
        ImageView mRemoveImage;
        FrameLayout mFrameLayout;
        ConstraintLayout mConstraintLayout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            mStatName = itemView.findViewById(R.id.item_name);
            mStatValue = itemView.findViewById(R.id.item_quantity);
            mAddImage = itemView.findViewById(R.id.addImage);
            mRemoveImage = itemView.findViewById(R.id.removeImage);
            mFrameLayout = itemView.findViewById(R.id.frameLayout2);
            mConstraintLayout = itemView.findViewById(R.id.linearLayout4);

            // increment stat value by one
            mAddImage.setOnClickListener(v -> {
                if (isShowingChecks())
                    return;

                Stat stat = stats.get(getBindingAdapterPosition());
                int temp = 0;
                if (!stat.getValue().isEmpty()) {
                    temp = Integer.parseInt(stat.getValue());
                }
                temp = temp + 1;
                statUpdateListener.incAndDecStatValue(stat, temp);
            });

            // decrement stat value by one
            mRemoveImage.setOnClickListener(v -> {
                if (isShowingChecks())
                    return;

                Stat stat = stats.get(getBindingAdapterPosition());
                int temp = 0;
                if (!stat.getValue().isEmpty()) {
                    temp = Integer.parseInt(stat.getValue());
                }
                temp = temp - 1;
                statUpdateListener.incAndDecStatValue(stat, temp);
            });

            //updates stat value by click on number
            mStatValue.setOnClickListener(v -> {
                if (isShowingChecks())
                    return;

                statUpdateListener.openStatEditDialog(getBindingAdapterPosition());
            });

            mFrameLayout.setOnLongClickListener(this);
            mFrameLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isShowingChecks()) {
                int position = getBindingAdapterPosition();
                if (stats.get(position).isChecked()) {
                    stats.get(position).setChecked(false);
                    mConstraintLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable));
                } else {
                    stats.get(position).setChecked(true);
                    mConstraintLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable_selected));
                }
            } else {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v, position);
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (longClickListener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(v, position);
                }
            }
            return true;
        }

        public void bindCheckBox() {
            if (stats.get(getBindingAdapterPosition()).isChecked()) {
                mConstraintLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable_selected));
            } else {
                mConstraintLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable));
            }
        }
    }
}
