package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.awkwardlydevelopedapps.unicharsheet.R

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(view)

        // adding preference fragment to container
        parentFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, PreferenceFragment())
                .commit()
    }

    private fun setUpToolbar(view: View) {
        val navController = Navigation.findNavController(view)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar_settings_fragment)
        toolbar.setTitle(R.string.settings)

        NavigationUI.setupWithNavController(toolbar, navController)
    }
}