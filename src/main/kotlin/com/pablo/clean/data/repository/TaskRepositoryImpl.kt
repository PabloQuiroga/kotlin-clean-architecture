package com.pablo.clean.data.repository

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TaskRepositoryImpl : ITaskRepository {
    private val storage = MutableStateFlow<Map<String, Task>>(emptyMap())

    override suspend fun save(task: Task): Task {
        storage.update { current -> current + (task.id to task) }
        return task
    }

    override suspend fun findById(id: String): Task? {
        return storage.value[id]
    }

    override suspend fun findAll(): List<Task> {
        return storage.value.values.toList()
    }

    override fun observeAll(): Flow<List<Task>> =
        storage.asStateFlow().map { tasks -> tasks.values.toList() }
}
