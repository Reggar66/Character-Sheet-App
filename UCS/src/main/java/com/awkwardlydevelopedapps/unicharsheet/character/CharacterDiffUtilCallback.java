package com.awkwardlydevelopedapps.unicharsheet.character;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CharacterDiffUtilCallback extends DiffUtil.Callback {

    private final List<Character> oldCharacters;
    private final List<Character> newCharacters;

    public CharacterDiffUtilCallback(List<Character> oldCharacters, List<Character> newCharacters) {
        this.oldCharacters = oldCharacters;
        this.newCharacters = newCharacters;
    }

    @Override
    public int getOldListSize() {
        return oldCharacters.size();
    }

    @Override
    public int getNewListSize() {
        return newCharacters.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCharacters.get(oldItemPosition).id == newCharacters.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Character oldCharacter = oldCharacters.get(oldItemPosition);
        final Character newCharacter = newCharacters.get(newItemPosition);
        return oldCharacter.getCharacterName().equals(newCharacter.getCharacterName()) &&
                oldCharacter.getClassName().equals(newCharacter.getClassName()) &&
                oldCharacter.getRaceName().equals(newCharacter.getRaceName());
    }
}
