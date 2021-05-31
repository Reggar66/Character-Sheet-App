package com.awkwardlydevelopedapps.unicharsheet.stats.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort

class StatSortStateViewModel : ViewModel() {

    var currentPageIndex: Int = 1

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