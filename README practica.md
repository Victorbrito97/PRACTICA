# Concurrencia y Paralelismo
**Matricula** 58808  
**Alumno:** Pech Brito Victor Manuel  

---
## EXPLICACION BREVE
Los semáforos y monitores son dos mecanismos fundamentales para la sincronización en sistemas concurrentes y paralelos, que permiten gestionar el acceso a recursos compartidos y evitar problemas como condiciones de carrera y bloqueos.

Un semáforo es una variable entera que controla el acceso a recursos mediante dos operaciones atómicas: **wait** (o P) y **signal** (o V). La operación wait decrementa el valor del semáforo y, si este es cero, bloquea el proceso que intenta acceder al recurso, esperando a que otro proceso libere el semáforo. La operación signal incrementa el valor y puede despertar procesos bloqueados. Existen semáforos binarios, que solo toman valores 0 o 1 y garantizan exclusión mutua, y semáforos contadores, que permiten controlar el acceso a múltiples instancias de un recurso. Los semáforos son muy útiles para coordinar procesos en sistemas donde varios hilos o procesos compiten por recursos limitados.

Por otro lado, los monitores son una abstracción de alto nivel que encapsula variables compartidas, procedimientos y mecanismos de sincronización. A diferencia de los semáforos, los monitores garantizan automáticamente la exclusión mutua, permitiendo que solo un proceso esté activo dentro del monitor a la vez. Además, utilizan variables de condición para que los procesos puedan esperar y ser notificados dentro del monitor, facilitando la coordinación entre ellos. Esto simplifica la programación concurrente al ocultar detalles complejos de sincronización.

En el contexto de la concurrencia, donde múltiples tareas progresan simultáneamente pero no necesariamente al mismo tiempo, y el paralelismo, que implica la ejecución simultánea real en múltiples procesadores o núcleos, tanto semáforos como monitores son esenciales para evitar conflictos y asegurar la correcta interacción entre procesos. Sin estos mecanismos, los sistemas podrían experimentar errores como condiciones de carrera, inconsistencias en datos o bloqueos.

En CONCLUSION: semáforos y monitores son herramientas clave para manejar la sincronización en sistemas concurrentes y paralelos, garantizando un acceso ordenado y seguro a los recursos compartidos.

---

## Código Fuente
- `Semaforo.java`  
- `Monitor.java`  

---

# Compilar
javac Semaforo.java
javac Monitor.java

# Ejecutar
java Semaforo 2 2 5 10
java Monitor 2 2 5 10
