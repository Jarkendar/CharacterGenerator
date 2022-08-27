package com.skrzypczak.charactergenerator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.databinding.FragmentCardSavesBinding

class CardSaveItemRecyclerViewAdapter(private val onUserInteract: OnUserInteract,
                                      private var list: List<CardModel>
) : RecyclerView.Adapter<CardSaveItemRecyclerViewAdapter.ViewHolder>() {

    interface OnUserInteract {
        fun onItemClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCardSavesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.idView.text = item.timeStamp.time.toString()
        holder.content.text = item.name
        holder.itemView.setOnClickListener {
            onUserInteract.onItemClick()
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<CardModel>) {
        val added = (list.size until newList.size)
        val changed =
            list.mapIndexedNotNull { index, task -> if (newList.size > index && task != newList[index]) index else null }
        val removed = (newList.size until list.size).reversed()

        added.forEach { notifyItemInserted(it) }
        changed.forEach { notifyItemChanged(it) }
        removed.forEach { notifyItemRemoved(it) }
        list = newList
    }

    inner class ViewHolder(binding: FragmentCardSavesBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val idView = binding.itemNumber
            val content = binding.content
    }
}