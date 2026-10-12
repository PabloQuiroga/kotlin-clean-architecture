package com.pablo.clean.domain.repository

import com.pablo.clean.domain.entities.Task

interface ITaskRepository {
    fun save(task: Task): Task
    fun findById(id: String): Task?
    fun findAll(): List<Task>
}
