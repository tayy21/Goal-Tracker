package com.example.goaltracker.data

data class Goal(
    val id: String? = null,
    val title: String,
    val description: String,
    val deadline: String, // You can use Date or LocalDateTime if you prefer
    val progress: Int = 0, // Progress in percentage
    val userId: String // Associated user ID
)

data class Task(
    val id: String? = null,
    val goalId: String, // ID of the associated goal
    val title: String,
    val description: String,
    val deadline: String, // You can use Date or LocalDateTime if you prefer
    val isCompleted: Boolean = false
)

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
)

data class Category(
    val id: String? = null,
    val name: String
)

data class ProgressLog(
    val id: String? = null,
    val goalId: String, // ID of the associated goal
    val progress: Int, // Progress made (percentage)
    val date: String // Date of progress log entry
)