package com.awkwardlydevelopedapps.unicharsheet.viewModels

import androidx.lifecycle.ViewModel

class DataHolderViewModel(
        var characterID: Int = 0,
        var characterName: String = "",
        var className: String = "",
        var raceName: String = "",
        var imageResourceID: Int = 0
) : ViewModel() {

}