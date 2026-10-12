package com.pablo.clean.data.dto

enum class TaskPriorityDTO { HIGH, MEDIUM, LOW }
enum class TaskStatusDTO { PENDING, PROCESSING, COMPLETED }

data class TaskDTO(
    val id: String,
    val description: String,
    val priority: TaskPriorityDTO,
    val status: TaskStatusDTO
)
