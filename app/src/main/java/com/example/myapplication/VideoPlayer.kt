package com.example.myapplication

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.TextoSecundario

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    @RawRes videoResId: Int? = null,
    isLooping: Boolean = true,
    isMuted: Boolean = false
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val videoShape = RoundedCornerShape(24.dp)
    val videoBorder = remember(colors.primary, colors.tertiary) {
        Brush.linearGradient(
            listOf(
                colors.tertiary.copy(alpha = 0.34f),
                Color.White.copy(alpha = 0.9f),
                colors.primary.copy(alpha = 0.38f),
                OuroChampanhe.copy(alpha = 0.52f)
            )
        )
    }
    var isPlaying by remember { mutableStateOf(true) }

    if (videoResId != null) {
        val exoPlayer = remember(videoResId) {
            ExoPlayer.Builder(context).build().apply {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build()
                setAudioAttributes(audioAttributes, true)

                val uri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                setMediaItem(MediaItem.fromUri(uri))
                repeatMode = if (isLooping) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                volume = if (isMuted) 0f else 1f
                playWhenReady = true
                prepare()
            }
        }

        DisposableEffect(exoPlayer) {
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }
            }
            exoPlayer.addListener(listener)
            onDispose {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
            }
        }

        // Card de vídeo com recorte, fundo e contorno derivados do tema
        Box(
            modifier = modifier
                .shadow(
                    elevation = 10.dp,
                    shape = videoShape,
                    ambientColor = Color(0x22000000),
                    spotColor = colors.primary.copy(alpha = 0.20f)
                )
                .clip(videoShape)
                .background(Color.White)
                .border(width = 1.dp, brush = videoBorder, shape = videoShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (exoPlayer.isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                }
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                        setBackgroundColor(android.graphics.Color.WHITE)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { view ->
                    view.player = exoPlayer
                    view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    view.setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                    view.setBackgroundColor(android.graphics.Color.WHITE)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.surfaceContainerLowest)
            )

            // Indicador sutil de Pause/Play no centro que aparece suavemente quando pausado
            AnimatedVisibility(
                visible = !isPlaying,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(colors.secondaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Vídeo pausado. Toque para reproduzir",
                        tint = colors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .clip(videoShape)
                .background(colors.surfaceContainerLowest),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Vídeo não especificado",
                color = TextoSecundario,
                fontSize = 13.sp
            )
        }
    }
}
