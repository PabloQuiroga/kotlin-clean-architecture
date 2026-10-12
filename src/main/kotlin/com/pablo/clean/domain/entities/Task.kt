package com.pablo.clean.domain.entities

enum class TaskPriority { HIGH, MEDIUM, LOW }
enum class TaskStatus { PENDING, PROCESSING, COMPLETED }

data class Task(
    val id: String,
    val description: String,
    val priority: TaskPriority,
    val status: TaskStatus
) {
    init {
        require(description.isNotBlank()) { "Task description cannot be empty" }

        if (status == TaskStatus.COMPLETED) {
            require(description.isNotBlank()) { "Completed tasks must have a description" }
        }
    }
}
