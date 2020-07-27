package com.awkwardlydevelopedapps.unicharsheet.preset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import java.util.ArrayList

class PresetListAdapter(private val mPresetList: ArrayList<PresetList>) : RecyclerView.Adapter<PresetListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPresetName: TextView = itemView.findViewById(R.id.textView_presetList_name)
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