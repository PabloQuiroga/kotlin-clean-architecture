package com.pablo.clean.data.mapper

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.dto.TaskPriorityDTO
import com.pablo.clean.data.dto.TaskStatusDTO
import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus

fun TaskDTO.toDomain(): Task {
    return Task(
        id = this.id,
        description = this.description,
        priority = when (this.priority) {
            TaskPriorityDTO.HIGH -> TaskPriority.HIGH
            TaskPriorityDTO.MEDIUM -> TaskPriority.MEDIUM
            TaskPriorityDTO.LOW -> TaskPriority.LOW
        },
        status = when (this.status) {
            TaskStatusDTO.PENDING -> TaskStatus.PENDING
            TaskStatusDTO.PROCESSING -> TaskStatus.PROCESSING
            TaskStatusDTO.COMPLETED -> TaskStatus.COMPLETED
        }
    )
}
