package com.example.practica1smartwatch.presentation.fitlife

/**
 * Modelo de datos del sistema FitLife.
 * En una app real estos valores vendrían de sensores del reloj
 * (acelerómetro, sensor de frecuencia cardiaca) y de una API.
 */
data class FitLifeData(
    val steps: Int = 4820,
    val goalSteps: Int = 8000,
    val heartRate: Int = 72,
    val minHeartRate: Int = 58,
    val maxHeartRate: Int = 143,
    val calories: Int = 310,
    val exerciseMinutes: Int = 34,
)

enum class ActivityType(val label: String, val icon: String) {
    CAMINAR("Caminar", "🚶"),
    CORRER("Correr", "🏃"),
    BICICLETA("Bicicleta", "🚴"),
    ENTRENAMIENTO("Entrenamiento", "🏋️"),
}

data class FitNotification(
    val title: String,
    val message: String,
    val time: String,
)

val sampleNotifications = listOf(
    FitNotification(
        title = "🎯 Objetivo alcanzado",
        message = "¡Completaste el 100% de tus pasos de ayer!",
        time = "23:58",
    ),
    FitNotification(
        title = "⏰ Recordatorio de actividad",
        message = "Llevas 40 min sin moverte. ¡Sal a caminar!",
        time = "12:30",
    ),
    FitNotification(
        title = "📊 Resumen de actividad",
        message = "Ayer: 9,420 pasos · 412 kcal · 42 min de ejercicio.",
        time = "07:00",
    ),
)
