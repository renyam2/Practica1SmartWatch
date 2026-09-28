# REPORTE — Práctica 1: Diseño de interfaces para dispositivos inteligentes

**Proyecto:** FitLife — monitor de actividad física
**Dispositivos:** Smartwatch (Versión A) · Smart TV (Versión B)

---

## 1. Resumen del proyecto

Se diseñó la misma aplicación de monitorización de actividad física (**FitLife**) para **dos dispositivos muy distintos**:

- **Smartwatch:** pantalla pequeña y circular (~300 px), interacción táctil y por deslizado (swipe), uso rápido con una mano, lectura a centímetros.
- **Smart TV:** pantalla grande 16:9, interacción con control remoto (navegación por foco y teclas direccionales), lectura a 1–2 m de distancia.

Ambas versiones usan **el mismo modelo de datos** (usuario María González, 8,450 pasos, 320 kcal, 72 bpm, meta diaria de 10,000 pasos), lo que demuestra que **lo que cambia es la interfaz, no la información**: la app no se transfiere tal cual, sino que se **adapta** a cada dispositivo.

**Cómo ejecutar:**
- Smartwatch: abrir `index.html` en un navegador.
- Smart TV: abrir `tv/index.html` en un navegador.
- O servidor local: `python3 -m http.server 8000` y abrir `http://localhost:8000` y `http://localhost:8000/tv/`.

---

## 2. Tabla de decisiones de diseño

| Elemento | Smartwatch | Smart TV | Justificación |
|---|---|---|---|
| **Menú** | Sin menú visible: una pantalla a la vez, navegación lineal por deslizado (Inicio → FC → Actividad → Objetivo → Notificaciones) con puntos indicadores. | Barra de pestañas fija en la parte inferior (Inicio, Estadísticas, Actividades, Historial, Objetivos) siempre visible. | En el reloj no hay espacio para un menú; el deslizado es la metáfora natural de la muñeca. En la TV la barra inferior permite saltar a cualquier sección con un movimiento lateral del control, sin perder el contexto. |
| **Botones** | Botones circulares de 44 px (área táctil mínima accesible), botones de actividad de ≥52 px, botón lateral físico "Atrás" y corona. | Botones grandes y espaciados (tarjetas 2×2, botones de actividad de 160 px) con estado de foco visible (borde amarillo + escala). | El dedo necesita áreas amplias y contiguas; el control remoto no toca la pantalla, así que los "botones" son áreas de foco separadas que se resaltan al ser navegadas. |
| **Tipografía** | Títulos de 0.85 rem, valores de 0.95–1.6 rem, etiquetas de 0.55–0.62 rem; una sola métrica grande por pantalla. | Títulos de 1.75 rem, valores de 2.1–2.4 rem, texto base de 1.05 rem; múltiples métricas visibles a la vez. | En el reloj la legibilidad se logra mostrando **poca información con tamaños relativos grandes** dentro del círculo; en la TV el texto debe ser **absolutamente grande** para leerse a 1–2 m, y el espacio disponible permite más contenido por pantalla. |
| **Navegación** | Deslizado horizontal (swipe) + botones ◀ ▶ + flechas del teclado; navegación **secuencial** (pantalla a pantalla). | Navegación **espacial** con flechas direccionales: el foco se mueve entre elementos según su posición en pantalla; Enter selecciona; Esc/⌂ regresa al Inicio. | El swipe es gestual y natural en una muñeca; en la TV no hay cursor, así que el foco se desplaza espacialmente (como en un televisor real) y el botón de aceptación confirma la acción. |
| **Información** | Una métrica principal por pantalla: reloj + anillo de progreso (Inicio), FC con zonas (FC), 4 actividades (Actividad), meta diaria (Objetivo), 3 notificaciones (Notificaciones). | Dashboard con 4 tarjetas + actividad reciente; 3 gráficas de barras de 7 días; grilla de actividades; historial con lista + panel de detalle; objetivos con barras de progreso y logros. | El reloj limita a lo esencial (una lectura rápida a la vista); la TV aprovecha el espacio para mostrar **todo a la vez**: tendencias, comparaciones y detalle sin cambiar de pantalla. |
| **Interacción** | Táctil directa: tocar el elemento, deslizar para cambiar de pantalla; simulación de sensores en vivo (datos cambian cada 3 s). | Control remoto: mover el foco con ▲ ▼ ◀ ▶, confirmar con Enter/OK, volver con Esc/⌂; incluye control remoto en pantalla para la demo en escritorio. | El usuario del reloj está en movimiento y con una mano; el usuario de la TV está sentado a distancia, por lo que toda la interacción debe poder resolverse con **5 teclas** del mando. |
| **Colores** | Tema oscuro de alto contraste (fondo negro, texto claro), acento azul `#38bdf8`, zonas de FC codificadas: verde (baja), amarillo (media), roja (alta); anillos de progreso azul/verde. | Mismo sistema de colores (consistencia de marca) pero con acento amarillo `#ffd54f` reservado **exclusivamente para el foco**, para que el elemento activo sea reconocible de un vistazo a distancia. | El alto contraste es obligatorio en ambos (luz ambiental en el reloj, distancia en la TV). En la TV el color de foco es la señal principal de "qué está seleccionado", por eso se reserva y no se usa para nada más. |
| **Notificaciones** | Pantalla dedicada con 3 tarjetas compactas (icono + título + texto corto + hora); se llega a ella deslizando. | No hay pantalla de notificaciones: la información relevante (resumen, actividad reciente, objetivos) se integra en el Dashboard y las secciones. | En el reloj las notificaciones son un uso frecuente (miradas rápidas), por eso merecen su propia pantalla; en la TV el usuario busca datos deliberadamente, así que las notificaciones se integran en el flujo de información en vez de ser una lista aparte. |

---

## 3. Preguntas

### 3.1 ¿Qué información debe mostrarse primero en el smartwatch?

La información **inmediata y de un solo vistazo**: la hora actual y el estado del día resumido en una métrica (los pasos actuales y el porcentaje de la meta diaria, representados con el anillo de progreso). La frecuencia cardiaca actual (72 bpm) y las calorías completan la pantalla de Inicio como métricas secundarias.

Razón: el usuario del smartwatch mira la muñeca durante 1–2 segundos, de pie o en movimiento. No tiene tiempo para navegar: la primera pantalla debe responder "¿cómo va mi día?" sin que tenga que hacer ninguna acción. El resto (detalle de FC, actividades, notificaciones) queda a **un deslizado** de distancia, pero nunca compite con la lectura principal.

### 3.2 ¿Qué información puede mostrarse con mayor detalle en la Smart TV?

La TV puede mostrar **contexto y tendencias que el reloj no puede albergar**:

- **Tiempos seriales:** gráficas de barras de los últimos 7 días (pasos, calorías y minutos de ejercicio por día).
- **Historial completo:** lista de actividades pasadas con fecha, duración, pasos, calorías y FC promedio, más un panel de detalle al seleccionar cada entrada.
- **Objetivos ampliados:** meta diaria y semanal con barras de progreso, porcentajes y lista de objetivos ya alcanzados (logros).
- **Vista simultánea:** el Dashboard muestra 4 métricas + actividad reciente a la vez, algo imposible en una pantalla circular de 300 px.

Razón: la TV se mira de forma deliberada y prolongada, a 1–2 m, con espacio de sobra (16:9). Por eso la interfaz puede "aplanar" la información: lo que en el reloj son 5 pantallas encadenadas, en la TV son 5 secciones navegables con un solo movimiento lateral del mando, y cada sección puede contener varias tablas/gráficas a la vez.

### 3.3 ¿Qué diferencias existen entre tocar una pantalla y navegar con un control remoto?

| Aspecto | Tocar la pantalla (smartwatch) | Control remoto (Smart TV) |
|---|---|---|
| **Precisión** | Alta: el dedo apunta exactamente al elemento que toca. | Baja: no hay cursor; el foco salta de elemento en elemento en orden espacial, y el usuario no controla la posición exacta del puntero. |
| **Feedback** | Inmediato y local (el elemento tocado reacciona). | El feedback es el **resaltado de foco** (borde amarillo + escala); sin él el usuario no sabría qué está seleccionado. |
| **Modelo mental** | "Toco lo que veo" (directo). | "Muevo el foco y confirmo" (indirecto, en dos pasos: navegar + Enter). |
| **Errores** | Tocar un botón equivocado es fácil de corregir (se toca otro). | Saltar el foco equivocado requiere mover flechas hasta el correcto; por eso el orden espacial de los elementos importa mucho. |
| **Cuerpo** | Una mano, a centímetros, a menudo en movimiento. | Sentado, a 1–2 m, con un dispositivo de 5 teclas. |
| **Diseño que exige** | Áreas táctiles grandes y contiguas (≥44 px), gestos (swipe). | Elementos bien separados, foco visible, mínimo de opciones por pantalla y un botón de "volver" siempre disponible (Esc/⌂). |

Conclusión: con el dedo el diseño optimiza **área táctil y gestos**; con el mando optimiza **jerarquía espacial y confirmación**. Por eso los mismos datos (pasos, FC, calorías) se organizan de forma distinta en cada dispositivo.

### 3.4 ¿Qué elementos de la interfaz tuvieron que modificarse entre ambos dispositivos?

1. **El menú:** deslizado lineal con puntos (watch) → barra de pestañas inferior fija (TV).
2. **Los botones:** áreas táctiles de 44–52 px (watch) → áreas de foco grandes (160 px) con resaltado amarillo y escala (TV).
3. **La tipografía:** tamaños relativos pequeños dentro del círculo (watch) → tamaños absolutos grandes para distancia de 1–2 m (TV).
4. **La navegación:** secuencial (pantalla a pantalla con swipe) → espacial (foco que se mueve entre elementos con flechas, confirmación con Enter).
5. **La cantidad de información por pantalla:** una métrica principal por pantalla (watch) → múltiples métricas, gráficas y tablas simultáneas (TV).
6. **Las notificaciones:** pantalla dedicada con tarjetas compactas (watch) → integradas en el Dashboard y las secciones (TV).
7. **El feedback de selección:** borde azul en el botón tocado (watch) → borde amarillo + escala en el elemento enfocado (TV).
8. **La retroalimentación de "volver":** botón lateral físico + deslizado inverso (watch) → tecla Esc / botón ⌂ del mando (TV).
9. **La representación de datos:** anillos radiales y aguja de zonas (watch) → gráficas de barras de 7 días y barras de progreso horizontales (TV).
10. **El color de acento:** azul `#38bdf8` para datos (watch) → amarillo `#ffd54f` reservado para el foco (TV), manteniendo el mismo sistema de colores de marca.

### 3.5 ¿Cuál de los dos diseños requiere una navegación más simplificada y por qué?

El **smartwatch**.

- **Una sola acción por pantalla:** cambiar de pantalla es el gesto único (deslizar); dentro de cada pantalla hay, como máximo, una decisión (elegir una actividad).
- **Sin menús ni submenús:** la estructura es lineal (5 pantallas encadenadas), no un árbol de navegación.
- **Contexto de uso:** el usuario mira la muñeca 1–2 segundos, de pie, en movimiento, a menudo con una sola mano. Cada segundo de navegación extra es un fallo de diseño.
- **Área mínima:** la pantalla circular de ~300 px no permite mostrar más de un botón de acción relevante a la vez.

En la TV la navegación es más "rica" (foco espacial entre muchos elementos, confirmación, retroceso) precisamente porque el usuario está **sentado, a distancia y con tiempo** para explorar; la complejidad de navegación se paga con la ventaja de mostrar mucho más contenido a la vez.

---

## 4. Capturas de pantalla

> Instrucciones: abre cada versión en el navegador, cambia a la pantalla correspondiente y toma la captura. Guarda cada imagen en la carpeta `capturas/` con el nombre indicado y reemplaza los espacios en blanco.

### 4.1 Versión Smartwatch (`index.html`)

**Pantalla 1 — Inicio** (reloj + anillo de progreso + pasos, FC y calorías)

![Smartwatch · Inicio](capturas/sw-01-inicio.png)

**Pantalla 2 — Frecuencia cardiaca** (valor actual + aguja sobre las zonas verde/amarilla/roja)

![Smartwatch · Frecuencia cardiaca](capturas/sw-02-frecuencia-cardiaca.png)

**Pantalla 3 — Actividad** (grilla 2×2 de actividades; se resalta la seleccionada)

![Smartwatch · Actividad](capturas/sw-03-actividad.png)

**Pantalla 4 — Objetivo diario** (anillo de progreso + meta y pasos actuales)

![Smartwatch · Objetivo diario](capturas/sw-04-objetivo-diario.png)

**Pantalla 5 — Notificaciones** (3 tarjetas: objetivo alcanzado, recordatorio, resumen)

![Smartwatch · Notificaciones](capturas/sw-05-notificaciones.png)

### 4.2 Versión Smart TV (`tv/index.html`)

**Pantalla 1 — Inicio / Dashboard** (4 tarjetas + actividad reciente; el elemento enfocado se resalta en amarillo)

![Smart TV · Dashboard](capturas/tv-01-dashboard.png)

**Pantalla 2 — Estadísticas** (3 gráficas de barras de 7 días: pasos, calorías, tiempo)

![Smart TV · Estadísticas](capturas/tv-02-estadisticas.png)

**Pantalla 3 — Actividades** (grilla 2×2; navegar con flechas y seleccionar con Enter)

![Smart TV · Actividades](capturas/tv-03-actividades.png)

**Pantalla 4 — Historial** (lista de actividades + panel de detalle al seleccionar)

![Smart TV · Historial](capturas/tv-04-historial.png)

**Pantalla 5 — Objetivos** (barras de progreso diaria/semanal + objetivos alcanzados)

![Smart TV · Objetivos](capturas/tv-05-objetivos.png)

---

## 5. Anexos — Scripts del proyecto

### 5.1 `index.html` (Smartwatch)

```html
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>FitLife &#x2014; Smartwatch</title>
  <link rel="stylesheet" href="css/styles.css">
</head>
<body>
  <header class="page-header">
    <h1>FitLife &#xB7; Prototipo Smartwatch</h1>
    <p>Desliza (o usa las flechas) para cambiar de pantalla. Pantalla actual:
      <span id="screen-name" class="screen-name">Inicio</span>
      &nbsp;&#xB7;&nbsp; <a href="tv/index.html">Versi&#xF3;n Smart TV &#x2192;</a>
    </p>
  </header>

  <main class="watch-stage">
    <!-- Marco f&#xED;sico del smartwatch -->
    <div class="watch-body">
      <div class="watch-crown" title="Corona (bot&#xF3;n lateral)"></div>
      <div class="watch-screen" id="watch-screen">

        <!-- ============ PANTALLA 1: INICIO ============ -->
        <section class="screen active" id="screen-home">
          <div class="clock" id="clock">12:00</div>
          <div class="ring" id="daily-ring" title="Progreso del objetivo diario">
            <svg viewBox="0 0 120 120">
              <circle class="ring-bg" cx="60" cy="60" r="52"></circle>
              <circle class="ring-fg" cx="60" cy="60" r="52"
                      stroke-dasharray="326.7" stroke-dashoffset="326.7"
                      id="ring-fg"></circle>
            </svg>
            <div class="ring-center">
              <span id="ring-percent">0%</span>
              <small>objetivo</small>
            </div>
          </div>
          <div class="metrics-row">
            <div class="metric">
              <span class="metric-icon">&#x1F463;</span>
              <span class="metric-value" id="home-steps">0</span>
              <span class="metric-label">pasos</span>
            </div>
            <div class="metric">
              <span class="metric-icon">&#x2764;&#xFE0F;</span>
              <span class="metric-value" id="home-hr">0</span>
              <span class="metric-label">bpm</span>
            </div>
            <div class="metric">
              <span class="metric-icon">&#x1F525;</span>
              <span class="metric-value" id="home-cal">0</span>
              <span class="metric-label">kcal</span>
            </div>
          </div>
          <div class="hint">Desliza &#x2192; para m&#xE1;s</div>
        </section>

        <!-- ============ PANTALLA 2: FRECUENCIA CARDIACA ============ -->
        <section class="screen" id="screen-hr">
          <h2 class="screen-title">Frecuencia cardiaca</h2>
          <div class="hr-current">
            <span class="hr-icon">&#x2764;&#xFE0F;</span>
            <span id="hr-now">72</span>
            <small>bpm</small>
          </div>
          <div class="hr-gauge" id="hr-gauge" title="Indicador visual de zona">
            <div class="gauge-band zone-low"></div>
            <div class="gauge-band zone-mid"></div>
            <div class="gauge-band zone-high"></div>
            <div class="gauge-needle" id="hr-needle"></div>
          </div>
          <div class="hr-zones">
            <div class="zone"><span class="dot dot-green"></span><span id="hr-min">45</span> m&#xED;n</div>
            <div class="zone"><span class="dot dot-yellow"></span> zona media</div>
            <div class="zone"><span class="dot dot-red"></span><span id="hr-max">190</span> m&#xE1;x</div>
          </div>
          <div class="hint">&#x2190; desliza para volver</div>
        </section>

        <!-- ============ PANTALLA 3: ACTIVIDAD ============ -->
        <section class="screen" id="screen-activity">
          <h2 class="screen-title">Actividad</h2>
          <p class="screen-sub">Elige una actividad</p>
          <div class="activity-grid">
            <button class="activity-btn" data-activity="Caminar">
              <span class="act-icon">&#x1F6B6;</span><span>Caminar</span>
            </button>
            <button class="activity-btn" data-activity="Correr">
              <span class="act-icon">&#x1F3C3;</span><span>Correr</span>
            </button>
            <button class="activity-btn" data-activity="Bicicleta">
              <span class="act-icon">&#x1F6B4;</span><span>Bicicleta</span>
            </button>
            <button class="activity-btn" data-activity="Entrenamiento">
              <span class="act-icon">&#x1F3CB;&#xFE0F;</span><span>Entrenamiento</span>
            </button>
          </div>
          <div class="activity-selected" id="activity-selected">
            Seleccionada: <strong id="activity-name">&#x2014;</strong>
          </div>
          <div class="hint">&#x2190; desliza para volver</div>
        </section>

        <!-- ============ PANTALLA 4: OBJETIVO DIARIO ============ -->
        <section class="screen" id="screen-goal">
          <h2 class="screen-title">Objetivo diario</h2>
          <div class="goal-ring">
            <svg viewBox="0 0 120 120">
              <circle class="ring-bg" cx="60" cy="60" r="52"></circle>
              <circle class="ring-fg ring-fg-goal" cx="60" cy="60" r="52"
                      stroke-dasharray="326.7" stroke-dashoffset="326.7"
                      id="goal-fg"></circle>
            </svg>
            <div class="ring-center">
              <span id="goal-percent">0%</span>
              <small>completado</small>
            </div>
          </div>
          <div class="goal-stats">
            <div class="stat">
              <span class="stat-label">Meta de pasos</span>
              <span class="stat-value" id="goal-target">10,000</span>
            </div>
            <div class="stat">
              <span class="stat-label">Pasos actuales</span>
              <span class="stat-value" id="goal-steps">0</span>
            </div>
          </div>
          <div class="hint">&#x2190; desliza para volver</div>
        </section>

        <!-- ============ PANTALLA 5: NOTIFICACIONES ============ -->
        <section class="screen" id="screen-notifications">
          <h2 class="screen-title">Notificaciones</h2>
          <div class="notif-list" id="notif-list">
            <div class="notif">
              <span class="notif-icon">&#x1F3AF;</span>
              <div class="notif-body">
                <strong>Objetivo alcanzado</strong>
                <p>&#xA1;Completaste tu meta de pasos de hoy!</p>
                <time>08:15</time>
              </div>
            </div>
            <div class="notif">
              <span class="notif-icon">&#x23F0;</span>
              <div class="notif-body">
                <strong>Recordatorio de actividad</strong>
                <p>Llevas 2 h sin moverte. &#xA1;Est&#xED;rate!</p>
                <time>11:30</time>
              </div>
            </div>
            <div class="notif">
              <span class="notif-icon">&#x1F4CA;</span>
              <div class="notif-body">
                <strong>Resumen de actividad</strong>
                <p>8,450 pasos &#xB7; 320 kcal &#xB7; 42 min de ejercicio.</p>
                <time>18:00</time>
              </div>
            </div>
          </div>
          <div class="hint">&#x2190; desliza para volver</div>
        </section>

      </div>
      <div class="watch-button" title="Bot&#xF3;n lateral">
        <span class="btn-label">Atr&#xE1;s</span>
      </div>
    </div>

    <!-- Controles de navegaci&#xF3;n (simulan deslizado / botones) -->
    <nav class="watch-nav">
      <button id="nav-prev" title="Pantalla anterior">&#x25C0;</button>
      <div class="dots" id="dots"></div>
      <button id="nav-next" title="Pantalla siguiente">&#x25B6;</button>
    </nav>
  </main>

  <script src="js/app.js"></script>
</body>
</html>
```

### 5.2 `css/styles.css` (Smartwatch)

```css
/* ============================================================
   FitLife — Smartwatch
   Diseño adaptado a pantalla pequeña y circular:
   - una métrica principal por pantalla
   - tipografía grande y de alto contraste
   - áreas táctiles amplias (botones ≥ 44px de área efectiva)
   - navegación por deslizado (swipe)
   ============================================================ */

:root {
  --bg: #0d1117;
  --screen-bg: #000;
  --text: #f2f4f8;
  --text-dim: #9aa3b2;
  --accent: #38bdf8;
  --green: #4ade80;
  --yellow: #facc15;
  --red: #f87171;
  --card: rgba(255, 255, 255, 0.08);
}

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
  background: var(--bg);
  color: var(--text);
  font-family: "Segoe UI", system-ui, sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.page-header {
  text-align: center;
  padding: 20px 16px 8px;
}
.page-header h1 { font-size: 1.25rem; font-weight: 600; }
.page-header p { color: var(--text-dim); font-size: 0.9rem; margin-top: 4px; }
.screen-name { color: var(--accent); font-weight: 700; }
.page-header a { color: var(--accent); text-decoration: none; font-weight: 600; }

/* ---------- Marco físico del reloj ---------- */
.watch-stage {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18px;
  padding: 12px 16px 32px;
}

.watch-body {
  position: relative;
  background: #1c2129;
  border-radius: 42px;
  padding: 18px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.6);
}

/* Corona (botón lateral superior) */
.watch-crown {
  position: absolute;
  right: -8px;
  top: 70px;
  width: 10px;
  height: 44px;
  background: #2a3140;
  border-radius: 4px;
}

/* Botón lateral "Atrás" */
.watch-button {
  position: absolute;
  right: -8px;
  top: 150px;
  width: 10px;
  height: 56px;
  background: #2a3140;
  border-radius: 4px;
  cursor: pointer;
}
.btn-label {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.6rem;
  color: var(--text-dim);
  white-space: nowrap;
}

/* ---------- Pantalla circular ---------- */
.watch-screen {
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background: var(--screen-bg);
  overflow: hidden;
  position: relative;
  border: 4px solid #2a3140;
  touch-action: pan-x;
}

/* ---------- Pantallas (secciones) ---------- */
.screen {
  position: absolute;
  inset: 0;
  display: none;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 34px 30px;
  transition: opacity 0.25s ease;
}
.screen.active { display: flex; }

.screen-title {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--accent);
  margin-bottom: 6px;
}
.screen-sub { font-size: 0.7rem; color: var(--text-dim); margin-bottom: 8px; }

.hint {
  font-size: 0.62rem;
  color: var(--text-dim);
  opacity: 0.7;
  margin-top: auto;
}

/* ---------- Pantalla 1: Inicio ---------- */
.clock {
  font-size: 1.6rem;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 4px;
}

.ring, .goal-ring {
  position: relative;
  width: 130px;
  height: 130px;
}
.ring svg, .goal-ring svg { width: 100%; height: 100%; transform: rotate(-90deg); }

.ring-bg {
  fill: none;
  stroke: rgba(255,255,255,0.12);
  stroke-width: 8;
}
.ring-fg {
  fill: none;
  stroke: var(--accent);
  stroke-width: 8;
  stroke-linecap: round;
  transition: stroke-dashoffset 0.8s ease;
}
.ring-fg-goal { stroke: var(--green); }

.ring-center {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.ring-center span { font-size: 1.1rem; font-weight: 700; }
.ring-center small { font-size: 0.6rem; color: var(--text-dim); }

.metrics-row {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.metric {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: var(--card);
  border-radius: 10px;
  padding: 6px 8px;
  min-width: 62px;
}
.metric-icon { font-size: 0.9rem; }
.metric-value { font-size: 0.95rem; font-weight: 700; }
.metric-label { font-size: 0.55rem; color: var(--text-dim); }

/* ---------- Pantalla 2: Frecuencia cardiaca ---------- */
.hr-current {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin-bottom: 10px;
}
.hr-icon { font-size: 1.1rem; }
.hr-current > span:first-of-type { font-size: 2.2rem; font-weight: 700; color: var(--red); }
.hr-current small { font-size: 0.7rem; color: var(--text-dim); }

.hr-gauge {
  position: relative;
  width: 180px;
  height: 44px;
  border-radius: 22px;
  overflow: hidden;
  display: flex;
  margin-bottom: 10px;
}
.gauge-band { height: 100%; }
.zone-low  { background: var(--green);  flex: 2; opacity: 0.85; }
.zone-mid  { background: var(--yellow); flex: 3; opacity: 0.85; }
.zone-high { background: var(--red);    flex: 2; opacity: 0.85; }
.gauge-needle {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 4px;
  background: #fff;
  left: 40%;
  transition: left 0.6s ease;
  box-shadow: 0 0 6px rgba(255,255,255,0.8);
}

.hr-zones {
  display: flex;
  gap: 12px;
  font-size: 0.62rem;
  color: var(--text-dim);
}
.zone { display: flex; align-items: center; gap: 4px; }
.dot { width: 8px; height: 8px; border-radius: 50%; display: inline-block; }
.dot-green  { background: var(--green); }
.dot-yellow { background: var(--yellow); }
.dot-red    { background: var(--red); }

/* ---------- Pantalla 3: Actividad ---------- */
.activity-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
  width: 100%;
  margin-top: 4px;
}
.activity-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.15);
  border-radius: 12px;
  color: var(--text);
  font-size: 0.68rem;
  padding: 10px 6px;
  cursor: pointer;
  min-height: 52px; /* área táctil accesible */
  transition: background 0.15s ease, border-color 0.15s ease;
}
.activity-btn:hover { background: rgba(255,255,255,0.16); }
.activity-btn.selected {
  border-color: var(--accent);
  background: rgba(56,189,248,0.18);
}
.act-icon { font-size: 1.2rem; }

.activity-selected {
  margin-top: 10px;
  font-size: 0.7rem;
  color: var(--text-dim);
}
.activity-selected strong { color: var(--accent); }

/* ---------- Pantalla 4: Objetivo diario ---------- */
.goal-stats {
  display: flex;
  gap: 18px;
  margin-top: 10px;
}
.stat {
  display: flex;
  flex-direction: column;
  background: var(--card);
  border-radius: 10px;
  padding: 8px 12px;
}
.stat-label { font-size: 0.58rem; color: var(--text-dim); }
.stat-value { font-size: 1rem; font-weight: 700; }

/* ---------- Pantalla 5: Notificaciones ---------- */
.notif-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  margin-top: 4px;
}
.notif {
  display: flex;
  gap: 8px;
  text-align: left;
  background: var(--card);
  border-radius: 10px;
  padding: 8px 10px;
}
.notif-icon { font-size: 1rem; }
.notif-body { flex: 1; }
.notif-body strong { font-size: 0.68rem; display: block; }
.notif-body p { font-size: 0.6rem; color: var(--text-dim); margin-top: 2px; }
.notif-body time { font-size: 0.55rem; color: var(--text-dim); }

/* ---------- Navegación ---------- */
.watch-nav {
  display: flex;
  align-items: center;
  gap: 14px;
}
.watch-nav button {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.2);
  color: var(--text);
  border-radius: 50%;
  width: 44px;   /* área táctil mínima accesible */
  height: 44px;
  font-size: 1rem;
  cursor: pointer;
}
.watch-nav button:hover { background: rgba(255,255,255,0.18); }

.dots { display: flex; gap: 8px; }
.dot-nav {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255,255,255,0.25);
  cursor: pointer;
}
.dot-nav.active { background: var(--accent); }
```

### 5.3 `js/app.js` (Smartwatch)

```javascript
/* ============================================================
   FitLife — Smartwatch
   Lógica de navegación y datos simulados
   - Navegación por deslizado (swipe) y botones
   - Reloj en tiempo real
   - Datos simulados (pasos, FC, calorías)
   ============================================================ */

// ---------- Datos simulados ----------
const state = {
  steps: 8450,
  hr: 72,
  hrMin: 45,
  hrMax: 190,
  calories: 320,
  goal: 10000,
  activity: null,
};

// ---------- Pantallas ----------
const screens = [
  { id: "screen-home",           name: "Inicio" },
  { id: "screen-hr",             name: "Frecuencia cardiaca" },
  { id: "screen-activity",       name: "Actividad" },
  { id: "screen-goal",           name: "Objetivo diario" },
  { id: "screen-notifications",  name: "Notificaciones" },
];
let current = 0;

const screenEls = screens.map(s => document.getElementById(s.id));
const screenNameEl = document.getElementById("screen-name");
const dotsEl = document.getElementById("dots");

// Puntos de navegación
screens.forEach((s, i) => {
  const d = document.createElement("span");
  d.className = "dot-nav";
  d.title = s.name;
  d.addEventListener("click", () => goTo(i));
  dotsEl.appendChild(d);
});

function goTo(index) {
  index = (index + screens.length) % screens.length;
  screenEls.forEach((el, i) => el.classList.toggle("active", i === index));
  dotsEl.querySelectorAll(".dot-nav").forEach((d, i) =>
    d.classList.toggle("active", i === index));
  screenNameEl.textContent = screens[index].name;
  current = index;
}

// ---------- Botones de navegación ----------
document.getElementById("nav-prev").addEventListener("click", () => goTo(current - 1));
document.getElementById("nav-next").addEventListener("click", () => goTo(current + 1));

// Botón lateral "Atrás" del reloj
document.querySelector(".watch-button").addEventListener("click", () => goTo(current - 1));

// Teclado (accesibilidad)
document.addEventListener("keydown", (e) => {
  if (e.key === "ArrowLeft")  goTo(current - 1);
  if (e.key === "ArrowRight") goTo(current + 1);
});

// ---------- Deslizado (swipe) sobre la pantalla ----------
const watchScreen = document.getElementById("watch-screen");
let swipeStartX = null;

watchScreen.addEventListener("touchstart", (e) => {
  swipeStartX = e.touches[0].clientX;
}, { passive: true });

watchScreen.addEventListener("touchend", (e) => {
  if (swipeStartX === null) return;
  const dx = e.changedTouches[0].clientX - swipeStartX;
  if (Math.abs(dx) > 30) goTo(dx < 0 ? current + 1 : current - 1);
  swipeStartX = null;
});

// También permite arrastrar con el mouse (útil para la demo en escritorio)
watchScreen.addEventListener("mousedown", (e) => { swipeStartX = e.clientX; });
watchScreen.addEventListener("mouseup", (e) => {
  if (swipeStartX === null) return;
  const dx = e.clientX - swipeStartX;
  if (Math.abs(dx) > 30) goTo(dx < 0 ? current + 1 : current - 1);
  swipeStartX = null;
});

// ---------- Reloj ----------
function updateClock() {
  const now = new Date();
  document.getElementById("clock").textContent =
    now.toLocaleTimeString("es", { hour: "2-digit", minute: "2-digit" });
}
setInterval(updateClock, 1000);
updateClock();

// ---------- Anillos de progreso ----------
const RING_CIRC = 2 * Math.PI * 52; // 326.7

function setRing(elId, percent) {
  const el = document.getElementById(elId);
  el.style.strokeDashoffset = RING_CIRC * (1 - percent / 100);
}

function renderHome() {
  const pct = Math.min(100, Math.round(state.steps / state.goal * 100));
  document.getElementById("home-steps").textContent = state.steps.toLocaleString("es");
  document.getElementById("home-hr").textContent = state.hr;
  document.getElementById("home-cal").textContent = state.calories;
  document.getElementById("ring-percent").textContent = pct + "%";
  setRing("ring-fg", pct);
}

function renderGoal() {
  const pct = Math.min(100, Math.round(state.steps / state.goal * 100));
  document.getElementById("goal-percent").textContent = pct + "%";
  document.getElementById("goal-target").textContent = state.goal.toLocaleString("es");
  document.getElementById("goal-steps").textContent = state.steps.toLocaleString("es");
  setRing("goal-fg", pct);
}

// ---------- Frecuencia cardiaca ----------
function renderHR() {
  document.getElementById("hr-now").textContent = state.hr;
  document.getElementById("hr-min").textContent = state.hrMin;
  document.getElementById("hr-max").textContent = state.hrMax;
  // Posición de la aguja entre mín y máx
  const t = (state.hr - state.hrMin) / (state.hrMax - state.hrMin);
  const left = Math.max(2, Math.min(96, t * 100));
  document.getElementById("hr-needle").style.left = left + "%";
}

// ---------- Selección de actividad ----------
document.querySelectorAll(".activity-btn").forEach((btn) => {
  btn.addEventListener("click", () => {
    document.querySelectorAll(".activity-btn").forEach(b => b.classList.remove("selected"));
    btn.classList.add("selected");
    state.activity = btn.dataset.activity;
    document.getElementById("activity-name").textContent = state.activity;
  });
});

// ---------- Simulación de datos en vivo ----------
setInterval(() => {
  state.steps += Math.floor(Math.random() * 8);
  state.hr = Math.max(state.hrMin, Math.min(state.hrMax,
    state.hr + Math.floor(Math.random() * 9) - 4));
  state.calories += Math.random() < 0.4 ? 1 : 0;
  renderHome();
  renderGoal();
  renderHR();
}, 3000);

// ---------- Inicialización ----------
goTo(0);
renderHome();
renderGoal();
renderHR();
```

### 5.4 `tv/index.html` (Smart TV)

```html
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>FitLife &#x2014; Smart TV</title>
  <link rel="stylesheet" href="css/tv.css">
</head>
<body>
  <header class="page-header">
    <h1>FitLife &#xB7; Prototipo Smart TV</h1>
    <p>
      Navega con las flechas (o el control remoto en pantalla) y selecciona con
      <kbd>Enter</kbd> / <kbd>Aceptar</kbd>.
      <a href="../index.html">&#x2190; Versi&#xF3;n Smartwatch</a>
    </p>
  </header>

  <main class="tv-stage">
    <!-- Marco f&#xED;sico de la TV -->
    <div class="tv-body">
      <div class="tv-screen" id="tv-screen">

        <!-- ============ PANTALLA 1: DASHBOARD ============ -->
        <section class="tv-screen-panel active" id="panel-dashboard">
          <div class="dash-header">
            <div class="user">
              <span class="avatar">&#x1F9D1;</span>
              <div>
                <h2>Mar&#xED;a Gonz&#xE1;lez</h2>
                <small>Resumen diario &#xB7; <span id="tv-date"></span></small>
              </div>
            </div>
            <div class="dash-goal-pill" tabindex="0" data-nav="goals">
              Objetivo del d&#xED;a: <strong id="dash-goal-pct">0%</strong>
            </div>
          </div>

          <div class="dash-grid">
            <div class="card" tabindex="0" data-nav="stats">
              <span class="card-icon">&#x1F463;</span>
              <span class="card-value" id="dash-steps">0</span>
              <span class="card-label">Pasos</span>
            </div>
            <div class="card" tabindex="0" data-nav="stats">
              <span class="card-icon">&#x1F525;</span>
              <span class="card-value" id="dash-cal">0</span>
              <span class="card-label">Calor&#xED;as (kcal)</span>
            </div>
            <div class="card" tabindex="0" data-nav="stats">
              <span class="card-icon">&#x2764;&#xFE0F;</span>
              <span class="card-value" id="dash-hr">0</span>
              <span class="card-label">Frecuencia cardiaca (bpm)</span>
            </div>
            <div class="card" tabindex="0" data-nav="goals">
              <span class="card-icon">&#x1F3AF;</span>
              <span class="card-value" id="dash-goal">10,000</span>
              <span class="card-label">Objetivo de pasos</span>
            </div>
          </div>

          <div class="dash-recent" tabindex="0" data-nav="history">
            <h3>Actividad reciente</h3>
            <ul id="recent-list"></ul>
          </div>
        </section>

        <!-- ============ PANTALLA 2: ESTAD&#xCD;STICAS ============ -->
        <section class="tv-screen-panel" id="panel-stats">
          <h2 class="panel-title">Estad&#xED;sticas &#xB7; &#xFA;ltimos 7 d&#xED;as</h2>
          <div class="charts">
            <div class="chart" tabindex="0">
              <h3>Pasos por d&#xED;a</h3>
              <div class="chart-bars" id="chart-steps"></div>
            </div>
            <div class="chart" tabindex="0">
              <h3>Calor&#xED;as por d&#xED;a</h3>
              <div class="chart-bars" id="chart-cal"></div>
            </div>
            <div class="chart" tabindex="0">
              <h3>Tiempo de ejercicio (min)</h3>
              <div class="chart-bars" id="chart-time"></div>
            </div>
          </div>
        </section>

        <!-- ============ PANTALLA 3: ACTIVIDADES ============ -->
        <section class="tv-screen-panel" id="panel-activities">
          <h2 class="panel-title">Actividades</h2>
          <p class="panel-sub">Selecciona una actividad con el control remoto</p>
          <div class="activity-grid" id="activity-grid">
            <button class="tv-activity" data-activity="Caminar">
              <span class="act-icon">&#x1F6B6;</span><span>Caminar</span>
            </button>
            <button class="tv-activity" data-activity="Correr">
              <span class="act-icon">&#x1F3C3;</span><span>Correr</span>
            </button>
            <button class="tv-activity" data-activity="Bicicleta">
              <span class="act-icon">&#x1F6B4;</span><span>Bicicleta</span>
            </button>
            <button class="tv-activity" data-activity="Entrenamiento">
              <span class="act-icon">&#x1F3CB;&#xFE0F;</span><span>Entrenamiento</span>
            </button>
          </div>
          <div class="activity-selected" id="activity-selected">
            Seleccionada: <strong id="activity-name">&#x2014;</strong>
          </div>
        </section>

        <!-- ============ PANTALLA 4: HISTORIAL ============ -->
        <section class="tv-screen-panel" id="panel-history">
          <h2 class="panel-title">Historial de actividades</h2>
          <div class="history-layout">
            <ul class="history-list" id="history-list"></ul>
            <aside class="history-detail" id="history-detail">
              <p class="detail-empty">Usa &#x2191; &#x2193; para elegir una actividad y <kbd>Enter</kbd> para ver el detalle.</p>
            </aside>
          </div>
        </section>

        <!-- ============ PANTALLA 5: OBJETIVOS ============ -->
        <section class="tv-screen-panel" id="panel-goals">
          <h2 class="panel-title">Objetivos</h2>
          <div class="goals-layout">
            <div class="goal-block" tabindex="0">
              <h3>Objetivo diario</h3>
              <div class="progress">
                <div class="progress-bar" id="daily-bar"></div>
                <span class="progress-text" id="daily-pct">0%</span>
              </div>
              <p><span id="goal-steps">0</span> de <span id="goal-target">10,000</span> pasos</p>
            </div>
            <div class="goal-block" tabindex="0">
              <h3>Objetivo semanal</h3>
              <div class="progress">
                <div class="progress-bar weekly" id="weekly-bar"></div>
                <span class="progress-text" id="weekly-pct">0%</span>
              </div>
              <p><span id="weekly-steps">0</span> de <span id="weekly-target">70,000</span> pasos</p>
            </div>
            <div class="goal-block" tabindex="0">
              <h3>Objetivos alcanzados</h3>
              <ul class="achieved-list">
                <li>&#x1F3C5; Meta de pasos completada 5 veces este mes</li>
                <li>&#x1F525; 300 kcal quemadas en una sesi&#xF3;n</li>
                <li>&#x23F1;&#xFE0F; 60 min de ejercicio continuo</li>
              </ul>
            </div>
          </div>
        </section>

        <!-- Barra de navegaci&#xF3;n inferior (secciones) -->
        <nav class="tv-tabbar" id="tabbar">
          <button data-panel="dashboard">Inicio</button>
          <button data-panel="stats">Estad&#xED;sticas</button>
          <button data-panel="activities">Actividades</button>
          <button data-panel="history">Historial</button>
          <button data-panel="goals">Objetivos</button>
        </nav>
      </div>
      <div class="tv-bezel"></div>
    </div>

    <!-- Control remoto en pantalla -->
    <div class="remote" title="Control remoto">
      <button class="r-btn r-up" data-dir="up">&#x25B2;</button>
      <button class="r-btn r-left" data-dir="left">&#x25C0;</button>
      <button class="r-btn r-ok" data-dir="ok">OK</button>
      <button class="r-btn r-right" data-dir="right">&#x25B6;</button>
      <button class="r-btn r-down" data-dir="down">&#x25BC;</button>
      <button class="r-btn r-back" data-dir="back">&#x2302; Inicio</button>
    </div>
  </main>

  <script src="js/tv.js"></script>
</body>
</html>
```

### 5.5 `tv/css/tv.css` (Smart TV)

```css
/* ============================================================
   FitLife — Smart TV
   Diseño adaptado a pantalla grande y control remoto:
   - navegación por FOCO (elemento resaltado), no por cursor
   - botones direccionales + botón Aceptar (Enter)
   - tipografía grande legible a distancia (10-2 m)
   - layout amplio: varias métricas visibles a la vez
   ============================================================ */

:root {
  --bg: #10141c;
  --screen-bg: #0a0e14;
  --text: #eef2f8;
  --text-dim: #98a3b5;
  --accent: #4f9cf7;
  --green: #34d399;
  --yellow: #fbbf24;
  --red: #f87171;
  --card: rgba(255, 255, 255, 0.06);
  --focus: #ffd54f;
}

* { box-sizing: border-box; margin: 0; padding: 0; }

body {
  background: var(--bg);
  color: var(--text);
  font-family: "Segoe UI", system-ui, sans-serif;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.page-header { text-align: center; padding: 20px 16px 8px; }
.page-header h1 { font-size: 1.3rem; }
.page-header p { color: var(--text-dim); font-size: 0.95rem; margin-top: 4px; }
.page-header a { color: var(--accent); text-decoration: none; }

kbd {
  background: rgba(255,255,255,0.12);
  border: 1px solid rgba(255,255,255,0.25);
  border-radius: 4px;
  padding: 1px 5px;
  font-size: 0.85em;
}

/* ---------- Marco físico de la TV ---------- */
.tv-stage {
  display: flex;
  gap: 28px;
  align-items: flex-start;
  padding: 12px 16px 40px;
  flex-wrap: wrap;
  justify-content: center;
}

.tv-body {
  background: #1b2029;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 14px 40px rgba(0,0,0,0.6);
}
.tv-bezel {
  height: 10px;
  background: #2a3140;
  border-radius: 0 0 10px 10px;
}

.tv-screen {
  width: min(960px, 92vw);
  aspect-ratio: 16 / 9;
  background: var(--screen-bg);
  border-radius: 8px;
  position: relative;
  overflow: hidden;
  border: 3px solid #2a3140;
}

/* ---------- Paneles ---------- */
.tv-screen-panel {
  position: absolute;
  inset: 0;
  display: none;
  flex-direction: column;
  padding: 28px 34px 70px;
  gap: 14px;
}
.tv-screen-panel.active { display: flex; }

.panel-title { font-size: 1.5rem; font-weight: 700; }
.panel-sub { color: var(--text-dim); font-size: 0.95rem; }

/* ---------- Sistema de FOCO (control remoto) ---------- */
[tabindex="0"] { outline: none; }

.focused {
  outline: 3px solid var(--focus);
  outline-offset: 3px;
  background: rgba(255, 213, 79, 0.10) !important;
  transform: scale(1.03);
  transition: transform 0.12s ease;
}

/* ---------- PANTALLA 1: Dashboard ---------- */
.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user { display: flex; align-items: center; gap: 12px; }
.avatar { font-size: 2.2rem; }
.user h2 { font-size: 1.35rem; }
.user small { color: var(--text-dim); }

.dash-goal-pill {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.15);
  border-radius: 999px;
  padding: 8px 18px;
  font-size: 1rem;
  cursor: pointer;
}
.dash-goal-pill strong { color: var(--green); }

.dash-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}
.card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 18px 10px;
  cursor: pointer;
}
.card-icon { font-size: 1.8rem; }
.card-value { font-size: 1.9rem; font-weight: 700; }
.card-label { font-size: 0.85rem; color: var(--text-dim); }

.dash-recent {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 14px 18px;
  cursor: pointer;
  flex: 1;
}
.dash-recent h3 { font-size: 1.05rem; margin-bottom: 8px; }
.dash-recent ul { list-style: none; }
.dash-recent li {
  font-size: 0.95rem;
  color: var(--text-dim);
  padding: 5px 0;
  border-bottom: 1px solid rgba(255,255,255,0.07);
}
.dash-recent li:last-child { border-bottom: none; }

/* ---------- PANTALLA 2: Estadísticas ---------- */
.charts {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  flex: 1;
}
.chart {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
}
.chart h3 { font-size: 1rem; margin-bottom: 12px; }

.chart-bars {
  flex: 1;
  display: flex;
  align-items: flex-end;
  gap: 8px;
}
.bar {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  gap: 4px;
  text-align: center;
}
.bar-fill {
  background: var(--accent);
  border-radius: 6px 6px 0 0;
  min-height: 4px;
}
.chart-cal .bar-fill { background: var(--yellow); }
.chart-time .bar-fill { background: var(--green); }
.bar-val { font-size: 0.75rem; color: var(--text-dim); }
.bar-day { font-size: 0.75rem; color: var(--text-dim); }

/* ---------- PANTALLA 3: Actividades ---------- */
.activity-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 18px;
  flex: 1;
  align-content: center;
}
.tv-activity {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.15);
  border-radius: 16px;
  color: var(--text);
  font-size: 1.15rem;
  font-weight: 600;
  padding: 28px 12px;
  cursor: pointer;
}
.act-icon { font-size: 2.6rem; }
.tv-activity.selected {
  border-color: var(--accent);
  background: rgba(79, 156, 247, 0.18);
}

.activity-selected {
  text-align: center;
  font-size: 1.1rem;
  color: var(--text-dim);
}
.activity-selected strong { color: var(--accent); }

/* ---------- PANTALLA 4: Historial ---------- */
.history-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  flex: 1;
}
.history-list {
  list-style: none;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 12px;
  padding: 12px 16px;
  cursor: pointer;
  font-size: 1rem;
}
.history-item .h-date { color: var(--text-dim); font-size: 0.85rem; }

.history-detail {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.detail-empty { color: var(--text-dim); }
.history-detail h3 { font-size: 1.2rem; }
.detail-row { display: flex; justify-content: space-between; font-size: 1rem; }
.detail-row span:first-child { color: var(--text-dim); }
.detail-row span:last-child { font-weight: 700; }

/* ---------- PANTALLA 5: Objetivos ---------- */
.goals-layout {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  flex: 1;
}
.goal-block {
  background: var(--card);
  border: 1px solid rgba(255,255,255,0.12);
  border-radius: 14px;
  padding: 18px;
  cursor: pointer;
}
.goal-block h3 { font-size: 1.1rem; margin-bottom: 12px; }
.goal-block p { color: var(--text-dim); margin-top: 8px; }

.progress {
  position: relative;
  height: 18px;
  background: rgba(255,255,255,0.1);
  border-radius: 9px;
  overflow: hidden;
}
.progress-bar {
  position: absolute;
  inset: 0 auto 0 0;
  width: 0%;
  background: var(--green);
  border-radius: 9px;
  transition: width 0.6s ease;
}
.progress-bar.weekly { background: var(--accent); }
.progress-text {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.8rem;
  font-weight: 700;
}

.achieved-list { list-style: none; }
.achieved-list li {
  font-size: 0.95rem;
  color: var(--text-dim);
  padding: 6px 0;
  border-bottom: 1px solid rgba(255,255,255,0.07);
}
.achieved-list li:last-child { border-bottom: none; }

/* ---------- Barra de pestañas (secciones) ---------- */
.tv-tabbar {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  gap: 8px;
  padding: 12px 34px;
  background: rgba(0,0,0,0.55);
}
.tv-tabbar button {
  flex: 1;
  background: transparent;
  border: 1px solid rgba(255,255,255,0.15);
  color: var(--text-dim);
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  padding: 10px 6px;
  cursor: pointer;
}
.tv-tabbar button.current {
  color: var(--text);
  border-color: var(--accent);
  background: rgba(79,156,247,0.15);
}

/* ---------- Control remoto ---------- */
.remote {
  display: grid;
  grid-template-columns: repeat(3, 64px);
  grid-template-rows: repeat(3, 64px);
  gap: 10px;
  background: #1b2029;
  border-radius: 20px;
  padding: 18px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.6);
}
.r-up    { grid-column: 2; grid-row: 1; }
.r-left  { grid-column: 1; grid-row: 2; }
.r-ok    { grid-column: 2; grid-row: 2; }
.r-right { grid-column: 3; grid-row: 2; }
.r-down  { grid-column: 2; grid-row: 3; }
.r-back  { grid-column: 1 / 4; grid-row: 3; }

.r-btn {
  background: #2a3140;
  border: none;
  border-radius: 50%;
  color: var(--text);
  font-size: 1.1rem;
  cursor: pointer;
}
.r-btn:active { background: #3a4356; }
.r-ok { background: var(--accent); font-weight: 700; }
.r-back { border-radius: 12px; font-size: 0.95rem; }
```

### 5.6 `tv/js/tv.js` (Smart TV)

```javascript
/* ============================================================
   FitLife — Smart TV
   Lógica de navegación por FOCO (control remoto):
   - flechas / control en pantalla mueven el foco
   - Enter / OK selecciona
   - datos simulados compartidos con la app (mismo sistema FitLife)
   ============================================================ */

// ---------- Datos simulados (mismo sistema FitLife) ----------
const user = { name: "María González" };

const week = [
  { day: "Lun", steps: 9200,  cal: 310, time: 45 },
  { day: "Mar", steps: 7400,  cal: 260, time: 30 },
  { day: "Mié", steps: 11300, cal: 380, time: 55 },
  { day: "Jue", steps: 6800,  cal: 240, time: 25 },
  { day: "Vie", steps: 10100, cal: 350, time: 50 },
  { day: "Sáb", steps: 12600, cal: 420, time: 70 },
  { day: "Dom", steps: 8450,  cal: 320, time: 42 },
];

const history = [
  { date: "Hoy, 18:00", activity: "Correr",        icon: "\u1F3C3", duration: "42 min", steps: 4200, cal: 320, hr: 148 },
  { date: "Hoy, 07:30", activity: "Caminar",       icon: "\u1F6B6", duration: "30 min", steps: 3100, cal: 140, hr: 92 },
  { date: "Ayer, 19:15", activity: "Bicicleta",    icon: "\u1F6B4", duration: "55 min", steps: 2800, cal: 380, hr: 132 },
  { date: "Ayer, 08:00", activity: "Entrenamiento",icon: "\u1F3CB\uFE0F", duration: "40 min", steps: 900,  cal: 290, hr: 155 },
  { date: "Mié, 18:30", activity: "Correr",        icon: "\u1F3C3", duration: "55 min", steps: 5600, cal: 430, hr: 158 },
  { date: "Mié, 07:00", activity: "Caminar",       icon: "\u1F6B6", duration: "25 min", steps: 2400, cal: 110, hr: 88 },
  { date: "Mar, 19:00", activity: "Bicicleta",    icon: "\u1F6B4", duration: "30 min", steps: 1500, cal: 210, hr: 120 },
];

const goals = {
  dailyTarget: 10000,
  weeklyTarget: 70000,
  todaySteps: 8450,
  weekSteps: week.reduce((a, d) => a + d.steps, 0),
};

// ---------- Paneles ----------
const panels = [
  { id: "dashboard",    name: "Inicio" },
  { id: "stats",        name: "Estadísticas" },
  { id: "activities",   name: "Actividades" },
  { id: "history",      name: "Historial" },
  { id: "goals",        name: "Objetivos" },
];
let currentPanel = 0;

const panelEls = panels.map(p => document.getElementById("panel-" + p.id));
const tabButtons = [...document.querySelectorAll(".tv-tabbar button")];

function switchPanel(index) {
  index = (index + panels.length) % panels.length;
  panelEls.forEach((el, i) => el.classList.toggle("active", i === index));
  tabButtons.forEach((b, i) => b.classList.toggle("current", i === index));
  currentPanel = index;
  focusElement(focusables()[0]);
}

tabButtons.forEach((btn, i) =>
  btn.addEventListener("click", () => switchPanel(i)));

// ---------- Sistema de FOCO (navegación espacial) ----------
let focusEl = null;

function focusables() {
  const panel = panelEls[currentPanel];
  const items = [...panel.querySelectorAll('[tabindex="0"], .tv-activity, .history-item')];
  return [...items, ...tabButtons];
}

function focusElement(el) {
  if (focusEl) focusEl.classList.remove("focused");
  focusEl = el;
  el.classList.add("focused");
}

function moveFocus(dir) {
  if (!focusEl) { focusElement(focusables()[0]); return; }
  const items = focusables();
  const r = focusEl.getBoundingClientRect();
  const cx = r.left + r.width / 2;
  const cy = r.top + r.height / 2;

  let best = null, bestScore = Infinity;
  for (const el of items) {
    if (el === focusEl) continue;
    const b = el.getBoundingClientRect();
    const ex = b.left + b.width / 2;
    const ey = b.top + b.height / 2;
    const dx = ex - cx, dy = ey - cy;

    // ¿Está en la dirección correcta?
    let forward, lateral;
    if (dir === "left")  { forward = -dx; lateral = dy; }
    if (dir === "right") { forward = dx;   lateral = dy; }
    if (dir === "up")    { forward = -dy; lateral = dx; }
    if (dir === "down")  { forward = dy;   lateral = dx; }
    if (forward <= 0) continue;

    // Penaliza desviación lateral y preferencia de filas/columnas
    const score = forward + Math.abs(lateral) * 2.5;
    if (score < bestScore) { bestScore = score; best = el; }
  }
  if (best) focusElement(best);
}

// ---------- Selección (Enter / OK) ----------
function activate(el) {
  if (el.closest(".tv-tabbar")) {
    switchPanel(tabButtons.indexOf(el));
    return;
  }
  const nav = el.dataset.nav;
  if (nav) {
    const idx = panels.findIndex(p => p.id === nav);
    if (idx >= 0) switchPanel(idx);
    return;
  }
  if (el.classList.contains("tv-activity")) {
    document.querySelectorAll(".tv-activity").forEach(b => b.classList.remove("selected"));
    el.classList.add("selected");
    document.getElementById("activity-name").textContent = el.dataset.activity;
    return;
  }
  if (el.classList.contains("history-item")) {
    const i = [...document.querySelectorAll(".history-item")].indexOf(el);
    showHistoryDetail(i);
    return;
  }
}

// ---------- Teclado (control remoto real) ----------
document.addEventListener("keydown", (e) => {
  switch (e.key) {
    case "ArrowUp":    e.preventDefault(); moveFocus("up"); break;
    case "ArrowDown":   e.preventDefault(); moveFocus("down"); break;
    case "ArrowLeft":   e.preventDefault(); moveFocus("left"); break;
    case "ArrowRight":  e.preventDefault(); moveFocus("right"); break;
    case "Enter":       e.preventDefault(); if (focusEl) activate(focusEl); break;
    case "Escape":      e.preventDefault(); switchPanel(0); break;
  }
});

// ---------- Control remoto en pantalla ----------
document.querySelectorAll(".r-btn").forEach((btn) => {
  btn.addEventListener("click", () => {
    const dir = btn.dataset.dir;
    if (dir === "ok") { if (focusEl) activate(focusEl); }
    else if (dir === "back") switchPanel(0);
    else moveFocus(dir);
  });
});

// ---------- Fecha ----------
document.getElementById("tv-date").textContent =
  new Date().toLocaleDateString("es", { weekday: "long", day: "numeric", month: "long" });

// ---------- Dashboard ----------
function renderDashboard() {
  const t = week[6];
  document.getElementById("dash-steps").textContent = t.steps.toLocaleString("es");
  document.getElementById("dash-cal").textContent = t.cal;
  document.getElementById("dash-hr").textContent = 72;
  document.getElementById("dash-goal").textContent = goals.dailyTarget.toLocaleString("es");
  const pct = Math.min(100, Math.round(t.steps / goals.dailyTarget * 100));
  document.getElementById("dash-goal-pct").textContent = pct + "%";

  const recent = document.getElementById("recent-list");
  recent.innerHTML = history.slice(0, 3).map(h =>
    `<li>${h.icon} ${h.activity} · ${h.duration} · ${h.date}</li>`).join("");
}

// ---------- Gráficas (barras SVG-free, puro CSS) ----------
function buildBars(containerId, values, unit = "") {
  const max = Math.max(...values);
  const el = document.getElementById(containerId);
  el.innerHTML = week.map((d, i) => {
    const v = values[i];
    const h = Math.max(6, (v / max) * 100);
    return `<div class="bar">
      <span class="bar-val">${v.toLocaleString("es")}${unit}</span>
      <div class="bar-fill" style="height:${h}%"></div>
      <span class="bar-day">${d.day}</span>
    </div>`;
  }).join("");
}

function renderCharts() {
  buildBars("chart-steps", week.map(d => d.steps));
  buildBars("chart-cal",   week.map(d => d.cal));
  buildBars("chart-time",  week.map(d => d.time), " min");
}

// ---------- Historial ----------
function renderHistory() {
  const list = document.getElementById("history-list");
  list.innerHTML = history.map((h, i) =>
    `<li class="history-item" tabindex="0" data-index="${i}">
       <span>${h.icon} ${h.activity}</span>
       <span class="h-date">${h.date}</span>
     </li>`).join("");
}

function showHistoryDetail(i) {
  const h = history[i];
  document.getElementById("history-detail").innerHTML = `
    <h3>${h.icon} ${h.activity}</h3>
    <div class="detail-row"><span>Fecha</span><span>${h.date}</span></div>
    <div class="detail-row"><span>Duración</span><span>${h.duration}</span></div>
    <div class="detail-row"><span>Pasos</span><span>${h.steps.toLocaleString("es")}</span></div>
    <div class="detail-row"><span>Calorías</span><span>${h.cal} kcal</span></div>
    <div class="detail-row"><span>FC promedio</span><span>${h.hr} bpm</span></div>`;
}

// ---------- Objetivos ----------
function renderGoals() {
  const dpct = Math.min(100, Math.round(goals.todaySteps / goals.dailyTarget * 100));
  const wpct = Math.min(100, Math.round(goals.weekSteps / goals.weeklyTarget * 100));
  document.getElementById("daily-bar").style.width = dpct + "%";
  document.getElementById("daily-pct").textContent = dpct + "%";
  document.getElementById("goal-steps").textContent = goals.todaySteps.toLocaleString("es");
  document.getElementById("goal-target").textContent = goals.dailyTarget.toLocaleString("es");
  document.getElementById("weekly-bar").style.width = wpct + "%";
  document.getElementById("weekly-pct").textContent = wpct + "%";
  document.getElementById("weekly-steps").textContent = goals.weekSteps.toLocaleString("es");
  document.getElementById("weekly-target").textContent = goals.weeklyTarget.toLocaleString("es");
}

// ---------- Inicialización ----------
renderDashboard();
renderCharts();
renderHistory();
renderGoals();
switchPanel(0);
```

---

*Fin del reporte.*
