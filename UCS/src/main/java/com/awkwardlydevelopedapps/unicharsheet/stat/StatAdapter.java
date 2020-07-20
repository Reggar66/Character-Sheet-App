package com.awkwardlydevelopedapps.unicharsheet.stat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;

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
        void IncAndDecStatValue(Stat stat, int value);

        void updateStatValue(int position);

        void playAnim();
    }

    /**
     * StatAdapter implementations
     */
    private Context context;
    private ArrayList<Stat> stats;
    private boolean showChecks;


    private StatUpdateListener statUpdateListener;
    private boolean isInit = false;

    public StatAdapter(Context context, StatUpdateListener statUpdateListener) {
        this.context = context;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_stat, parent, false);

        return new ViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Stat stat = stats.get(position);
        viewHolder.mStatName.setText(stat.getName());
        viewHolder.mStatValue.setText(stat.getValue());

        viewHolder.bindCheckBox();
    }

    @Override
    public int getItemCount() {
        if (stats != null)
            return stats.size();
        else
            return 0;
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

        Context context;
        TextView mStatName;
        TextView mStatValue;
        ImageView mAddImage;
        ImageView mRemoveImage;
        FrameLayout mFrameLayout;
        ConstraintLayout mConstraintLayout;

        public ViewHolder(final Context context, @NonNull final View itemView) {
            super(itemView);

            this.context = context;

            mStatName = itemView.findViewById(R.id.item_name);
            mStatValue = itemView.findViewById(R.id.item_quantity);
            mAddImage = itemView.findViewById(R.id.addImage);
            mRemoveImage = itemView.findViewById(R.id.removeImage);
            mFrameLayout = itemView.findViewById(R.id.frameLayout2);
            mConstraintLayout = itemView.findViewById(R.id.linearLayout4);

            // increment stat value by one
            mAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowingChecks())
                        return;

                    Stat stat = stats.get(getAdapterPosition());
                    int temp = Integer.parseInt(stat.getValue());
                    temp = temp + 1;
                    statUpdateListener.IncAndDecStatValue(stat, temp);
                }
            });

            // decrement stat value by one
            mRemoveImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowingChecks())
                        return;

                    Stat stat = stats.get(getAdapterPosition());
                    int temp = Integer.parseInt(stat.getValue());
                    temp = temp - 1;
                    statUpdateListener.IncAndDecStatValue(stat, temp);
                }
            });

            //updates stat value by click on number
            mStatValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowingChecks())
                        return;

                    statUpdateListener.updateStatValue(getAdapterPosition());
                }
            });

            mFrameLayout.setOnLongClickListener(this);
            mFrameLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isShowingChecks()) {
                int position = getAdapterPosition();
                if (stats.get(position).isChecked()) {
                    stats.get(position).setChecked(false);
                    mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable));
                } else {
                    stats.get(position).setChecked(true);
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
            if (stats.get(getAdapterPosition()).isChecked()) {
                mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable_selected));
            } else {
                mConstraintLayout.setBackground(context.getDrawable(R.drawable.list_item_stat_drawable));
            }
        }
    }
}
