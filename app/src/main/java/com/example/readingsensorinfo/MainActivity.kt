package com.example.readingsensorinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readingsensorinfo.ui.theme.ReadingSensorInfoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadingSensorInfoTheme {
                val viewModel = viewModel<MainViewModel>()
                val orientation = viewModel.orientation.collectAsState()
                val blendFactor = viewModel.blendFactor.collectAsState()
                val dataHashMap = viewModel.dataHashMap.collectAsState()
                dataHashMap.value["x-axis"]?.let { LineGraph(dataPoints = it.toList(), color = Color.Red) }
                dataHashMap.value["y-axis"]?.let { LineGraph(dataPoints = it.toList(), color = Color.Blue) }
                dataHashMap.value["z-axis"]?.let { LineGraph(dataPoints = it.toList(), color = Color.Green) }
                Circles(orientation.value, blendFactor.value)

            }
        }
    }
}

@Composable
fun Circles(value: List<Float>, blendFactor: List<Float>) {
    val baseColor1 = Color.Yellow
    val baseColor2 = Color.Blue

    Canvas(modifier = Modifier.size(400.dp), onDraw = {
        drawCircle(color = blendColor(color1 = baseColor1, color2 = baseColor2, fraction = blendFactor[0]), radius = value[0], center = Offset(size.width / 2f, size.height / 2f))
        drawCircle(color = blendColor(color1 = baseColor1, color2 = baseColor2, fraction = blendFactor[1]), radius = value[1], center = Offset(size.width * 0.25f, size.height * 0.25f))
        drawCircle(color = blendColor(color1 = baseColor1, color2 = baseColor2, fraction = blendFactor[2]), radius = value[2], center = Offset(size.width * 0.75f, size.height * 0.25f))
    })
}

@Composable
fun LineGraph(dataPoints: List<Float>, color: Color) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val padding = 16.dp.toPx()
        val graphWidth = canvasWidth - padding * 2
        val graphHeight = canvasHeight - padding * 2

        val maxYValue = 360f
        val minYValue = 0f

        val yValueRange = maxYValue - minYValue

        val dataPointCount = dataPoints.size
        if (dataPointCount > 1) {
            val xStep = graphWidth / (dataPointCount - 1)

            for (i in 0 until dataPointCount - 1) {
                val x1 = padding + xStep * i
                val x2 = padding + xStep * (i + 1)

                val y1 = canvasHeight - padding - (dataPoints[i] - minYValue) / yValueRange * graphHeight
                val y2 = canvasHeight - padding - (dataPoints[i + 1] - minYValue) / yValueRange * graphHeight

                drawLine(
                    color = color,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 4f
                )
            }
        }
    }
}


fun blendColor(color1: Color, color2: Color, fraction: Float): Color {
    val r = color1.red * (1 - fraction) + color2.red * fraction
    val g = color1.green * (1 - fraction) + color2.green * fraction
    val b = color1.blue * (1 - fraction) + color2.blue * fraction
    return Color(r, g, b)
}



