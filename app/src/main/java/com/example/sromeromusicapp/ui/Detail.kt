package com.example.sromeromusicapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.sromeromusicapp.model.Album
import com.example.sromeromusicapp.model.RetrofitInstance
import com.example.sromeromusicapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Detail(albumId: String, navController: NavController) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val scope = rememberCoroutineContext()

    // URL funcional como respaldo (por si la API no trae imagen válida)
    val workingImageUrl = "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png"

    LaunchedEffect(albumId) {
        kotlinx.coroutines.launch {
            try {
                val apiAlbum = RetrofitInstance.api.getAlbumById(albumId)
                // Forzar imagen funcional
                album = apiAlbum.copy(image = workingImageUrl)
                isLoading = false
            } catch (e: Exception) {
                errorMsg = "Error: ${e.localizedMessage}"
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(PurpleLight), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PurplePrimary)
        }
        return
    }

    if (errorMsg != null || album == null) {
        Box(modifier = Modifier.fillMaxSize().background(PurpleLight), contentAlignment = Alignment.Center) {
            Text(errorMsg ?: "Álbum no encontrado", color = Color.Red)
        }
        return
    }

    val a = album!!
    val tracks = (1..10).map { i -> "${a.title} • Track $i" }

    Box(modifier = Modifier.fillMaxSize().background(PurpleLight)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            // HEADER IMAGE
            item {
                Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(a.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = a.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Scrim morado
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x881A0A3D),
                                        Color(0xDD2D1B69)
                                    )
                                )
                            )
                    )
                    // Botones atrás y favorito
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .statusBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        IconButton(onClick = { isFavorite = !isFavorite }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color.White
                            )
                        }
                    }
                    // Título y artista + botones play/shuffle
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(a.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(a.artist, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(PurplePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }

            // ABOUT THIS ALBUM
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("About this album", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PurplePrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(a.description, fontSize = 14.sp, color = TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ARTIST CHIP
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PurpleLight,
                        tonalElevation = 2.dp
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                            Text("Artist: ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text(a.artist, fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // TRACK LIST
            items(tracks) { trackTitle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = a.image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(trackTitle, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary, maxLines = 1)
                            Text(a.artist, fontSize = 12.sp, color = TextSecondary)
                        }
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        // MINI PLAYER
        Reproductor(album = a, modifier = Modifier.align(Alignment.BottomCenter))
    }
}