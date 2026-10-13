package com.pablo.clean.data.repository

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TaskRepositoryImpl : ITaskRepository {
    private val storage = MutableStateFlow<Map<String, Task>>(emptyMap())
    private val mutex = Mutex()

    override suspend fun save(task: Task): Task = mutex.withLock {
        storage.update { current -> current + (task.id to task) }
        task
    }

    override suspend fun findById(id: String): Task? {
        return storage.value[id]
    }

    override suspend fun findAll(): List<Task> {
        return storage.value.values.toList()
    }

    override suspend fun updateTask(id: String, transform: (Task) -> Task): Task? = mutex.withLock {
        val current = storage.value[id] ?: return@withLock null
        val updated = transform(current)
        storage.update { tasks -> tasks + (id to updated) }
        updated
    }

    override fun observeAll(): Flow<List<Task>> =
        storage.asStateFlow().map { tasks -> tasks.values.toList() }
}
