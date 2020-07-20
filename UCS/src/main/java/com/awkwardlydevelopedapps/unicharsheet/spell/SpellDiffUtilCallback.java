package com.awkwardlydevelopedapps.unicharsheet.spell;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class SpellDiffUtilCallback extends DiffUtil.Callback {

    private List<Spell> oldSpells;
    private List<Spell> newSpells;

    public SpellDiffUtilCallback(List<Spell> oldSpells, List<Spell> newSpells) {
        this.oldSpells = oldSpells;
        this.newSpells = newSpells;
    }

    @Override
    public int getOldListSize() {
        return oldSpells.size();
    }

    @Override
    public int getNewListSize() {
        return newSpells.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldSpells.get(oldItemPosition).id == newSpells.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Spell oldSpell = oldSpells.get(oldItemPosition);
        final Spell newSpell = newSpells.get(newItemPosition);

        return oldSpell.getSpellName().equals(newSpell.getSpellName())
                && oldSpell.getCost().equals(newSpell.getCost())
                && oldSpell.getAddCost().equals(newSpell.getAddCost())
                && oldSpell.getDmg().equals(newSpell.getDmg())
                && oldSpell.getAddEffect().equals(newSpell.getAddCost())
                && oldSpell.getDescription().equals(newSpell.getDescription())
                && oldSpell.getSpecialNotes().equals(newSpell.getSpecialNotes());
    }
}
