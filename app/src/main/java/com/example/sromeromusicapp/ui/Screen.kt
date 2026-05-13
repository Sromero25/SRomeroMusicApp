package com.example.sromeromusicapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sromeromusicapp.model.Album
import com.example.sromeromusicapp.model.RetrofitInstance
import com.example.sromeromusicapp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Screen(navController: NavController) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var currentAlbum by remember { mutableStateOf<Album?>(null) }
    val scope = rememberCoroutineScope()

    // Álbum hardcodeado para mostrar en el mini player al inicio (requerimiento del profesor)
    val hardcodedAlbum = Album(
        id = "69f90c5e2fa93270c1903690",
        title = "The Dark Side of the Moon",
        artist = "Pink Floyd",
        description = "Album conceptual de 1973.",
        image = "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png"
    )

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                albums = RetrofitInstance.api.getAlbums()
                currentAlbum = hardcodedAlbum
                isLoading = false
            } catch (e: Exception) {
                errorMsg = "Error al cargar álbumes: ${e.localizedMessage}"
                currentAlbum = hardcodedAlbum
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(PurpleLight)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
        ) {
            // ── HEADER ──
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(PurplePrimary, PurpleLight)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 28.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Good Morning!", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                        Text(
                            "Juan Frausto",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ── ALBUMS (LazyRow) ──
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Albums", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Text("See more", color = PurplePrimary, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PurplePrimary)
                    }
                } else if (errorMsg != null) {
                    Text(errorMsg!!, color = Color.Red, modifier = Modifier.padding(horizontal = 20.dp))
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(albums) { album ->
                            AlbumCard(album = album, onClick = {
                                currentAlbum = album
                                navController.navigate("detail/${album.id}")
                            })
                        }
                    }
                }
            }

            // ── RECENTLY PLAYED ──
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recently Played", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Text("See more", color = PurplePrimary, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (!isLoading && errorMsg == null) {
                items(albums) { album ->
                    RecentlyPlayedItem(album = album, onClick = {
                        currentAlbum = album
                        navController.navigate("detail/${album.id}")
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        // ── MINI PLAYER ──
        currentAlbum?.let {
            Reproductor(album = it, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Overlay oscuro
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xCC1A0A3D))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(album.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Text(album.artist, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
            }
            // Botón play circular
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = PurplePrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = album.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(album.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary, maxLines = 1)
                Text("${album.artist} • Popular Song", fontSize = 12.sp, color = TextSecondary, maxLines = 1)
            }
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}