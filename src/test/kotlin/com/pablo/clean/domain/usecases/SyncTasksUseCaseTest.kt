package com.pablo.clean.domain.usecases

import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.dto.TaskPriorityDTO
import com.pablo.clean.data.dto.TaskStatusDTO
import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.repository.ITaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.*

class SyncTasksUseCaseTest {

    private val repository: ITaskRepository = mock()
    private val useCase = SyncTasksUseCase(repository)

    @Test
    fun `should sync valid tasks and ignore invalid ones`() = runTest {
        val dtos = listOf(
            TaskDTO("1", "Valid Task", TaskPriorityDTO.HIGH, TaskStatusDTO.PENDING),
            TaskDTO("2", "", TaskPriorityDTO.LOW, TaskStatusDTO.PENDING) // Invalid: empty desc
        )

        useCase.execute(dtos)

        verify(repository, times(1)).save(any())
        verify(repository, times(0)).save(argThat { this.id == "2" })
    }

    @Test
    fun `should update task if id already exists (deduplication)`() = runTest {
        val existingTask = Task("1", "Old Description", com.pablo.clean.domain.entities.TaskPriority.LOW, com.pablo.clean.domain.entities.TaskStatus.PENDING)
        whenever(repository.findById("1")).thenReturn(existingTask)

        val dtos = listOf(
            TaskDTO("1", "New Description", TaskPriorityDTO.HIGH, TaskStatusDTO.PROCESSING)
        )

        useCase.execute(dtos)

        verify(repository).save(argThat { this.description == "New Description" })
    }

    @Test
    fun `should sync tasks by priority and preserve input order for equal priorities`() = runTest {
        val dtos = listOf(
            TaskDTO("1", "Low", TaskPriorityDTO.LOW, TaskStatusDTO.PENDING),
            TaskDTO("2", "High first", TaskPriorityDTO.HIGH, TaskStatusDTO.PENDING),
            TaskDTO("3", "Medium", TaskPriorityDTO.MEDIUM, TaskStatusDTO.PENDING),
            TaskDTO("4", "High second", TaskPriorityDTO.HIGH, TaskStatusDTO.PENDING)
        )

        useCase.execute(dtos)

        val savedTasks = argumentCaptor<Task>()
        verify(repository, times(4)).save(savedTasks.capture())
        assertEquals(
            listOf("High first", "High second", "Medium", "Low"),
            savedTasks.allValues.map { it.description }
        )
    }
}
