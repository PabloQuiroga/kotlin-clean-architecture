package com.pablo.clean.domain.entities

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

class TaskTest {

    @Test
    fun `should create task when data is valid`() {
        val task = Task(
            id = "1",
            description = "Valid description",
            priority = TaskPriority.HIGH,
            status = TaskStatus.PENDING
        )
        assertNotNull(task)
        assertEquals("1", task.id)
    }

    @Test
    fun `should throw exception when description is empty`() {
        assertThrows<IllegalArgumentException> {
            Task(
                id = "2",
                description = "",
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.PENDING
            )
        }
    }

    @Test
    fun `should throw exception when task is completed without description`() {
        assertThrows<IllegalArgumentException> {
            Task(
                id = "3",
                description = "",
                priority = TaskPriority.LOW,
                status = TaskStatus.COMPLETED
            )
        }
    }
}
