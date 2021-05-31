package com.awkwardlydevelopedapps.unicharsheet.inventory.backpack.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort

class ItemSortStateViewModel : ViewModel() {

    val sortOrderLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        sortOrderLiveData.value = Sort.BY_NAME_ASC
    }

    fun changeSortOrder(sortOrder: Int) {
        sortOrderLiveData.value = sortOrder
    }

}