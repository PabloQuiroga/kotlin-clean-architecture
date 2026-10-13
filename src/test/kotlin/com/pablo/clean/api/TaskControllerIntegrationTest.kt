package com.pablo.clean.api

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.dto.TaskPriorityDTO
import com.pablo.clean.data.dto.TaskStatusDTO
import com.pablo.clean.data.repository.TaskRepositoryImpl
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.usecases.SyncTasksUseCase
import com.pablo.clean.domain.usecases.UpdateTaskStatusUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TaskControllerIntegrationTest {

    @Test
    fun `should sync valid tasks ignore invalid tasks and update a task status`() = runTest {
        val repository = TaskRepositoryImpl()
        val controller = TaskController(
            SyncTasksUseCase(repository),
            UpdateTaskStatusUseCase(repository)
        )

        controller.syncTasks(
            listOf(
                TaskDTO("1", "First task", TaskPriorityDTO.HIGH, TaskStatusDTO.PENDING),
                TaskDTO("2", "", TaskPriorityDTO.MEDIUM, TaskStatusDTO.PENDING),
                TaskDTO("3", "Third task", TaskPriorityDTO.LOW, TaskStatusDTO.PENDING)
            )
        )

        val syncedTasks = repository.findAll()
        assertEquals(setOf("1", "3"), syncedTasks.map { it.id }.toSet())

        controller.updateStatus("1", TaskStatus.PROCESSING)

        assertEquals(TaskStatus.PROCESSING, repository.findById("1")?.status)
    }
}
