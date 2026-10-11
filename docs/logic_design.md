# Diseño Lógico: Motor de Sincronización de Tareas

## 1. Arquitectura de Capas (Clean Architecture)

El sistema sigue el principio de dependencias unidireccionales: la lógica de negocio es el centro y no conoce los detalles de la implementación externa.

### 1.1 Capa de Dominio (Domain) - El Núcleo
Es la capa más pura y estable.
- **Entidades**: Define la `Tarea` (Task) con sus atributos básicos y reglas de validación (ej: una tarea no puede estar vacía).
- **Casos de Uso (Use Cases)**: Define la orquestación de la lógica.
    - `SincronizarTareas`: Recibe datos externos $\rightarrow$ Valida $\rightarrow$ Guarda en repositorio.
    - `CambiarEstadoTarea`: Valida la transición de estado (ej: de Pendiente a Completado).
- **Interfaces de Repositorio**: Define el contrato de almacenamiento (qué se puede hacer con los datos), pero no cómo se guardan.

### 1.2 Capa de Datos (Data) - La Infraestructura
Implementa los detalles técnicos.
- **Implementación del Repositorio**: Realiza las operaciones reales de lectura/escritura en la base de datos o memoria.
- **Data Sources**: Maneja la comunicación con la API externa.
- **Mapeadores (Mappers)**: Convierte los objetos de la API (DTOs) en Entidades del Dominio.

### 1.3 Capa de API / Presentación (API)
Es el punto de entrada al sistema.
- **Controlador/Orquestador**: Recibe la petición del usuario, llama al Caso de Uso correspondiente y entrega la respuesta.

## 2. Flujo de Datos (Data Flow)
`API` $\rightarrow$ `UseCase` $\rightarrow$ `Repository (Interface)` $\rightarrow$ `Repository Implementation` $\rightarrow$ `Data Source`