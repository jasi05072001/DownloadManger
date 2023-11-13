package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jasmeet.downloadmanger.homeScreenItems.HomeScreenItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    val homeScreenList = listOf(
        HomeScreenItems(
            title = "Drm Media Items List",
            onClick = { navController.navigate(Screens.DrmScreen.route) }
        ),
        HomeScreenItems(
            title ="Hls Media Items List",
            onClick = { navController.navigate(Screens.HlsScreen.route) }
        ),
        HomeScreenItems(
            title = "Mp4 Media Items List",
            onClick = { navController.navigate(Screens.Mp4Screen.route) }
        ) ,
        HomeScreenItems(
            title = "View Downloads",
            onClick = {
                navController.navigate(Screens.DownloadScreen.route)

            }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Exoplayer DM Demo")
                },
            )
        }
    ) {
        LazyColumn(
            Modifier
                .padding(top = it.calculateTopPadding()+15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            items(homeScreenList){
                OutlinedCard(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { it.onClick.invoke()}
                ) {
                    Text(
                        text = it.title,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }




}