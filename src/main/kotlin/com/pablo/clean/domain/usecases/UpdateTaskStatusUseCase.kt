package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository

class UpdateTaskStatusUseCase(private val repository: ITaskRepository) {
    suspend fun execute(id: String, newStatus: TaskStatus) {
        val updatedTask = repository.updateTask(id) { task ->
            val isValidTransition = when (task.status to newStatus) {
                TaskStatus.PENDING to TaskStatus.PROCESSING,
                TaskStatus.PROCESSING to TaskStatus.COMPLETED -> true
                else -> false
            }
            if (!isValidTransition) {
                throw IllegalStateException("Invalid task status transition: ${task.status} -> $newStatus")
            }

            task.copy(status = newStatus)
        }

        if (updatedTask == null) {
            throw NoSuchElementException("Task with id $id not found")
        }
    }
}
