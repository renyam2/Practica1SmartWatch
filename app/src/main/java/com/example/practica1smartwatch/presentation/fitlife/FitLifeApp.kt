package com.example.practica1smartwatch.presentation.fitlife

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import com.example.practica1smartwatch.presentation.theme.Practica1SmartWatchTheme

/**
 * FitLife — versión Smartwatch (Wear OS).
 *
 * Decisiones de diseño adaptadas al dispositivo:
 *  - Una sola columna vertical desplazable (TransformingLazyColumn) con 5
 *    "pantallas" completas: en un reloj, el desplazamiento vertical es el
 *    gesto natural (pulgar sobre la muñeca).
 *  - Cada pantalla ocupa el alto total de la pantalla: el usuario nunca
 *    necesita leer dos pantallas a la vez.
 *  - Botones grandes y de un solo toque; sin menús desplegables ni
 *    interacciones de precisión (puntero, arrastre fino).
 *  - El botón de borde ("Inicio") siempre regresa a la pantalla principal,
 *    aprovechando el botón físico del reloj como navegación de "atrás".
 */

const val SCREEN_HOME = 0
const val SCREEN_HEART_RATE = 1
const val SCREEN_ACTIVITY = 2
const val SCREEN_GOAL = 3
const val SCREEN_NOTIFICATIONS = 4

@Composable
fun FitLifeApp() {
    Practica1SmartWatchTheme {
        val data = remember { FitLifeData() }
        var selectedActivity by remember { mutableStateOf(ActivityType.CAMINAR) }

        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            val transformationSpec = rememberTransformationSpec()
            val goTo: (Int) -> Unit = { listState.scrollToItem(it) }

            ScreenScaffold(
                scrollState = listState,
                edgeButton = {
                    EdgeButton(
                        onClick = { goTo(SCREEN_HOME) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            ),
                    ) {
                        Text("Inicio")
                    }
                },
            ) { contentPadding ->
                TransformingLazyColumn(
                    state = listState,
                    contentPadding = contentPadding,
                ) {
                    item(key = "home") {
                        val scope = this
                        HomeScreen(
                            scope = scope,
                            data = data,
                            transformationSpec = transformationSpec,
                            goTo = goTo,
                        )
                    }
                    item(key = "heartRate") {
                        val scope = this
                        HeartRateScreen(
                            scope = scope,
                            data = data,
                            transformationSpec = transformationSpec,
                            goTo = goTo,
                        )
                    }
                    item(key = "activity") {
                        val scope = this
                        ActivityScreen(
                            scope = scope,
                            selectedActivity = selectedActivity,
                            onSelect = { selectedActivity = it },
                            transformationSpec = transformationSpec,
                            goTo = goTo,
                        )
                    }
                    item(key = "goal") {
                        val scope = this
                        GoalScreen(
                            scope = scope,
                            data = data,
                            transformationSpec = transformationSpec,
                            goTo = goTo,
                        )
                    }
                    item(key = "notifications") {
                        val scope = this
                        NotificationsScreen(
                            scope = scope,
                            transformationSpec = transformationSpec,
                            goTo = goTo,
                        )
                    }
                }
            }
        }
    }
}
