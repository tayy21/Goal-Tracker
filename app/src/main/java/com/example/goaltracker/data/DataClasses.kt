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

data class User(
    val userId: String? = null,
    val email: String,
)

data class ProgressLog(
    val progress: Int,
    val date: String,
    var id: String? = null
) {
    constructor() : this(0, "")
}