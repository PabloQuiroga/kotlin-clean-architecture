package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.*

class UpdateTaskStatusUseCaseTest {

    private val repository: ITaskRepository = mock()
    private val useCase = UpdateTaskStatusUseCase(repository)

    @Test
    fun `should update task status successfully`() {
        val task = Task("1", "Test Task", TaskPriority.MEDIUM, TaskStatus.PENDING)
        whenever(repository.findById("1")).thenReturn(task)

        useCase.execute("1", TaskStatus.PROCESSING)

        verify(repository).save(argThat { this.status == TaskStatus.PROCESSING })
    }

    @Test
    fun `should throw exception when task is not found`() {
        whenever(repository.findById("non-existent")).thenReturn(null)

        org.junit.jupiter.api.assertThrows<NoSuchElementException> {
            useCase.execute("non-existent", TaskStatus.COMPLETED)
        }
    }
}
