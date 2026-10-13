package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository

class UpdateTaskStatusUseCase(private val repository: ITaskRepository) {
    suspend fun execute(id: String, newStatus: TaskStatus) {
        val task = repository.findById(id)
            ?: throw NoSuchElementException("Task with id $id not found")

        val isValidTransition = when (task.status to newStatus) {
            TaskStatus.PENDING to TaskStatus.PROCESSING,
            TaskStatus.PROCESSING to TaskStatus.COMPLETED -> true
            else -> false
        }
        if (!isValidTransition) {
            throw IllegalStateException("Invalid task status transition: ${task.status} -> $newStatus")
        }

        val updatedTask = task.copy(status = newStatus)
        repository.save(updatedTask)
    }
}
