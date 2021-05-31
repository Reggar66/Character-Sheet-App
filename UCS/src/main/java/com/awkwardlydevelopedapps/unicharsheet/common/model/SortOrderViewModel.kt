package com.awkwardlydevelopedapps.unicharsheet.common.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort

abstract class SortOrderViewModel : ViewModel() {

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