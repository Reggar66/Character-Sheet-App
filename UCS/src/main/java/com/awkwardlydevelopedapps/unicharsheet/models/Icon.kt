package com.awkwardlydevelopedapps.unicharsheet.models

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract

class Icon(val iconId: Int,
           val iconName: String,
           var selected: Boolean = false) {

    companion object {
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
            icons.add(Icon(ImageContract.Character.BESTIAL_FANGS_ID, ImageContract.Character.BESTIAL_FANGS))
            icons.add(Icon(ImageContract.Character.BOAR_TUSKS_ID, ImageContract.Character.BOAR_TUSKS))
            icons.add(Icon(ImageContract.Character.BURNING_SKULL_ID, ImageContract.Character.BURNING_SKULL))
            icons.add(Icon(ImageContract.Character.HORNED_REPTILE_ID, ImageContract.Character.HORNED_REPTILE))

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
            icons.add(Icon(ImageContract.Spell.ACID_BLOB_ID, ImageContract.Spell.ACID_BLOB))
            icons.add(Icon(ImageContract.Spell.ANGULAR_SPIDER_ID, ImageContract.Spell.ANGULAR_SPIDER))
            icons.add(Icon(ImageContract.Spell.BLEEDING_EYE_ID, ImageContract.Spell.BLEEDING_EYE))
            icons.add(Icon(ImageContract.Spell.BROKEN_TABLET_ID, ImageContract.Spell.BROKEN_TABLET))
            icons.add(Icon(ImageContract.Spell.CADUCEUS_ID, ImageContract.Spell.CADUCEUS))
            icons.add(Icon(ImageContract.Spell.CLOUDY_FORK_ID, ImageContract.Spell.CLOUDY_FORK))
            icons.add(Icon(ImageContract.Spell.DEATH_JUICE_ID, ImageContract.Spell.DEATH_JUICE))
            icons.add(Icon(ImageContract.Spell.DRIPPING_KNIFE_ID, ImageContract.Spell.DRIPPING_KNIFE))
            icons.add(Icon(ImageContract.Spell.EVIL_BAT_ID, ImageContract.Spell.EVIL_BAT))
            icons.add(Icon(ImageContract.Spell.FANGS_ID, ImageContract.Spell.FANGS))
            icons.add(Icon(ImageContract.Spell.FLAMING_CLAW_ID, ImageContract.Spell.FLAMING_CLAW))
            icons.add(Icon(ImageContract.Spell.INCENSE_ID, ImageContract.Spell.INCENSE))
            icons.add(Icon(ImageContract.Spell.PAWPRINT_ID, ImageContract.Spell.PAWPRINT))
            icons.add(Icon(ImageContract.Spell.SPIRAL_ARROW_ID, ImageContract.Spell.SPIRAL_ARROW))
            icons.add(Icon(ImageContract.Spell.STEAM_ID, ImageContract.Spell.STEAM))
            icons.add(Icon(ImageContract.Spell.WOLF_HEAD_ID, ImageContract.Spell.WOLF_HEAD))

            return icons
        }
    }
}