package com.awkwardlydevelopedapps.unicharsheet.characterList.adapters;

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
import com.awkwardlydevelopedapps.unicharsheet.characterList.utils.CharacterDiffUtilCallback;
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ViewHolder> {

    /**
     * Listeners
     */
    //LongClick
    //Define listener member
    private OnItemLongClickListener longClickListener;

    //define the listener interface
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    //Click
    //Define listener member
    private OnItemClickListener listener;

    //define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    //define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * MainAdapter implementations
     */
    private Context context;
    private final ArrayList<Character> characters;
    private boolean showChecks;

    public CharacterListAdapter() {
        characters = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_character, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Character character = characters.get(position);
        viewHolder.mIcon.setImageResource(character.getImageResourceId());
        viewHolder.mCharName.setText(character.getCharacterName());
        viewHolder.mClassName.setText(character.getClassName());
        viewHolder.mRaceName.setText(character.getRaceName());

        viewHolder.bindCheckBox();
    }

    public void setCharacters(List<Character> characters) {
        final CharacterDiffUtilCallback characterDiffUtilCallback = new CharacterDiffUtilCallback(this.characters, characters);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(characterDiffUtilCallback);

        this.characters.clear();
        this.characters.addAll(characters);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void setShowChecks() {
        if (!showChecks) {
            showChecks = true;
        } else {
            showChecks = false;
            for (int i = 0; i < characters.size(); i++) {
                if (characters.get(i).isChecked()) {
                    characters.get(i).setChecked(false);
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
        ImageView mIcon;
        TextView mCharName;
        TextView mClassName;
        TextView mRaceName;
        FrameLayout mFrameLayout;
        ConstraintLayout mConstraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.image);
            mCharName = itemView.findViewById(R.id.character_text_view);
            mClassName = itemView.findViewById(R.id.class_text_view);
            mRaceName = itemView.findViewById(R.id.race_text_view);
            mFrameLayout = itemView.findViewById(R.id.frameLayout3);
            mConstraintLayout = itemView.findViewById(R.id.list_item_character_rootView);

            mFrameLayout.setOnLongClickListener(this);
            mFrameLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (isShowingChecks()) {
                int position = getBindingAdapterPosition();
                if (characters.get(position).isChecked()) {
                    characters.get(position).setChecked(false);
                    mFrameLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_spell_background_drawable_ripple));
                } else {
                    characters.get(position).setChecked(true);
                    mFrameLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable_selected));
                }

            } else {
                // Works only if Checks are not shown. Other way - listener applied via
                // interface wont work.
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

        //To prevent "remembering" the state by adapter
        public void bindCheckBox() {
            if (characters.get(getBindingAdapterPosition()).isChecked()) {
                mFrameLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_stat_drawable_selected));
            } else {
                mFrameLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.list_item_spell_background_drawable_ripple));
            }
        }
    }
}
