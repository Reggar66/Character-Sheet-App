package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.awkwardlydevelopedapps.unicharsheet.R

class NotesFragmentDisplay() : Fragment() {

    // TODO everything

    var changeFragmentCallback: ChangeFragmentCallback? = null

    interface ChangeFragmentCallback {
        fun changeToList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO correct layout
        return inflater.inflate(R.layout.fragment_spells_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }
}