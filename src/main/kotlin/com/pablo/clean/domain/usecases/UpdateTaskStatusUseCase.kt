package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository

class UpdateTaskStatusUseCase(private val repository: ITaskRepository) {
    fun execute(id: String, newStatus: TaskStatus) {
        val task = repository.findById(id)
            ?: throw NoSuchElementException("Task with id $id not found")

        val updatedTask = task.copy(status = newStatus)
        repository.save(updatedTask)
    }
}
