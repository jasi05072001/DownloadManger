package com.jasmeet.downloadmanger

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.jasmeet.downloadmanger.screens.Screens
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
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

    @Test
    fun performClickOnDrmItem(){
        composeTestRule.onNodeWithTag("DrmScreen").performClick()
        composeTestRule.onNodeWithTag("FirstMediaItem").performClick()
        navController.assertCurrentRouteName(Screens.VideoPlayer.route)
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