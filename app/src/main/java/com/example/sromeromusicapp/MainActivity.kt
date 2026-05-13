package com.example.sromeromusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sromeromusicapp.ui.Detail
import com.example.sromeromusicapp.ui.Screen
import com.example.sromeromusicapp.ui.theme.SRomeroMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SRomeroMusicAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable(
                            route = "detail/{albumId}",
                            arguments = listOf(
                                navArgument("albumId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
                            Detail(albumId = albumId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}