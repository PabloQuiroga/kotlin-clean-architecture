# Diseño lógico: motor de sincronización de tareas

## 1. Arquitectura

El dominio contiene las entidades, reglas y casos de uso. Define el contrato del repositorio y no importa implementaciones de API o datos. La demo usa DTOs como entrada; el controlador los convierte a entidades antes de llamar al caso de uso.

### Dominio

- `Task` valida sus datos al construirse.
- `SyncTasksUseCase` recibe entidades `Task`, las ordena por prioridad y las guarda mediante `ITaskRepository`.
- `UpdateTaskStatusUseCase` solicita al repositorio una actualización atómica. La transición se valida usando la tarea leída dentro de esa operación.
- Las transiciones permitidas son Pendiente → Procesando y Procesando → Completado.

### Datos

- `TaskRepositoryImpl` mantiene un mapa inmutable dentro de `MutableStateFlow`.
- Un `Mutex` serializa `save` y `updateTask`. La transformación y validación ocurren bajo el mismo bloqueo; si la transformación falla, el estado no cambia.
- `observeAll()` emite la lista inicial y las listas posteriores.
- Los DTOs y el mapper convierten los datos de ejemplo a entidades. No hay comunicación con una API externa en el alcance actual.

### API/demo

- `TaskController` convierte los DTO a `Task`, omite los DTO inválidos y delega la sincronización y los cambios de estado a los casos de uso.
- `Main` configura Koin, crea datos de ejemplo y ejecuta las operaciones suspendibles.

## 2. Flujos

Sincronización:

`Main/demo → TaskController → mapper DTO→Task → SyncTasksUseCase → ITaskRepository → TaskRepositoryImpl`

Cambio de estado:

`TaskController → UpdateTaskStatusUseCase → updateTask atómico → validación + escritura bajo Mutex`

Observación:

`TaskRepositoryImpl.StateFlow → ITaskRepository.observeAll() → colector`
