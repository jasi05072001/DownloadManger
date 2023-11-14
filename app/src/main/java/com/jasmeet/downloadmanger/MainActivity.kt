package com.jasmeet.downloadmanger

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jasmeet.downloadmanger.screens.ARTWORK_URL
import com.jasmeet.downloadmanger.screens.DESCRIPTION
import com.jasmeet.downloadmanger.screens.DRM_LICENCE_URL
import com.jasmeet.downloadmanger.screens.DownloadScreen
import com.jasmeet.downloadmanger.screens.DrmScreen
import com.jasmeet.downloadmanger.screens.HlsScreen
import com.jasmeet.downloadmanger.screens.HomeScreen
import com.jasmeet.downloadmanger.screens.MIME_TYPE
import com.jasmeet.downloadmanger.screens.Mp4Screen
import com.jasmeet.downloadmanger.screens.OfflineVideoPlayerScreen
import com.jasmeet.downloadmanger.screens.Screens
import com.jasmeet.downloadmanger.screens.TITLE
import com.jasmeet.downloadmanger.screens.VIDEO_URL
import com.jasmeet.downloadmanger.screens.VideoPlayerScreen
import com.jasmeet.downloadmanger.ui.theme.DownloadMangerTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestPermissionLauncher =
        this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            DownloadMangerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}


@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ){
        composable(route = Screens.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(
            route =Screens.DownloadScreen.route,
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
                fadeOut()+ slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            }
        ){
            DownloadScreen(navController = navController)
        }

        composable(
            route =Screens.DrmScreen.route,
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
                fadeOut()+ slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            }
        ){
            DrmScreen(navController = navController)
        }
        composable(
            route =Screens.HlsScreen.route,
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
                fadeOut() + slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            }
        ){
            HlsScreen(navController = navController)
        }
        composable(
           route = Screens.Mp4Screen.route,
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
                fadeOut() + slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
        ){
            Mp4Screen(navController = navController)
        }
        composable(
            route = Screens.VideoPlayer.route,
            arguments = listOf(
                navArgument(VIDEO_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(ARTWORK_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(MIME_TYPE){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(TITLE){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(DESCRIPTION){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(DRM_LICENCE_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                }
            ),
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
               fadeOut()+ slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            }
        ){
            VideoPlayerScreen(
                navController = navController,
                videoUrl = it.arguments?.getString(VIDEO_URL),
                mimeType = it.arguments?.getString(MIME_TYPE),
                title = it.arguments?.getString(TITLE),
                description = it.arguments?.getString(DESCRIPTION),
                artworkUrl = it.arguments?.getString(ARTWORK_URL),
                drmLicence = it.arguments?.getString(DRM_LICENCE_URL)
            )
        }

        composable(
            route = Screens.OfflineVideoPlayer.route,
            arguments = listOf(
                navArgument(VIDEO_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(ARTWORK_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(MIME_TYPE){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(TITLE){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(DESCRIPTION){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                },
                navArgument(DRM_LICENCE_URL){
                    type = NavType.StringType
                    defaultValue = "0"
                    nullable = true
                }
            ),
            enterTransition = {
                fadeIn() + slideInVertically(initialOffsetY = { -1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            },
            exitTransition = {
                fadeOut()+ slideOutVertically(targetOffsetY = { 1000 }, animationSpec = tween(700, easing = FastOutLinearInEasing))
            }
        ){
            OfflineVideoPlayerScreen(
                navController = navController,
                videoUrl = it.arguments?.getString(VIDEO_URL),
                mimeType = it.arguments?.getString(MIME_TYPE),
                title = it.arguments?.getString(TITLE),
                description = it.arguments?.getString(DESCRIPTION),
                artworkUrl = it.arguments?.getString(ARTWORK_URL),
                drmLicence = it.arguments?.getString(DRM_LICENCE_URL)
            )
        }
    }
}
