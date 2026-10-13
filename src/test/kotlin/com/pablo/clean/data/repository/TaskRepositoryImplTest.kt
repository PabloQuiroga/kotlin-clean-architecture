package com.pablo.clean.data.repository

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class TaskRepositoryImplTest {

    private val repository: ITaskRepository = TaskRepositoryImpl()

    @Test
    fun `should save and find task by id`() = runTest {
        val task = Task("1", "Test Task", TaskPriority.HIGH, TaskStatus.PENDING)
        repository.save(task)

        val found = repository.findById("1")
        assertNotNull(found)
        assertEquals("Test Task", found?.description)
    }

    @Test
    fun `should return null when task is not found`() = runTest {
        val found = repository.findById("non-existent")
        assertNull(found)
    }

    @Test
    fun `should return all saved tasks`() = runTest {
        repository.save(Task("1", "Task 1", TaskPriority.LOW, TaskStatus.PENDING))
        repository.save(Task("2", "Task 2", TaskPriority.MEDIUM, TaskStatus.PENDING))

        val all = repository.findAll()
        assertEquals(2, all.size)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `should emit current tasks and subsequent changes`() = runTest {
        val emissions = mutableListOf<List<Task>>()
        backgroundScope.launch { repository.observeAll().toList(emissions) }
        runCurrent()

        repository.save(Task("1", "Task 1", TaskPriority.LOW, TaskStatus.PENDING))
        runCurrent()

        assertEquals(
            listOf(emptyList<Task>(), listOf(Task("1", "Task 1", TaskPriority.LOW, TaskStatus.PENDING))),
            emissions
        )
    }

    @Test
    fun `should retain all tasks saved concurrently`() = runTest {
        (1..100).map { id ->
            async(Dispatchers.Default) {
                repository.save(Task(id.toString(), "Task $id", TaskPriority.LOW, TaskStatus.PENDING))
            }
        }.awaitAll()

        assertEquals((1..100).map(Int::toString).toSet(), repository.findAll().map { it.id }.toSet())
    }
}
