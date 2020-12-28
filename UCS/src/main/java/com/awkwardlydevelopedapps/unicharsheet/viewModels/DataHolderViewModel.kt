package com.awkwardlydevelopedapps.unicharsheet.viewModels

import androidx.lifecycle.ViewModel

class DataHolderViewModel(
        var characterId: Int = 0,
        var characterName: String = "",
        var className: String = "",
        var raceName: String = "",
        var imageResourceId: Int = 0
) : ViewModel() {

}