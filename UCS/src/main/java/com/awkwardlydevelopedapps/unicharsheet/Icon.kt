package com.awkwardlydevelopedapps.unicharsheet

import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract

class Icon(val iconId: Int,
           val iconName: String) {

    companion object {
        fun populateIcons(): ArrayList<Icon> {
            val icons = ArrayList<Icon>()

            // TODO change to normal icons
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