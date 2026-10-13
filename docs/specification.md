# Especificación: motor de sincronización de tareas

## 1. Alcance actual

El proyecto demuestra la sincronización de tareas con datos de ejemplo. No consume una API externa ni guarda datos de forma durable. El repositorio mantiene las tareas en memoria durante la ejecución del proceso.

## 2. Requerimientos funcionales

### 2.1 Flujo de datos

- **Entrada**: `Main` entrega al controlador una lista de tareas de ejemplo con ID, descripción, prioridad y estado.
- **Mapeo y validación**: el controlador convierte los DTO a entidades de dominio. Las entradas que no cumplen las reglas de la entidad se omiten.
- **Almacenamiento**: las tareas válidas se guardan en el repositorio en memoria. Guardar un ID existente reemplaza la tarea anterior.
- **Consulta reactiva**: el repositorio expone el estado actual y sus cambios como `Flow`.
- **Estados**: solo se permiten Pendiente → Procesando y Procesando → Completado.

### 2.2 Reglas de negocio

- **Prioridad**: sincronizar en orden Alta → Media → Baja y conservar el orden de entrada entre tareas con igual prioridad.
- **Deduplicación**: una tarea con un ID existente reemplaza a la anterior sin crear otra entrada.
- **Concurrencia**: las escrituras y actualizaciones se serializan; la validación de una transición se ejecuta dentro de la actualización atómica para usar el estado más reciente.

## 3. Arquitectura y asincronía

- **Dominio**: entidades, casos de uso e interfaz del repositorio. No importa clases de API ni de datos.
- **Datos**: implementación en memoria del repositorio y DTO/mappers usados para la entrada de ejemplo.
- **API/demo**: el controlador mapea la entrada y coordina los casos de uso; `Main` ejecuta la demo.
- Las operaciones del repositorio son suspendibles. `observeAll()` publica cambios con `Flow`.

## 4. Criterios de aceptación

1. La sincronización omite entradas inválidas y guarda las válidas.
2. La sincronización aplica el orden de prioridad y deduplica por ID.
3. Las transiciones inválidas se rechazan y las actualizaciones concurrentes no aplican una transición sobre un estado obsoleto.
4. El flujo atraviesa controlador, dominio e interfaz del repositorio hasta la implementación de datos.

## 5. Fuera del alcance actual

La conexión a una API externa y la persistencia durable podrán añadirse después detrás de las interfaces de datos.
