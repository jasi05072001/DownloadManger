package com.jasmeet.downloadmanger

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.jasmeet.downloadmanger.screens.Screens
import com.jasmeet.downloadmanger.utils.DownloadUtil
import com.jasmeet.downloadmanger.utils.MediaItemTag
import io.mockk.MockKAnnotations
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var context :Context
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        MockKAnnotations.init(this)
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainApp(navController = navController)
        }
    }

    private fun NavController.assertCurrentRouteName(expectedRouteName: String) {
        Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
    }

    @Test
    fun navigateToDrmScreen(){
        composeTestRule.onNodeWithTag("DrmScreen").performClick()
    }

    @Test
    fun moodTrackerNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screens.HomeScreen.route)
    }

    @OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
    @Test
    fun performClickOnDrmItem(){
         context = ApplicationProvider.getApplicationContext()

        val downloadUtil = DownloadUtil.getDownloadTracker(context)

        val mediaItem =  MediaItem.Builder()
            .setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("Elephant Dream")
                    .setDescription(
                        "The film tells the story of Emo and Proog, two people with different visions of the surreal world in which they live. " +
                                "Viewers are taken on a journey through that world, full of strange mechanical birds, stunning technological vistas and machinery that seems to have a life of its own."
                    )
                    .setArtworkUri(Uri.parse("https://images.unsplash.com/photo-1594623930572-300a3011d9ae?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
                    .build()
            )
            .setTag(MediaItemTag(-1, "Elephant Dream"))
            .build()

        composeTestRule.onNodeWithTag("Mp4Screen").performClick()
        composeTestRule.onNodeWithTag("FirstMediaItem").performClick()
        composeTestRule.onNodeWithTag("DownloadButton").performClick()

        assertEquals(downloadUtil.isDownloaded(mediaItem), false)

    }

    @Test
    fun performClickOnHlsItem(){
        composeTestRule.onNodeWithTag("HlsScreen").performClick()
        navController.assertCurrentRouteName(Screens.HlsScreen.route)
    }

    @Test
    fun performClickOnMp4Item(){
        composeTestRule.onNodeWithTag("Mp4Screen").performClick()
        navController.assertCurrentRouteName(Screens.Mp4Screen.route)
    }
}