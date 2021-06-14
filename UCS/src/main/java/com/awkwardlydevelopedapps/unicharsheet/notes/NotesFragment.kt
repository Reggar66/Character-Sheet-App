package com.awkwardlydevelopedapps.unicharsheet.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.data.Sort
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel
import com.awkwardlydevelopedapps.unicharsheet.notes.viewModel.NoteSortStateViewModel

class NotesFragment : Fragment(),
    NotesFragmentList.ChangeFragmentCallback,
    NotesFragmentDisplay.ChangeFragmentCallback {

    private var characterID = 0
    private lateinit var characterName: String
    private lateinit var characterClass: String
    private lateinit var characterRace: String
    private var characterIconID = 0

    private val dataHolderViewModel: DataHolderViewModel by activityViewModels()
    private val noteSortStateViewModel: NoteSortStateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        characterID = dataHolderViewModel.characterID
        characterName = dataHolderViewModel.characterName
        characterClass = dataHolderViewModel.className
        characterRace = dataHolderViewModel.raceName
        characterIconID = dataHolderViewModel.imageResourceID

        if (savedInstanceState == null) {
            childFragmentManager.commit {
                replace<NotesFragmentList>(R.id.frameLayout_notes_fragment_container)
            }
        }

        return inflater.inflate(R.layout.fragment_notes, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
    }

    private fun setupToolbar(view: View) {
        val navController = Navigation.findNavController(view)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar_notesFragment)
        toolbar.inflateMenu(R.menu.toolbar_menu_character_list)

        val textViewName: TextView = view.findViewById(R.id.toolbar_textView_characterName)
        val textViewClass: TextView = view.findViewById(R.id.toolbar_textView_characterClass)
        val textViewRace: TextView = view.findViewById(R.id.toolbar_textView_characterRace)
        val imageView: ImageView = view.findViewById(R.id.toolbar_statsFragment_icon)

        imageView.setImageResource(characterIconID)
        textViewName.text = characterName
        textViewClass.text = characterClass
        textViewRace.text = characterRace

        // Handling clicks on menu item
        toolbar.setOnMenuItemClickListener(ToolbarOnMenuClickListener())

        // Setup with Navigation Controller
        NavigationUI.setupWithNavController(toolbar, navController)
    }

    override fun changeToDisplayNote(noteId: Int) {
        childFragmentManager.commit {
            replace<NotesFragmentDisplay>(
                R.id.frameLayout_notes_fragment_container,
                args = Bundle().apply {
                    putInt(NotesFragmentDisplay.KEY_NOTE_ID, noteId)
                })
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }


    override fun changeToList() {
        childFragmentManager.popBackStack()
    }

    /**
     * Inner CLasses
     */

    inner class ToolbarOnMenuClickListener : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {

            return when (item?.itemId) {
                R.id.action_about -> {
                    NavHostFragment
                        .findNavController(this@NotesFragment)
                        .navigate(NotesFragmentDirections.actionNotesFragmentToAboutFragment())
                    true
                }
                R.id.action_settings -> {
                    NavHostFragment
                        .findNavController(this@NotesFragment)
                        .navigate(NotesFragmentDirections.actionNotesFragmentToSettingsFragment())
                    true
                }
                R.id.action_sort_nameAsc -> {
                    noteSortStateViewModel.changeSortOrder(Sort.BY_NAME_ASC)
                    true
                }
                R.id.action_sort_nameDesc -> {
                    noteSortStateViewModel.changeSortOrder(Sort.BY_NAME_DESC)
                    true
                }
                else -> false
            }
        }

    }
}