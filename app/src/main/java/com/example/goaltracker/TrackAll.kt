package com.example.goaltracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goaltracker.Adapter.TrackAdapter
import com.example.goaltracker.data.Goal
import com.example.goaltracker.databinding.ActivityTrackAllBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrackAll : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var binding: ActivityTrackAllBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        getTracks { trackList ->
            trackAdapter = TrackAdapter(trackList) { track ->
                val intent = Intent(this@TrackAll, TrackDetails::class.java)
                intent.putExtra("goalId", track.id)
                startActivity(intent)
                Toast.makeText(this@TrackAll, "Clicked on track: ${track.title}", Toast.LENGTH_SHORT).show()
            }

            binding.recyclerViewMenu.adapter = trackAdapter
            binding.recyclerViewMenu.layoutManager = LinearLayoutManager(this@TrackAll)

            binding.GoBack.setOnClickListener {
                val intent = Intent(this@TrackAll, HomePage::class.java)
                startActivity(intent)
            }
        }

    }

    private fun getTracks(callback: (List<Goal>) -> Unit) {
        val goalList = mutableListOf<Goal>()

        databaseReference.child("Goal").child(auth.currentUser?.uid ?: "").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (goalSnapshot in dataSnapshot.children) {
                    val key = goalSnapshot.key
                    val goal = goalSnapshot.getValue(Goal::class.java)
                    goal?.let {
                        it.id = key
                        goalList.add(it)
                    }
                }

                // Invoke the callback with the goalList
                callback(goalList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}