package com.commandiron.iosswipesearchcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.commandiron.iosswipesearchcompose.ui.theme.IosSwipeSearchComposeTheme
import com.commandiron.swipe_search_compose.SwipeSearch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IosSwipeSearchComposeTheme {
                val text = remember { mutableStateOf("") }
                SwipeSearch(
                    modifier = Modifier.fillMaxSize(),
                    textValue = text.value,
                    onValueChange = {
                        text.value = it
                    }
                )
            }
        }
    }
}