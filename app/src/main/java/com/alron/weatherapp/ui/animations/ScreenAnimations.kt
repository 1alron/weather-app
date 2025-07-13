package com.alron.weatherapp.ui.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

const val TWEEN_DURATION = 700

fun enterSlideInFromRight(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TWEEN_DURATION)
    ) + fadeIn(animationSpec = tween(TWEEN_DURATION))
}

fun exitSlideOutToLeft(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(TWEEN_DURATION)
    ) + fadeOut(animationSpec = tween(TWEEN_DURATION))
}

fun enterSlideInFromLeft(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(TWEEN_DURATION)
    ) + fadeIn(animationSpec = tween(TWEEN_DURATION))
}

fun exitSlideOutToRight(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TWEEN_DURATION)
    ) + fadeOut(animationSpec = tween(TWEEN_DURATION))
}