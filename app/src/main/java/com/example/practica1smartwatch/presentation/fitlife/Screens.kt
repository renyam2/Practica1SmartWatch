package com.example.practica1smartwatch.presentation.fitlife

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.LazyListScope
import androidx.wear.compose.foundation.lazy.LazyTransformationSpec
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Surface
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// ---------------------------------------------------------------------------
// Pantalla 1 — Inicio
// ---------------------------------------------------------------------------

@Composable
fun HomeScreen(
    scope: LazyListScope,
    data: FitLifeData,
    transformationSpec: LazyTransformationSpec,
    goTo: (Int) -> Unit,
) {
    val t = transformationSpec
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ListHeader(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            transformation = SurfaceTransformation(t),
        ) {
            Text("FitLife")
        }

        // Hora (reloj en vivo)
        ClockText(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        )

        // Métricas principales: pasos, frecuencia cardiaca y calorías
        Row(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MetricCard("Pasos", "👣", formatSteps(data.steps))
            MetricCard("Frecuencia", "❤️", "${data.heartRate} ppm")
            MetricCard("Calorías", "🔥", "${data.calories} kcal")
        }

        // Progreso del objetivo diario
        val goalFraction = data.steps.toFloat() / data.goalSteps
        Row(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(
                progress = { goalFraction },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Column {
                Text(
                    text = "Objetivo diario",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "${(goalFraction * 100).roundToInt()}% completado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // Accesos a las demás pantallas (un toque por destino)
        Row(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = { goTo(SCREEN_HEART_RATE) }) { Text("Cardio") }
            Button(onClick = { goTo(SCREEN_ACTIVITY) }) { Text("Actividad") }
            Button(onClick = { goTo(SCREEN_GOAL) }) { Text("Objetivo") }
            Button(onClick = { goTo(SCREEN_NOTIFICATIONS) }) { Text("Alertas") }
        }
    }
}

@Composable
private fun ClockText(modifier: Modifier = Modifier) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            now = LocalDateTime.now()
        }
    }
    Text(
        text = now.format(DateTimeFormatter.ofPattern("HH:mm")),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
    )
}

@Composable
private fun MetricCard(label: String, icon: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f),
    ) {
        Text(text = icon, style = MaterialTheme.typography.titleMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------
// Pantalla 2 — Frecuencia cardiaca
// ---------------------------------------------------------------------------

@Composable
fun HeartRateScreen(
    scope: LazyListScope,
    data: FitLifeData,
    transformationSpec: LazyTransformationSpec,
    goTo: (Int) -> Unit,
) {
    val t = transformationSpec
    // Posición de la frecuencia actual dentro del rango [min, max]
    val fraction = (data.heartRate - data.minHeartRate)
        .toFloat() / (data.maxHeartRate - data.minHeartRate)
    val zone = when {
        data.heartRate < 90 -> "Zona de reposo"
        data.heartRate < 120 -> "Zona moderada"
        else -> "Zona intensa"
    }
    val zoneColor = when {
        data.heartRate < 90 -> Color(0xFF2E7D32) // verde
        data.heartRate < 120 -> Color(0xFFF9A825) // ámbar
        else -> Color(0xFFD32F2F) // rojo
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ListHeader(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            transformation = SurfaceTransformation(t),
        ) {
            Text("Frecuencia cardiaca")
        }

        // Frecuencia actual (dato principal, lo más grande)
        Column(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${data.heartRate} ppm",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = zoneColor,
            )
            Text(
                text = zone,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        // Indicador visual: barra con la posición dentro del rango min–max
        Row(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeartRateBar(fraction = fraction, color = zoneColor)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Mín: ${data.minHeartRate}",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "Máx: ${data.maxHeartRate}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Button(
            onClick = { goTo(SCREEN_HOME) },
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        ) {
            Text("Volver al inicio")
        }
    }
}

@Composable
private fun HeartRateBar(fraction: Float, color: Color) {
    Canvas(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Fondo del rango completo
        drawRoundRect(color = MaterialTheme.colorScheme.surfaceVariant)
        // Porción recorrida por la frecuencia actual
        drawRoundRect(
            color = color,
            right = size.width * fraction,
        )
    }
}

// ---------------------------------------------------------------------------
// Pantalla 3 — Actividad
// ---------------------------------------------------------------------------

@Composable
fun ActivityScreen(
    scope: LazyListScope,
    selectedActivity: ActivityType,
    onSelect: (ActivityType) -> Unit,
    transformationSpec: LazyTransformationSpec,
    goTo: (Int) -> Unit,
) {
    val t = transformationSpec
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ListHeader(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            transformation = SurfaceTransformation(t),
        ) {
            Text("Actividad")
        }

        // Actividad seleccionada (confirmación visible)
        Surface(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${selectedActivity.icon} ${selectedActivity.label}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Actividad en curso",
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }

        // Selección con un solo toque por opción
        ActivityType.entries.forEach { activity ->
            val isSelected = activity == selectedActivity
            Button(
                onClick = { onSelect(activity) },
                modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
                colors =
                    if (isSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
            ) {
                Text("${activity.icon}  ${activity.label}")
            }
        }

        Button(
            onClick = { goTo(SCREEN_HOME) },
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        ) {
            Text("Volver al inicio")
        }
    }
}

// ---------------------------------------------------------------------------
// Pantalla 4 — Objetivo diario
// ---------------------------------------------------------------------------

@Composable
fun GoalScreen(
    scope: LazyListScope,
    data: FitLifeData,
    transformationSpec: LazyTransformationSpec,
    goTo: (Int) -> Unit,
) {
    val t = transformationSpec
    val fraction = data.steps.toFloat() / data.goalSteps
    val percent = (fraction * 100).roundToInt()
    val remaining = (data.goalSteps - data.steps).coerceAtLeast(0)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ListHeader(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            transformation = SurfaceTransformation(t),
        ) {
            Text("Objetivo diario")
        }

        // Porcentaje completado dentro del anillo
        Column(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                progress = { fraction },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
        }

        // Meta de pasos y pasos actuales
        Row(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatSteps(data.goalSteps),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Meta de pasos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatSteps(data.steps),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Pasos actuales",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Text(
            text = "Te faltan ${formatSteps(remaining)} pasos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        )

        Button(
            onClick = { goTo(SCREEN_HOME) },
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        ) {
            Text("Volver al inicio")
        }
    }
}

// ---------------------------------------------------------------------------
// Pantalla 5 — Notificaciones
// ---------------------------------------------------------------------------

@Composable
fun NotificationsScreen(
    scope: LazyListScope,
    transformationSpec: LazyTransformationSpec,
    goTo: (Int) -> Unit,
) {
    val t = transformationSpec
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ListHeader(
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
            transformation = SurfaceTransformation(t),
        ) {
            Text("Notificaciones")
        }

        sampleNotifications.forEach { notification ->
            Surface(
                modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = notification.time,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        Button(
            onClick = { goTo(SCREEN_HOME) },
            modifier = Modifier.fillMaxWidth().transformedHeight(scope, t),
        ) {
            Text("Volver al inicio")
        }
    }
}

// ---------------------------------------------------------------------------
// Utilidades
// ---------------------------------------------------------------------------

private fun formatSteps(steps: Int): String =
    steps.toString().replace(",", ".")

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@WearPreviewDevices
@WearPreviewFontScales
@Composable
private fun FitLifeAppPreview() {
    FitLifeApp()
}
