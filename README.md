# Kotlin Clean Architecture

Demo de un motor de sincronización de tareas organizado en capas de API, dominio y datos.

## Funcionalidades

- Sincroniza tareas válidas y omite las que no cumplen las reglas de dominio.
- Procesa las tareas por prioridad: Alta, Media y Baja.
- Permite las transiciones Pendiente → Procesando → Completado.
- Expone las tareas guardadas mediante `Flow` y operaciones suspendibles.
- Usa datos de demo y almacenamiento en memoria; no requiere credenciales ni servicios externos.

## Ejecutar

Requiere JDK 17.

```bash
./gradlew run
```

## Pruebas

```bash
./gradlew test
```

La suite incluye pruebas unitarias y un test de integración del controlador, los casos de uso y el repositorio.
