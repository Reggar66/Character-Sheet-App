package com.awkwardlydevelopedapps.unicharsheet.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.Icon

class IconsSpellAdapter(private val mIcons: List<Icon>) : RecyclerView.Adapter<IconsSpellAdapter.ViewHolder>() {

    interface ItemDataCallback {
        fun setIconForDialog(icon: Icon)
    }

    internal lateinit var callback: ItemDataCallback

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val iconImage = itemView.findViewById<ImageView>(R.id.bottomSheetSpellIconItem_icon)
        val frame = itemView.findViewById<FrameLayout>(R.id.bottomSheetSpellIconItem_frame)

        init {
            frame.setOnClickListener(this)
        }

        fun chooseBinding() {
            val icon: Icon = mIcons[adapterPosition]
            if (icon.selected) {
                frame.backgroundTintList = ColorStateList
                        .valueOf(ContextCompat.getColor(itemView.context, R.color.colorAccent))
            } else {
                frame.backgroundTintList = ColorStateList
                        .valueOf(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark))
            }
        }

        override fun onClick(v: View?) {
            for ((index, icon) in mIcons.withIndex()) {
                if (icon.selected) {
                    icon.selected = false
                    notifyItemChanged(index)
                }
            }

            val icon: Icon = mIcons[adapterPosition]
            icon.selected = true
            notifyItemChanged(adapterPosition)
            callback.setIconForDialog(icon)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val rootIconView = inflater.inflate(R.layout.bottom_sheet_spell_icon_item, parent, false)
        return ViewHolder(rootIconView)
    }

    override fun getItemCount(): Int {
        return mIcons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon: Icon = mIcons[position]
        val iconImage = holder.iconImage
        iconImage.setImageResource(icon.iconId)

        holder.chooseBinding()
    }

    fun setItemCallback(callback: ItemDataCallback) {
        this.callback = callback
    }
}
