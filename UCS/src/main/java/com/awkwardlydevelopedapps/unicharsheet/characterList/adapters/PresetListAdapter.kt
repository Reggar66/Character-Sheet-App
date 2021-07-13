package com.awkwardlydevelopedapps.unicharsheet.characterList.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.characterList.model.PresetList
import com.awkwardlydevelopedapps.unicharsheet.characterList.dao.PresetDao
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.ArrayList

class PresetListAdapter(
    private val mPresetList: ArrayList<PresetList>,
    private val textViewPresetToSet: TextView,
    private val bottomSheetBehavior: BottomSheetBehavior<View>,
    private val presetDao: PresetDao,
    private val activity: Activity
) : RecyclerView.Adapter<PresetListAdapter.ViewHolder>() {


    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun passPresetListItem(presetListItem: PresetList, presetIndex: Int)
    }

    companion object {
        private const val SEPARATOR_POSITION = 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {

        val textViewPresetName: TextView = itemView.findViewById(R.id.textView_presetList_name)
        val frameLayout: FrameLayout =
            itemView.findViewById(R.id.frameLayout_presetListName_container)

        val separatorView: View = itemView.findViewById(R.id.separator_presetList)

        init {
            frameLayout.setOnClickListener(this)
            frameLayout.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClickListener?.passPresetListItem(
                mPresetList[bindingAdapterPosition],
                bindingAdapterPosition
            )

            textViewPresetToSet.text = textViewPresetName.text
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        override fun onLongClick(p0: View?): Boolean {
            return if (textViewPresetName.text != "Blade" && textViewPresetName.text != "None") {
                deleteDialog()
                true
            } else
                false
        }

        private fun deleteDialog() {
            val builder: AlertDialog.Builder = activity.let {
                AlertDialog.Builder(it)
            }

            builder.setMessage("Would you like to delete \'" + textViewPresetName.text + "\' preset?")
                .setTitle("Delete preset")

            builder.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                deletePreset()
            })

            builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            val dialog: AlertDialog? = builder.create()
            dialog?.show()
        }

        private fun deletePreset() {
            val position = bindingAdapterPosition
            mPresetList.removeAt(position)
            notifyItemRemoved(position)

            // Delete preset from database
            ExecSingleton.getInstance().execute {
                presetDao.deletePresetListItem(textViewPresetName.text.toString())
                presetDao.deletePresetStats(textViewPresetName.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val rootView = inflater.inflate(
            R.layout.bottom_sheet_preset_list_item,
            parent,
            false
        )

        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return mPresetList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preset: PresetList = mPresetList[position]
        val presetName = holder.textViewPresetName
        presetName.text = preset.name

        // Showing separator just before custom presets
        if (position == SEPARATOR_POSITION)
            holder.separatorView.visibility = View.VISIBLE
        else
            holder.separatorView.visibility = View.GONE
    }
}