package com.awkwardlydevelopedapps.unicharsheet.common.viewModel

import androidx.lifecycle.ViewModel

class DataHolderViewModel(
        var characterID: Int = 0,
        var characterName: String = "",
        var className: String = "",
        var raceName: String = "",
        var imageResourceID: Int = 0,
        var selectedSpellID: Int = -1
) : ViewModel() {

    val SPELL_ID_NOT_SET = -1;

}