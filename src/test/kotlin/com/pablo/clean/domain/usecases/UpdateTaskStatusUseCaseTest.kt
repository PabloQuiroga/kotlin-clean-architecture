package com.pablo.clean.domain.usecases

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.*

class UpdateTaskStatusUseCaseTest {

    private val repository: ITaskRepository = mock()
    private val useCase = UpdateTaskStatusUseCase(repository)

    @Test
    fun `should update task status successfully`() = runTest {
        val task = Task("1", "Test Task", TaskPriority.MEDIUM, TaskStatus.PENDING)
        stubUpdate("1", task)

        useCase.execute("1", TaskStatus.PROCESSING)

        verify(repository).updateTask(eq("1"), any())
    }

    @Test
    fun `should allow completing a task that is processing`() = runTest {
        val task = Task("1", "Test Task", TaskPriority.MEDIUM, TaskStatus.PROCESSING)
        stubUpdate("1", task)

        useCase.execute("1", TaskStatus.COMPLETED)

        verify(repository).updateTask(eq("1"), any())
    }

    @Test
    fun `should reject invalid status transitions without saving`() = runTest {
        val invalidTransitions = listOf(
            Task("1", "Task 1", TaskPriority.LOW, TaskStatus.PENDING) to TaskStatus.COMPLETED,
            Task("2", "Task 2", TaskPriority.LOW, TaskStatus.COMPLETED) to TaskStatus.PROCESSING,
            Task("3", "Task 3", TaskPriority.LOW, TaskStatus.PENDING) to TaskStatus.PENDING,
            Task("4", "Task 4", TaskPriority.LOW, TaskStatus.PROCESSING) to TaskStatus.PENDING
        )

        invalidTransitions.forEach { (task, targetStatus) ->
            stubUpdate(task.id, task)
            org.junit.jupiter.api.assertThrows<IllegalStateException> {
                useCase.execute(task.id, targetStatus)
            }
        }

        verify(repository, times(4)).updateTask(any(), any())
    }

    @Test
    fun `should throw exception when task is not found`() = runTest {
        whenever(repository.updateTask(eq("non-existent"), any())).thenReturn(null)

        org.junit.jupiter.api.assertThrows<NoSuchElementException> {
            useCase.execute("non-existent", TaskStatus.COMPLETED)
        }
    }

    private suspend fun stubUpdate(id: String, task: Task) {
        whenever(repository.updateTask(eq(id), any())).thenAnswer { invocation ->
            val transform = invocation.getArgument<(Task) -> Task>(1)
            transform(task)
        }
    }
}
