package com.pablo.clean.domain.repository

import com.pablo.clean.domain.entities.Task
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    suspend fun save(task: Task): Task
    suspend fun findById(id: String): Task?
    suspend fun findAll(): List<Task>
    suspend fun updateTask(id: String, transform: (Task) -> Task): Task?
    fun observeAll(): Flow<List<Task>>
}
