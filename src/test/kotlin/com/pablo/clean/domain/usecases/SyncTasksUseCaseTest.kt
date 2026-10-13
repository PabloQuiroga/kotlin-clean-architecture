package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus
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
    fun `should sync domain tasks`() = runTest {
        val tasks = listOf(
            Task("1", "Task one", TaskPriority.HIGH, TaskStatus.PENDING),
            Task("2", "Task two", TaskPriority.LOW, TaskStatus.PENDING)
        )

        useCase.execute(tasks)

        verify(repository, times(2)).save(any())
    }

    @Test
    fun `should update task if id already exists (deduplication)`() = runTest {
        val existingTask = Task("1", "Old Description", TaskPriority.LOW, TaskStatus.PENDING)
        whenever(repository.findById("1")).thenReturn(existingTask)

        val tasks = listOf(
            Task("1", "New Description", TaskPriority.HIGH, TaskStatus.PROCESSING)
        )

        useCase.execute(tasks)

        verify(repository).save(argThat { this.description == "New Description" })
    }

    @Test
    fun `should sync tasks by priority and preserve input order for equal priorities`() = runTest {
        val tasks = listOf(
            Task("1", "Low", TaskPriority.LOW, TaskStatus.PENDING),
            Task("2", "High first", TaskPriority.HIGH, TaskStatus.PENDING),
            Task("3", "Medium", TaskPriority.MEDIUM, TaskStatus.PENDING),
            Task("4", "High second", TaskPriority.HIGH, TaskStatus.PENDING)
        )

        useCase.execute(tasks)

        val savedTasks = argumentCaptor<Task>()
        verify(repository, times(4)).save(savedTasks.capture())
        assertEquals(
            listOf("High first", "High second", "Medium", "Low"),
            savedTasks.allValues.map { it.description }
        )
    }
}
