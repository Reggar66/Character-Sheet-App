package com.awkwardlydevelopedapps.unicharsheet.common.model

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.settings.PreferenceFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BottomSheetDialogModel : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
        setTheme()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Expands bottom dialog so when Keyboard is shown half of the dialog won't be hidden
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val sheetInternal: View =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(sheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setTheme() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val themeOption = sharedPreferences.getString(
            PreferenceFragment.KEY_APP_THEME,
            PreferenceFragment.THEME_OPTION_DEVICE
        )

        if (themeOption == PreferenceFragment.THEME_OPTION_POLAR_NIGHT)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStylePolarNight)
        else
            setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }
}