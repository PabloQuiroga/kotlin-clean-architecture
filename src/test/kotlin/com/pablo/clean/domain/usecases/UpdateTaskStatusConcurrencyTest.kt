package com.pablo.clean.domain.usecases

import com.pablo.clean.data.repository.TaskRepositoryImpl
import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.entities.TaskPriority
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateTaskStatusConcurrencyTest {

    @Test
    fun `should allow only one concurrent attempt to apply the same transition`() = runTest {
        val delegate = TaskRepositoryImpl()
        delegate.save(Task("1", "Concurrent task", TaskPriority.HIGH, TaskStatus.PENDING))
        val repository = BarrierReadTaskRepository(delegate, CyclicBarrier(2))
        val useCase = UpdateTaskStatusUseCase(repository)

        val successfulUpdates = (1..2).map {
            async(Dispatchers.Default) {
                runCatching { useCase.execute("1", TaskStatus.PROCESSING) }.isSuccess
            }
        }.awaitAll().count { it }

        assertEquals(1, successfulUpdates)
        assertEquals(TaskStatus.PROCESSING, delegate.findById("1")?.status)
    }

    private class BarrierReadTaskRepository(
        private val delegate: ITaskRepository,
        private val readBarrier: CyclicBarrier
    ) : ITaskRepository {
        override suspend fun save(task: Task): Task = delegate.save(task)

        override suspend fun findById(id: String): Task? {
            val task = delegate.findById(id)
            readBarrier.await(5, TimeUnit.SECONDS)
            return task
        }

        override suspend fun findAll(): List<Task> = delegate.findAll()

        override fun observeAll() = delegate.observeAll()

        override suspend fun updateTask(id: String, transform: (Task) -> Task): Task? =
            delegate.updateTask(id, transform)
    }
}
