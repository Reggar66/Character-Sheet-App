package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.awkwardlydevelopedapps.unicharsheet.MainActivity
import com.awkwardlydevelopedapps.unicharsheet.R

class NotesFragment : Fragment() {

    private var characterId = 0
    private lateinit var characterName: String
    private lateinit var characterClass: String
    private lateinit var characterRace: String
    private var characterIconId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        characterId = (requireActivity() as MainActivity).characterId
        characterName = (requireActivity() as MainActivity).characterName
        characterClass = (requireActivity() as MainActivity).characterClass
        characterRace = (requireActivity() as MainActivity).characterRace
        characterIconId = (requireActivity() as MainActivity).characterIconId


        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout_notes_fragment_container, getNewNotesFragmentList())
        fragmentTransaction.commit()


        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    private fun getNewNotesFragmentList(): Fragment {
        return NotesFragmentList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setupToolbar(view: View) {
        val navController = Navigation.findNavController(view)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar_notesFragment)
        toolbar.inflateMenu(R.menu.toolbar_menu_character_list)

        val textViewName: TextView = view.findViewById(R.id.toolbar_textView_characterName)
        val textViewClass: TextView = view.findViewById(R.id.toolbar_textView_characterClass)
        val textViewRace: TextView = view.findViewById(R.id.toolbar_textView_characterRace)
        val imageView: ImageView = view.findViewById(R.id.toolbar_statsFragment_icon)

        imageView.setImageResource(characterIconId)
        textViewName.text = characterName
        textViewClass.text = characterClass
        textViewRace.text = characterRace

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(ToolbarOnMenuClickListener())

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(toolbar, navController)
    }

    /**
     * Inner CLasses
     */

    inner class ToolbarOnMenuClickListener : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            TODO("Not yet implemented")
        }

    }
}