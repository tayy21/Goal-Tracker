package com.example.goaltracker.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.goaltracker.data.Goal
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.databinding.GoalBinding


class GoalAdapter(
    private val goalList: List<Goal>,
    private val onItemClick: (Goal) -> Unit
) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: GoalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goal) {
            binding.apply {
                textTitle.text = "Title: ${goal.title}"
                textCata.text = "Category: ${goal.category}"
                textDead.text = "Deadline: ${goal.deadline}"
                viewButton.setOnClickListener {
                    onItemClick(goal)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goalList[position])
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}