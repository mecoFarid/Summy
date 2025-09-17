package com.mecofarid.summy.common.view.ui.ext

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.layout
import kotlinx.coroutines.launch

fun Modifier.squareAspectRatio() =
    layout { measurable, constraints ->
        // Measure the composable
        val placeable = measurable.measure(constraints)

        //get the current max dimension to assign width=height
        val height = placeable.height
        val width = placeable.width
        val size = maxOf(height, width)

        //assign the dimension and the center position
        layout(size, size) {
            // Where the composable gets placed
            placeable.placeRelative((size - width) / 2, (size - height) / 2)
        }
    }

fun Modifier.bounce(bounceWhenChanged: Any, maxScale: Float) = composed {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(initialValue = 1f) }

    LaunchedEffect(key1 = bounceWhenChanged) {
        coroutineScope.launch {
            val animationSpec = spring<Float>(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium)
            scale.animateTo(
                targetValue = maxScale,
                animationSpec = animationSpec
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = animationSpec
            )
        }
    }

    scale(scale.value)
}

fun Modifier.clickableWithoutIndication(onClick: () -> Unit) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource,
        indication = null,
        onClick = onClick
    )
}