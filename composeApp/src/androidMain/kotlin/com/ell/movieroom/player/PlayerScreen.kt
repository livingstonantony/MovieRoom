package com.ell.movieroom.player

import android.content.pm.ActivityInfo
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import com.ell.movieroom.LocalAppContainer
import com.ell.movieroom.chat.ChatScreen
import com.ell.movieroom.presentation.devices.DeviceViewModel
import com.ell.movieroom.utils.findActivity
import com.ell.movieroom.utils.toVideoTimeRounded
import com.ell.movieroom.viewmodel.MainViewModel

@Composable
fun VideoPlayerScreen(
    viewModel: MainViewModel = viewModel(
        factory = LocalAppContainer.current.mainViewModelFactory,
    ),
    devicesViewModel: DeviceViewModel = viewModel(
        factory = LocalAppContainer.current.deviceViewModel
    )
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentDuration by viewModel.currentPosition.collectAsState()
    val duration by viewModel.durationMs.collectAsState()


    val context = LocalContext.current
    val activity = remember { context.findActivity() }

    var isLandscape by rememberSaveable { mutableStateOf(false) }

    // 🔥 Sync with ExoPlayer controls
    var areSystemControlsVisible by remember { mutableStateOf(true) }

    val pickMedia =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { viewModel.setVideo(it) }
        }

    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        val (video, controls) = createRefs()

        // ───────── VIDEO ─────────
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = viewModel.player
                    useController = true

                    setControllerVisibilityListener(
                        PlayerView.ControllerVisibilityListener { visibility ->
                            areSystemControlsVisible = visibility == View.VISIBLE
                        }
                    )
                }
            },
            update = { playerView ->
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        playerView.onPause()
                        playerView.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        playerView.onResume()
                    }

                    else -> Unit
                }
            },
            modifier = Modifier
                .constrainAs(video) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)

                    if (isLandscape) {
                        end.linkTo(
                            if (areSystemControlsVisible) controls.start
                            else parent.end
                        )
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    } else {
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.ratio("16:9")
                    }
                }
                .background(Color.Black.copy(alpha = 0.9f))
        )

        // ───────── CONTROLS (RIGHT SIDE / BOTTOM) ─────────
        AnimatedVisibility(
            visible = areSystemControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.constrainAs(controls) {

                if (isLandscape) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.value(260.dp)
                } else {
                    top.linkTo(video.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            }
        ) {
            ChatScreen(
                devicesViewModel = devicesViewModel,

                isPlaying = isPlaying,
                isLandscape = isLandscape,
                onPickVideo = {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                },
                onPlayPause = {
                    if (isPlaying) viewModel.pause()
                    else viewModel.play()
                },
                onSeek = { viewModel.seekTo() },
                onToggleOrientation = {
                    isLandscape = !isLandscape
                    activity.requestedOrientation =
                        if (isLandscape)
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                },
                duration = "${currentDuration.toVideoTimeRounded()}/${duration.toVideoTimeRounded()}"
            )
        }
    }
}



