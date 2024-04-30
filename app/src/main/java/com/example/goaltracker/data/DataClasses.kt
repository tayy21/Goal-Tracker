package com.example.goaltracker.data

data class Goal(
    val title: String = "",
    val description: String = "",
    val deadline: String = "",
    val category: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    var id: String? = null,
)

data class SubGoal(
    val userId: String? = null,
    val goalId: String, // ID of the associated goal
    val title: String,
    val description: String,
    val deadline: String, // You can use Date or LocalDateTime if you prefer
    val imageUrl: String = "",
)

data class User(
    val userId: String? = null,
    val email: String,
)

data class ProgressLog(
    val userId: String? = null,
    val goalId: String, // ID of the associated goal
    val progress: Int, // Progress made (percentage)
    val date: String // Date of progress log entry
)