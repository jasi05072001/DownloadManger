package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun VideoPlayerScreen(
    navController: NavHostController,
    videoUrl: String?,
    mimeType: String?,
    title: String?,
    description: String?,
    artworkUrl: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ){

        Text(text =videoUrl.toString())
        Text(text = mimeType.toString())
        Text(text = title.toString())
        Text(text = description.toString())
        Text(text = artworkUrl.toString())

    }


}