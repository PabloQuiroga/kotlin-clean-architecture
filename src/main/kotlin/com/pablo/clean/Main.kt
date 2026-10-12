package com.pablo.clean

import com.pablo.clean.api.TaskController
import com.pablo.clean.data.dto.TaskDTO
import com.pablo.clean.data.dto.TaskPriorityDTO
import com.pablo.clean.data.dto.TaskStatusDTO
import com.pablo.clean.domain.entities.TaskStatus
import com.pablo.clean.domain.repository.ITaskRepository
import com.pablo.clean.domain.usecases.SyncTasksUseCase
import com.pablo.clean.domain.usecases.UpdateTaskStatusUseCase
import com.pablo.clean.data.repository.TaskRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single<ITaskRepository> { TaskRepositoryImpl() }
    single { SyncTasksUseCase(get()) }
    single { UpdateTaskStatusUseCase(get()) }
    single { TaskController(get(), get()) }
}

fun main() {
    println("=== Starting Task Sync Engine Demo ===")

    startKoin {
        modules(appModule)
    }

    val controller = org.koin.java.KoinJavaComponent.get<TaskController>(TaskController::class.java)

    // 1. Simular ingesta de datos (Sincronización)
    val mockExternalData = listOf(
        TaskDTO("1", "Implement Clean Architecture", TaskPriorityDTO.HIGH, TaskStatusDTO.PENDING),
        TaskDTO("2", "", TaskPriorityDTO.LOW, TaskStatusDTO.PENDING), // Debe ser ignorada
        TaskDTO("3", "Configure CI Pipeline", TaskPriorityDTO.MEDIUM, TaskStatusDTO.PENDING)
    )

    controller.syncTasks(mockExternalData)

    // 2. Simular actualización de estado
    controller.updateStatus("1", TaskStatus.PROCESSING)
    controller.updateStatus("99", TaskStatus.COMPLETED) // Debe fallar (no existe)

    println("=== Demo Finished ===")
}
