package com.awkwardlydevelopedapps.unicharsheet.characterList.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.Icon
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CharacterIconsAdapter(
    private val mIcons: List<Icon>,
    private val imageToSet: ImageView,
    private val bottomSheetBehavior: BottomSheetBehavior<View>
) : RecyclerView.Adapter<CharacterIconsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val iconImage = itemView.findViewById<ImageView>(R.id.bottomSheetIconItem_icon)

        init {
            iconImage.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            imageToSet.setImageResource(mIcons[position].iconId)
            imageToSet.contentDescription = mIcons[position].iconName
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val rootIconView = inflater.inflate(R.layout.bottom_sheet_icon_item, parent, false)
        return ViewHolder(rootIconView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon: Icon = mIcons[position]
        val iconImage = holder.iconImage
        iconImage.setImageResource(icon.iconId)
    }

    override fun getItemCount(): Int {
        return mIcons.size
    }
}