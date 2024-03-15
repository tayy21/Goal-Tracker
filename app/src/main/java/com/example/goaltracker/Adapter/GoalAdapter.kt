package com.example.goaltracker.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.goaltracker.data.Goal
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.R
import androidx.vectordrawable.animated.R.drawable
import com.example.goaltracker.databinding.GoalBinding
import com.bumptech.glide.Glide
import android.content.Intent
import android.telecom.Call
import com.example.goaltracker.Details


class GoalAdapter(
    private val goalList: List<Goal>,
    private val onItemClick: (Goal) -> Unit,


) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: GoalBinding) : RecyclerView.ViewHolder(binding.root) {
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
                    val intent = Intent(itemView.context, Details::class.java)
                    itemView.context.startActivity(intent)
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