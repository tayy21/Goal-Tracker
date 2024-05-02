package com.example.goaltracker.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.goaltracker.data.Goal
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.databinding.SubBinding
import com.bumptech.glide.Glide


class SGoalAdapter(
    private val goalList: List<Goal>,
    private val onItemClick: (Goal) -> Unit,


    ) : RecyclerView.Adapter<SGoalAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: SubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goal) {
            binding.apply {
                textTitle.text = "Title: ${goal.title}"
                textCata.text = "Category: ${goal.category}"
                textDead.text = "Deadline: ${goal.deadline}"

                if (!goal.imageUrl.isNullOrEmpty()) {
                    Glide.with(itemView)
                        .load(goal.imageUrl)
                        .into(imageGoal)
                }
                viewButton.setOnClickListener {
                    onItemClick(goal)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goalList[position])
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}