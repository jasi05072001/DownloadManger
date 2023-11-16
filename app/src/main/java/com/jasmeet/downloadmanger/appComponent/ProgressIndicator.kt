package com.jasmeet.downloadmanger.appComponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.jasmeet.downloadmanger.R
import kotlinx.coroutines.launch

@Composable
fun ProgressIndicator(
    progress :Float,
    onClick: () -> Unit = {}
) {


    val animatedProgress = remember { Animatable(initialValue = progress) }
    val coroutineScope = rememberCoroutineScope()

    val animationSpec = remember {
        TweenSpec<Float>(
            durationMillis = 0,
            easing = FastOutSlowInEasing
        )
    }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            progress,
            animationSpec = animationSpec
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            coroutineScope.launch {
                animatedProgress.stop()
            }
        }
    }


    val transition = rememberInfiniteTransition(label = "")
    val anim by transition.animateFloat(
        initialValue = -2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box (contentAlignment = Alignment.Center, modifier = Modifier
        .clip(CircleShape)
        .background(Color.Transparent)
        .clickable {
            onClick()
        }
    ){
        CircularProgressIndicator(
            progress = animatedProgress.value,
            modifier = Modifier.size(36.dp),
            trackColor = Color.White.copy(alpha = 0.7f),
            color = Color.Red,
        )
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.vector),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .padding(6.dp)
                    .graphicsLayer(
                        translationY = animateFloatAsState(
                            targetValue = anim * 20f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = LinearEasing
                            ), label = ""
                        ).value,
                    ),
                tint = Color.White
            )
        }
    }
}