# Práctica 1 — FitLife: interfaces para dispositivos inteligentes

Prototipos navegables de la aplicación **FitLife** adaptada a **dos dispositivos**:
un **Smartwatch** (Versión A) y una **Smart TV** (Versión B).

## Cómo ejecutar

No requiere instalación. Abre el archivo `index.html` correspondiente en un navegador:

```bash
# Opcional: servidor local
python3 -m http.server 8000
# Smartwatch: http://localhost:8000
# Smart TV:    http://localhost:8000/tv/
```

### Versión A — Smartwatch (`index.html`)
- **Deslizar** (swipe) sobre la pantalla del reloj, o arrastrar con el mouse.
- Botones ◀ ▶, puntos indicadores, flechas del teclado (← →).
- Botón lateral "Atrás" del reloj (simula el botón físico).

### Versión B — Smart TV (`tv/index.html`)
- **Control remoto**: flechas (teclado o control en pantalla) mueven el **foco**.
- **Enter / OK** selecciona; **Esc** o botón ⌂ vuelve al Inicio.
- Barra de pestañas inferior para navegar entre secciones.

## Pantallas

### Versión A — Smartwatch (5 pantallas)

| # | Pantalla | Contenido |
|---|----------|-----------|
| 1 | **Inicio** | Hora en vivo, pasos, FC, calorías y anillo de progreso del objetivo |
| 2 | **Frecuencia cardiaca** | FC actual/mín/máx + indicador visual de zonas con aguja |
| 3 | **Actividad** | Selección: Caminar, Correr, Bicicleta, Entrenamiento |
| 4 | **Objetivo diario** | Meta, pasos actuales y % completado |
| 5 | **Notificaciones** | Objetivo alcanzado, recordatorio, resumen |

### Versión B — Smart TV (5 pantallas)

| # | Pantalla | Contenido |
|---|----------|-----------|
| 1 | **Dashboard** | Usuario, resumen diario, pasos, calorías, FC, objetivo, actividad reciente |
| 2 | **Estadísticas** | Gráficas de barras: pasos/día, calorías/día, tiempo de ejercicio (7 días) |
| 3 | **Actividades** | Selección: Caminar, Correr, Bicicleta, Entrenamiento |
| 4 | **Historial** | Lista navegable con control remoto + panel de detalle |
| 5 | **Objetivos** | Objetivo diario, progreso, objetivo semanal, objetivos alcanzados |

## Decisión de diseño: por qué NO se traslada la app 1:1

| Factor | Smartwatch | Smart TV |
|--------|-----------|----------|
| **Tamaño de pantalla** | Circular y pequeña: **una métrica principal por pantalla**, sin dashboard completo | Grande 16:9: **múltiples métricas a la vez**, gráficas de 7 días, lista + detalle en paralelo |
| **Método de interacción** | Toques y deslizado (swipe); botones táctiles de área amplia (≥ 44 px) | **Control remoto**: navegación por **foco** (elemento resaltado), teclas direccionales y botón **Aceptar**; sin cursor ni scroll fino |
| **Contexto de uso** | Mirada de 1-2 s en movimiento: número grande + icono, sin formularios | Uso sedente a 1-2 m de distancia: tipografía grande, botones grandes, layout espacioso |
| **Capacidades del dispositivo** | Sensores en vivo (pasos, FC, calorías que varían cada 3 s); notificaciones cortas | Visualización rica (gráficas, historial profundo); navegación entre secciones con barra de pestañas |
| **Legibilidad** | Alto contraste, fondo negro, colores con significado (verde=meta, rojo=FC alta) | Foco amarillo de alto contraste visible a distancia; jerarquía clara por tamaño |

## Arquitectura

```
index.html          → Versión A: marco del reloj + 5 secciones
css/styles.css      → Estilos smartwatch (pantalla circular pequeña)
js/app.js           → Navegación swipe/botones, datos simulados, anillos, aguja FC

tv/index.html       → Versión B: marco de TV + 5 paneles + control remoto
tv/css/tv.css       → Estilos smart TV (pantalla grande, sistema de foco)
tv/js/tv.js         → Navegación espacial por foco, gráficas, historial, objetivos
```

Ambas versiones usan el **mismo sistema FitLife** con datos simulados coherentes
(misma usuaria, mismos pasos/calorías/FC), para evidenciar que la diferencia
está en la **interfaz**, no en los datos.
