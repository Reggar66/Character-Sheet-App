package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.adapters.IconsSpellAdapter
import com.awkwardlydevelopedapps.unicharsheet.data.ImageContract
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.models.Icon
import com.awkwardlydevelopedapps.unicharsheet.models.Spell
import com.awkwardlydevelopedapps.unicharsheet.viewModels.SpellsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpellCreateBottomSheetDialog(private val viewModel: SpellsViewModel,
                                   private val charId: Int) : BottomSheetDialogModel(),
        IconsSpellAdapter.ItemDataCallback {

    lateinit var editTextSpellName: EditText
    lateinit var icon: Icon

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_spells, container, false)

        editTextSpellName = rootView.findViewById(R.id.dialogSpells_name)
        editTextSpellName.requestFocus()
        val fab: FloatingActionButton = rootView.findViewById(R.id.fab_spellCreation_bottomDialog)
        fab.setOnClickListener {
            createSpell()
            dialog?.dismiss()
        }

        val recyclerView: RecyclerView = rootView.findViewById(R.id.dialogSpells_recyclerView)
        val icons: ArrayList<Icon> = Icon.populateSpellIcons()
        icons[0].selected = true
        icon = icons[0]
        val iconsSpellAdapter = IconsSpellAdapter(icons)
        iconsSpellAdapter.setItemCallback(this)
        recyclerView.adapter = iconsSpellAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)

        return rootView
    }

    private fun createSpell() {
        viewModel.insert(Spell(editTextSpellName.text.toString(),
                "",
                "",
                "",
                "",
                "",
                "",
                icon.iconName,
                charId))
    }

    override fun getIcon(icon: Icon) {
        this.icon = icon
    }
}