package com.jasmeet.downloadmanger.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun Mp4Screen(navController: NavHostController, title: String, id: String) {
    Column {
        Text(text = title)
        Text(text = id)
    }

}