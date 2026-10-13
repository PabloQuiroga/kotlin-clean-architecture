package com.pablo.clean.api

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.mapper.toDomain
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.usecases.SyncTasksUseCase
import com.pablo.clean.domain.usecases.UpdateTaskStatusUseCase

class TaskController(
    private val syncTasksUseCase: SyncTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase
) {
    suspend fun syncTasks(tasks: List<TaskDTO>) {
        println("API: Starting task synchronization...")
        val domainTasks = tasks.mapNotNull { dto ->
            try {
                dto.toDomain()
            } catch (_: IllegalArgumentException) {
                null
            }
        }
        syncTasksUseCase.execute(domainTasks)
        println("API: Synchronization completed.")
    }

    suspend fun updateStatus(id: String, status: TaskStatus) {
        println("API: Updating status for task $id to $status...")
        try {
            updateTaskStatusUseCase.execute(id, status)
            println("API: Status updated successfully.")
        } catch (e: Exception) {
            println("API Error: ${e.message}")
        }
    }
}
