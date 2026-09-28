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
