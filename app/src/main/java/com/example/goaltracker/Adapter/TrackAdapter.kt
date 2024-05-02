package com.example.goaltracker.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goaltracker.databinding.TrackBinding
import com.example.goaltracker.data.Goal

class TrackAdapter(
    private val trackList: List<Goal>,
    private val onItemClick: (Goal) -> Unit
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: TrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Goal) {
            binding.apply {
                textTitle.text = "Title: ${track.title}"

                if (!track.imageUrl.isNullOrEmpty()) {
                    Glide.with(itemView)
                        .load(track.imageUrl)
                        .into(imageGoal)
                }

                viewButton.setOnClickListener {
                    onItemClick(track)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}