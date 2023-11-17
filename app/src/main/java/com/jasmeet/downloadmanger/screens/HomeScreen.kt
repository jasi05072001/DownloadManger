package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import com.jasmeet.downloadmanger.appComponent.OutlinedCardComponent
import com.jasmeet.downloadmanger.appComponent.VerticalSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    Scaffold(
        modifier =  Modifier.testTag("HomeScreen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Exoplayer DM Demo")
                },
            )
        }
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            OutlinedCardComponent(
                testTag = "DrmScreen",
                text = "Drm Media Items List",
                onClick = {
                    navController.navigate(Screens.DrmScreen.route)
                }
            )

            VerticalSpace(height = 20)

            OutlinedCardComponent(
                testTag = "HlsScreen",
                text = "Hls Media Items List",
                onClick = {
                    navController.navigate(Screens.HlsScreen.route)
                }
            )

            VerticalSpace(height = 20)


            OutlinedCardComponent(
                testTag = "Mp4Screen",
                text = "Mp4 Media Items List",
                onClick = {
                    navController.navigate(Screens.Mp4Screen.route)
                }
            )

            VerticalSpace(height = 20)

        }
    }

}