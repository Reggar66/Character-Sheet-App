package com.awkwardlydevelopedapps.unicharsheet.models

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract

class Icon(val iconId: Int,
           val iconName: String,
           var selected: Boolean = false) {

    companion object {
        // TODO add more icons
        fun populateCharacterIcons(): ArrayList<Icon> {
            val icons = ArrayList<Icon>()

            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.CULTIST_ID, ImageContract.Character.CULTIST))
            icons.add(Icon(ImageContract.Character.VIKING_ID, ImageContract.Character.VIKING))
            icons.add(Icon(ImageContract.Character.WIZARD_ID, ImageContract.Character.WIZARD))
            icons.add(Icon(ImageContract.Character.VISORED_ID, ImageContract.Character.VISORED))
            icons.add(Icon(ImageContract.Character.KENAKU_ID, ImageContract.Character.KENAKU))
            icons.add(Icon(ImageContract.Character.ALIEN_ID, ImageContract.Character.ALIEN))
            icons.add(Icon(ImageContract.Character.BANDIT_ID, ImageContract.Character.BANDIT))
            icons.add(Icon(ImageContract.Character.DWARF_ID, ImageContract.Character.DWARF))
            icons.add(Icon(ImageContract.Character.HOOD_ID, ImageContract.Character.HOOD))
            icons.add(Icon(ImageContract.Character.MEDUSA_ID, ImageContract.Character.MEDUSA))
            icons.add(Icon(ImageContract.Character.ORC_ID, ImageContract.Character.ORC))
            icons.add(Icon(ImageContract.Character.OVERLORD_ID, ImageContract.Character.OVERLORD))

            return icons
        }

        fun populateSpellIcons(): ArrayList<Icon> {
            val icons = ArrayList<Icon>()
            icons.add(Icon(ImageContract.Spell.FIRE_ID, ImageContract.Spell.FIRE))
            icons.add(Icon(ImageContract.Spell.AIR_ID, ImageContract.Spell.AIR))
            icons.add(Icon(ImageContract.Spell.WATER_ID, ImageContract.Spell.WATER))
            icons.add(Icon(ImageContract.Spell.EARTH_ID, ImageContract.Spell.EARTH))
            icons.add(Icon(ImageContract.Spell.NATURE_ID, ImageContract.Spell.NATURE))
            icons.add(Icon(ImageContract.Spell.ESSENCE_ID, ImageContract.Spell.ESSENCE))
            icons.add(Icon(ImageContract.Spell.MIND_ID, ImageContract.Spell.MIND))

            return icons
        }
    }
}