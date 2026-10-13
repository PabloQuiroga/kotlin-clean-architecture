package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.repository.ITaskRepository

class SyncTasksUseCase(private val repository: ITaskRepository) {
    suspend fun execute(tasks: List<Task>) {
        tasks.sortedBy { task ->
            when (task.priority) {
                TaskPriority.HIGH -> 0
                TaskPriority.MEDIUM -> 1
                TaskPriority.LOW -> 2
            }
        }.forEach { task ->
            repository.save(task)
        }
    }
}
