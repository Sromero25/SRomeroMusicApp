package com.example.sromeromusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sromeromusicapp.ui.theme.SRomeroMusicAppTheme
import androidx.compose.runtime.*
import androidx.compose.material3.*
import com.example.sromeromusicapp.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SRomeroMusicAppTheme {
                    TestScreen()
                }
            }
        }
}

@Composable
fun TestScreen() {

    var text by remember {
        mutableStateOf("Cargando...")
    }

    LaunchedEffect(Unit) {

        try {

            val albums =
                RetrofitInstance.api.getAlbums()

            text =
                albums.firstOrNull()?.title
                    ?: "No hay álbumes"

        } catch (e: Exception) {

            text = "Error: ${e.message}"
        }
    }

    Text(text = text)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SRomeroMusicAppTheme {
        Greeting("Android")
    }
}