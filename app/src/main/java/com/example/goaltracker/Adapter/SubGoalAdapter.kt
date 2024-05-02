package com.example.goaltracker.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.goaltracker.data.SubGoal
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.databinding.SubgoalBinding
import com.bumptech.glide.Glide


class SubGoalAdapter(
    private val goalList: List<SubGoal>,
    private val onItemClick: (SubGoal) -> Unit,


    ) : RecyclerView.Adapter<SubGoalAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: SubgoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: SubGoal) {
            binding.apply {
                textTitle.text = "Title: ${goal.title}"
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
        val binding = SubgoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goalList[position])
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}