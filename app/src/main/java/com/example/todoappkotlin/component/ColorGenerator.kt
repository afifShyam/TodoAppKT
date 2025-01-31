package com.example.todoappkotlin.component

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

// Generate Random Color in ARGB Int format
fun generateRandColorInt(): Int {
    var color: Int
    do {
        val red = Random.nextInt(50, 256)
        val green = Random.nextInt(0, 100)
        val blue = Random.nextInt(50, 256)

        color = android.graphics.Color.argb(255, red, green, blue)
    } while (green > red && green > blue)

    return color
}



// Convert stored Int color to Jetpack Compose Color
fun Int.toComposeColor(): Color {
    val red = android.graphics.Color.red(this)
    val green = android.graphics.Color.green(this)
    val blue = android.graphics.Color.blue(this)
    val alpha = android.graphics.Color.alpha(this)



    return Color(
        red = red / 255f,
        green = green / 255f,
        blue = blue / 255f,
        alpha = alpha / 255f
    )
}

fun getGradientBrush(startColor: Int, endColor: Int): Brush {
    return Brush.linearGradient(
        colors = listOf(
            startColor.toComposeColor(),
            endColor.toComposeColor()
        )
    )
}