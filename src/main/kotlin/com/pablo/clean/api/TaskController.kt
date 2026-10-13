package com.pablo.clean.api

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.dto.TaskPriorityDTO
import com.pablo.clean.data.dto.TaskStatusDTO
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.usecases.SyncTasksUseCase
import com.pablo.clean.domain.usecases.UpdateTaskStatusUseCase

class TaskController(
    private val syncTasksUseCase: SyncTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase
) {
    suspend fun syncTasks(tasks: List<TaskDTO>) {
        println("API: Starting task synchronization...")
        syncTasksUseCase.execute(tasks)
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
