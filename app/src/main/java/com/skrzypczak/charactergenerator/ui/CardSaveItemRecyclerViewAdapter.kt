package com.skrzypczak.charactergenerator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skrzypczak.charactergenerator.CardSaver
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.databinding.CardSavesItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CardSaveItemRecyclerViewAdapter(private val onUserInteract: OnUserInteract,
                                      private var list: List<CardModel>,
                                      private val cardSaver: CardSaver
) : RecyclerView.Adapter<CardSaveItemRecyclerViewAdapter.ViewHolder>() {

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnUserInteract {
        fun onItemClick(cardModel: CardModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardSavesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.race.text = item.race
        holder.date.text = item.timeStamp.toString()
        holder.strength.text = item.attribution.strength.toString()
        holder.wisdom.text = item.attribution.wisdom.toString()
        holder.agility.text = item.attribution.agility.toString()
        holder.spirit.text = item.attribution.spirit.toString()
        holder.wit.text = item.attribution.wit.toString()
        holder.inspiration.text = item.attribution.inspirationLimit.toString()
        holder.fear.text = item.attribution.fearLimit.toString()
        holder.damage.text = item.attribution.damageLimit.toString()

        holder.itemView.setOnClickListener {
            onUserInteract.onItemClick(item)
            notifyItemChanged(selectedPosition)
            notifyItemChanged(position)
            selectedPosition = position
        }

        ioScope.launch {
            val thumbnail = cardSaver.getDrawableFromUri(item.imageUri)
            mainScope.launch {
                holder.thumbnail.setImageDrawable(thumbnail)
            }
        }

        holder.itemView.setBackgroundColor(if (selectedPosition == position) Color.GREEN else Color.TRANSPARENT)//todo to change base color
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<CardModel>) {
        val added = (list.size until newList.size)
        val changed = list
            .mapIndexedNotNull { index, task -> if (newList.size > index && task != newList[index]) index else null }
        val removed = (newList.size until list.size).reversed()

        added.forEach { notifyItemInserted(it) }
        changed.forEach { notifyItemChanged(it) }
        removed.forEach { notifyItemRemoved(it) }
        list = newList
    }

    inner class ViewHolder(binding: CardSavesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val name = binding.name
            val race = binding.race
            val date = binding.date
            val strength = binding.strengthLabel
            val wisdom = binding.wisdomLabel
            val agility = binding.agilityLabel
            val spirit = binding.spiritLabel
            val wit = binding.witLabel
            val inspiration = binding.inspirationLabel
            val fear = binding.fearLabel
            val damage = binding.damageLabel
            val thumbnail = binding.cardImage
    }
}