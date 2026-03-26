package com.lhz.scaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.lhz.feature.user.UserScreen
import com.lhz.scaffold.ui.theme.RoadtestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoadtestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Test: feature:feature-user composable
                    UserScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}