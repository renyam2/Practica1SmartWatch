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
  { date: "Hoy, 18:00", activity: "Correr",        icon: "🏃", duration: "42 min", steps: 4200, cal: 320, hr: 148 },
  { date: "Hoy, 07:30", activity: "Caminar",       icon: "🚶", duration: "30 min", steps: 3100, cal: 140, hr: 92 },
  { date: "Ayer, 19:15", activity: "Bicicleta",    icon: "🚴", duration: "55 min", steps: 2800, cal: 380, hr: 132 },
  { date: "Ayer, 08:00", activity: "Entrenamiento",icon: "🏋️", duration: "40 min", steps: 900,  cal: 290, hr: 155 },
  { date: "Mié, 18:30", activity: "Correr",        icon: "🏃", duration: "55 min", steps: 5600, cal: 430, hr: 158 },
  { date: "Mié, 07:00", activity: "Caminar",       icon: "🚶", duration: "25 min", steps: 2400, cal: 110, hr: 88 },
  { date: "Mar, 19:00", activity: "Bicicleta",    icon: "🚴", duration: "30 min", steps: 1500, cal: 210, hr: 120 },
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
