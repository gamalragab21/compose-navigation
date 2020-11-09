package com.koduok.compose.navigation.sample.examples

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.koduok.compose.navigation.AmbientBackStack
import com.koduok.compose.navigation.Router
import com.koduok.compose.navigation.core.RouteDescription

@Composable
fun TranslucentScreen() {
    Router("A", start = TranslucentRoute(0, 1f)) {
        TranslucentScreen(value = it.data.value, opacity = it.data.opacity) {
            val newOpacity = when {
                it.data.opacity <= 0.3f -> 1f
                it.data.opacity <= 0.6f -> 0.3f
                else -> 0.6f
            }
            push(RouteDescription(TranslucentRoute(it.data.value + 1, newOpacity), showRouteBelow = newOpacity < 1f))
        }
    }
}

data class TranslucentRoute(val value: Int, val opacity: Float)

@Composable
fun TranslucentScreen(value: Int, opacity: Float, onNext: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().drawLayer(alpha = opacity).clickable(onClick = onNext)) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = "${AmbientBackStack.current.key.id}$value", style = MaterialTheme.typography.h1.copy(
                color = when {
                    opacity <= 0.3f -> Color.Blue
                    opacity <= 0.6f -> Color.Red
                    else -> Color.Black
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}