package com.awkwardlydevelopedapps.unicharsheet.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.ArrayList

class PresetListAdapter(private val mPresetList: ArrayList<PresetList>,
                        private val textViewPresetToSet: TextView,
                        private val bottomSheetBehavior: BottomSheetBehavior<View>) : RecyclerView.Adapter<PresetListAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewPresetName: TextView = itemView.findViewById(R.id.textView_presetList_name)
        val frameLayout: FrameLayout = itemView.findViewById(R.id.frameLayout_presetListName_container)

        init {
            frameLayout.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            textViewPresetToSet.text = textViewPresetName.text
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val rootView = inflater.inflate(R.layout.bottom_sheet_preset_list_item,
                parent,
                false)

        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return mPresetList.size
    }

    override fun onBindViewHolder(holder: PresetListAdapter.ViewHolder, position: Int) {
        val preset: PresetList = mPresetList[position]
        val presetName = holder.textViewPresetName
        presetName.text = preset.name
    }
}