package com.koduok.compose.navigation.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.onActive
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.ScrollableColumn
import androidx.ui.foundation.ScrollableRow
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.height
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.koduok.compose.navigation.Router
import com.koduok.compose.navigation.core.BackStackController
import com.koduok.compose.navigation.core.GlobalRoute
import com.koduok.compose.navigation.core.Route
import com.koduok.compose.navigation.core.backStackController
import com.koduok.compose.navigation.sample.AppRoute.BottomTabsRoute
import com.koduok.compose.navigation.sample.AppRoute.ComplexRoute
import com.koduok.compose.navigation.sample.AppRoute.HomeRoute
import com.koduok.compose.navigation.sample.AppRoute.MultipleStartRoute
import com.koduok.compose.navigation.sample.AppRoute.SimpleRoute
import com.koduok.compose.navigation.sample.AppRoute.SplitScreenRoute
import com.koduok.compose.navigation.sample.AppRoute.TranslucentRoute
import com.koduok.compose.navigation.sample.examples.BottomTabsScreen
import com.koduok.compose.navigation.sample.examples.ComplexScreen
import com.koduok.compose.navigation.sample.examples.MultipleStartScreen
import com.koduok.compose.navigation.sample.examples.SimpleScreen
import com.koduok.compose.navigation.sample.examples.SplitScreen
import com.koduok.compose.navigation.sample.examples.TranslucentScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SampleApp()
            }
        }
    }

    override fun onBackPressed() {
        if (!backStackController.pop()) super.onBackPressed()
    }
}

@Composable
fun SampleApp() {
    Column {
        BackStackVisualizer()
        Router<AppRoute>("Root", HomeRoute) {
            when (it.data) {
                HomeRoute -> HomeScreen { appRoute -> push(appRoute) }
                SimpleRoute -> SimpleScreen()
                MultipleStartRoute -> MultipleStartScreen()
                BottomTabsRoute -> BottomTabsScreen()
                SplitScreenRoute -> SplitScreen()
                ComplexRoute -> ComplexScreen()
                TranslucentRoute -> TranslucentScreen()
            }
        }
    }
}

@Composable
fun BackStackVisualizer() {
    val globalRoutes = state { backStackController.snapshot }
    onActive {
        val listener = object : BackStackController.Listener {
            override fun onBackStackChanged(snapshot: List<GlobalRoute>) {
                globalRoutes.value = snapshot
            }
        }
        backStackController.addListener(listener)
        onDispose {
            backStackController.removeListener(listener)
        }
    }

    Box(modifier = Modifier.height(144.dp)) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.primary) {
            ScrollableColumn(modifier = Modifier.fillMaxSize(), children = {
                ScrollableRow(modifier = Modifier.fillMaxSize(), children = {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = buildBackStackVisual(globalRoutes.value), style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                    )
                })

            })

        }
    }
}

private fun buildBackStackVisual(globalRoutes: List<GlobalRoute>): String {
    return globalRoutes
        .map { globalRoute ->
            globalRoute.snapshot.filter { it.key.id != "Root" }.sortedBy { it.key.parentCount }.map { it.name }
        }
        .reversed()
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")
}

private val Route<*>.name: String get() = "${this.key.id}${this.data}"

@Preview
@Composable
fun DefaultPreview() {
    AppTheme {
        SampleApp()
    }
}