# Plan de implementación: motor de sincronización de tareas

## Tecnologías

- Kotlin/JVM y Gradle Kotlin DSL.
- Kotlin Coroutines y `Flow` para operaciones suspendibles y observación de cambios.
- Koin para inyección de dependencias.
- JUnit 5 y Mockito-Kotlin para pruebas.
- `kotlinx-coroutines-test` para probar código suspendible y flujos.

## Capas

- **Dominio**: entidades, reglas de estado, casos de uso e interfaz del repositorio. El caso de uso de sincronización recibe entidades `Task`.
- **Datos**: DTOs y mappers para convertir la entrada de demo a entidades; repositorio en memoria implementado con `StateFlow`.
- **API/demo**: `TaskController` convierte DTOs a entidades y coordina los casos de uso; `Main` carga datos de ejemplo y ejecuta la aplicación.

## Alcance actual

- La sincronización omite entradas inválidas y guarda por prioridad Alta → Media → Baja, conservando el orden de entrada entre prioridades iguales.
- El cambio de estado solo permite Pendiente → Procesando → Completado.
- El repositorio ofrece operaciones `suspend` y `observeAll()` como `Flow`.
- Hay pruebas unitarias y un test de integración desde el controlador hasta el repositorio.
- La entrada usa datos de demo y el almacenamiento es en memoria. No hay conexión a una API externa ni persistencia durable.
- GitHub Actions ejecuta `./gradlew build` en pushes y pull requests a `main`.

## Siguiente alcance posible

- Incorporar una fuente externa o almacenamiento durable detrás de contratos de datos sin trasladar esos detalles al dominio.
