package com.awkwardlydevelopedapps.unicharsheet.abilities.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.abilities.model.Spell;
import com.awkwardlydevelopedapps.unicharsheet.abilities.utils.SpellDiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class SpellAdapter extends RecyclerView.Adapter<SpellAdapter.ViewHolder> {

    /**
     * Listeners
     */
    //LongClick
    //Define listener member
    private SpellAdapter.OnItemLongClickListener longClickListener;

    //define the listener interface
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemLongClickListener(SpellAdapter.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    //Click
    //Define listener member
    private SpellAdapter.OnItemClickListener listener;

    //define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(SpellAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * SpellAdapter
     */
    private Context context;
    private ArrayList<Spell> spells = new ArrayList<>();
    private boolean showChecks;

    public SpellAdapter() {

    }

    public void setSpells(List<Spell> spells) {
        final SpellDiffUtilCallback spellDiffUtilCallback = new SpellDiffUtilCallback(this.spells, spells);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(spellDiffUtilCallback);

        this.spells.clear();
        this.spells.addAll(spells);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public SpellAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_spell, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SpellAdapter.ViewHolder viewHolder, int position) {
        Spell spell = spells.get(position);
        viewHolder.mSpellName.setText(spell.getSpellName());
        viewHolder.mIcon.setImageResource(spell.getImageResourceId());

        viewHolder.bindCheckBox();
    }

    @Override
    public int getItemCount() {
        return spells.size();
    }

    public void setShowChecks() {
        if (!showChecks) {
            showChecks = true;
        } else {
            showChecks = false;
            for (int i = 0; i < spells.size(); i++) {
                if (spells.get(i).isChecked()) {
                    spells.get(i).setChecked(false);
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
        TextView mSpellName;
        ImageView mIcon;
        FrameLayout mFrameLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mSpellName = (TextView) itemView.findViewById(R.id.spell_name);
            mIcon = (ImageView) itemView.findViewById(R.id.spell_icon);
            mFrameLayout = (FrameLayout) itemView.findViewById(R.id.spell_item_name_bar);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Handle color change during checking item
            if (isShowingChecks()) {
                int position = getBindingAdapterPosition();
                if (spells.get(position).isChecked()) {
                    spells.get(position).setChecked(false);
                    mFrameLayout.setBackground(context.getDrawable(R.drawable.list_item_spell_background_drawable_ripple));
                } else {
                    spells.get(position).setChecked(true);
                    mFrameLayout.setBackground(context.getDrawable(R.drawable.list_item_spell_background_drawable_selected));
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
            if (spells.get(getBindingAdapterPosition()).isChecked()) {
                mFrameLayout.setBackground(context.getDrawable(R.drawable.list_item_spell_background_drawable_selected));
            } else {
                mFrameLayout.setBackground(context.getDrawable(R.drawable.list_item_spell_background_drawable_ripple));
            }
        }
    }
}
