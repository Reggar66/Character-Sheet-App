package com.awkwardlydevelopedapps.unicharsheet.models

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.awkwardlydevelopedapps.unicharsheet.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheetDialogModel : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }
}