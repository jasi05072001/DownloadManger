package com.jasmeet.downloadmanger.appComponent

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jasmeet.downloadmanger.R
import kotlinx.coroutines.launch

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress :Float,
    onClick: () -> Unit = {},
    isAnimating : Boolean = false
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
                    .then(
                        if (isAnimating) {
                            Modifier.graphicsLayer(
                                translationY = animateFloatAsState(
                                    targetValue = anim * 20f,
                                    animationSpec = tween(
                                        durationMillis = 400,
                                        easing = LinearEasing
                                    ), label = ""
                                ).value,
                            )
                        } else {
                            Modifier
                        }

                    ),
                tint = Color.White
            )
        }
    }
}

@Composable
fun OutlinedCardComponent(
    testTag :String,
    text:String,
    onClick: () -> Unit = {}
) {
    androidx.compose.material3.OutlinedCard(
        modifier = Modifier
            .testTag(testTag)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick.invoke()
            }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(20.dp)
        )
    }

}

@Composable
fun VerticalSpace(
    height : Int
) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun HorizontalSpace(
    width : Int
) {
    Spacer(modifier = Modifier.width(width.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadItemComponent(
    onClick: () -> Unit,
    artWorkUri :String,
    title :String,
    onDownloadClick : () -> Unit,
    cardClickTestTag: String,
    downloadClickTextTag: String,
    context:Context

) {

    OutlinedCard(
        onClick= {
            onClick.invoke()
        },
        modifier = Modifier
            .testTag(cardClickTestTag)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    ){

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(artWorkUri)
                    .crossfade(true)
                    .build(),
                onError = {error ->
                    Log.d("error", error.result.throwable.message.toString())
                },
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(120.dp, 90.dp),
                contentScale = ContentScale.FillBounds
            )


            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                softWrap = true,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.testTag(downloadClickTextTag),
                onClick = {
                    onDownloadClick.invoke()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.offline),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            HorizontalSpace(width = 10)

        }
    }

}