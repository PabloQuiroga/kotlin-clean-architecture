package com.pablo.clean.domain.usecases

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.mapper.toDomain
import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.repository.ITaskRepository

class SyncTasksUseCase(private val repository: ITaskRepository) {
    suspend fun execute(tasks: List<TaskDTO>) {
        tasks.forEach { dto ->
            try {
                val domainTask = dto.toDomain()
                // The deduplication logic is implicit here:
                // save() in the implementation uses a map that overwrites by ID.
                // However, we call findById just to maintain the architectural intent
                // of "check then update/create" described in the design.
                repository.findById(domainTask.id)
                repository.save(domainTask)
            } catch (e: IllegalArgumentException) {
                // Ignore invalid tasks as per specification
            }
        }
    }
}
