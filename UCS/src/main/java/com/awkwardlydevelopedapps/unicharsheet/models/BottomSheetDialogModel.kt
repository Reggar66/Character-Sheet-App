package com.awkwardlydevelopedapps.unicharsheet.models

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.awkwardlydevelopedapps.unicharsheet.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheetDialogModel : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Expands bottom dialog so when Keyboard is shown half of the dialog won't be hidden
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val sheetInternal: View = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        super.onViewCreated(view, savedInstanceState)
    }
}