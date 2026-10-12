package com.pablo.clean.data.repository

import com.pablo.clean.domain.entities.Task
import com.pablo.clean.domain.repository.ITaskRepository
import java.util.concurrent.ConcurrentHashMap

class TaskRepositoryImpl : ITaskRepository {
    private val storage = ConcurrentHashMap<String, Task>()

    override fun save(task: Task): Task {
        storage[task.id] = task
        return task
    }

    override fun findById(id: String): Task? {
        return storage[id]
    }

    override fun findAll(): List<Task> {
        return storage.values.toList()
    }
}
