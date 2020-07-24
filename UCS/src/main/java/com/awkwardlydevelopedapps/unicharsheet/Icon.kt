package com.awkwardlydevelopedapps.unicharsheet

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract

class Icon(val iconId: Int,
           val iconName: String) {

    companion object {
        fun populateIcons(): ArrayList<Icon> {
            val icons = ArrayList<Icon>()

            // TODO change to normal icons
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.CULTIST_ID, ImageContract.Character.CULTIST))
            icons.add(Icon(ImageContract.Character.VIKING_ID, ImageContract.Character.VIKING))
            icons.add(Icon(ImageContract.Character.WIZARD_ID, ImageContract.Character.WIZARD))
            icons.add(Icon(ImageContract.Character.VISORED_ID, ImageContract.Character.VISORED))
            icons.add(Icon(ImageContract.Character.KENAKU_ID, ImageContract.Character.KENAKU))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))
            icons.add(Icon(ImageContract.Character.COWLED_ID, ImageContract.Character.COWLED))

            return icons
        }
    }
}