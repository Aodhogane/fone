package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.button.PrimaryButton
import com.example.myapplication.text.CustomTextField
import kotlinx.coroutines.launch
import java.io.IOException


// Главный экран альбома
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    coverImageUri: Uri?,
    onCoverImageSelected: (Uri?) -> Unit,
    albumName: String,
    onAlbumNameChange: (String) -> Unit,
    artistName: String,
    onArtistNameChange: (String) -> Unit,
    tracks: List<Track>,
    onTrackAdded: (Track?) -> Unit,
    onSaveAlbum: () -> Unit,
) {
    var isErrorState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Album Screen") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1C1C1E))
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1E))
                .padding(16.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AlbumCoverSection(
                albumCoverUri = coverImageUri,
                onCoverImageSelected = onCoverImageSelected,
                isErrorState = isErrorState
            )

            AlbumDetailsSection(
                albumName = albumName,
                onAlbumNameChange = onAlbumNameChange,
                artistName = artistName,
                onArtistNameChange = onArtistNameChange,
                isErrorState = isErrorState
            )

            TracksSection(
                tracks = tracks,
                onTrackAdded = onTrackAdded,
                isErrorState = isErrorState
            )

            Button(
                onClick = {
                    isErrorState = coverImageUri == null || albumName.isBlank() || artistName.isBlank() || tracks.isEmpty()
                    if (!isErrorState) onSaveAlbum()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xfffacd66),
                    contentColor = Color.Black
                )
            ) {
                Text("Download")
            }
        }
    }
}

// Модель трека
data class Track(val uri: Uri, val title: String?, val artist: String?, val duration: String?)


//секция выбора обложки альбома
@Composable
fun AlbumCoverSection(albumCoverUri: Uri?, onCoverImageSelected: (Uri?) -> Unit, isErrorState: Boolean) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val borderColor = if (isErrorState && albumCoverUri == null) Color.Red else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable {
                onCoverImageSelected(null)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                albumCoverUri?.let {
                    val painter = rememberAsyncImagePainter(it)
                    Image(
                        painter = painter,
                        contentDescription = "Album Cover",
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Icon",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Download the album cover",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun AlbumDetailsSection(
    albumName: String,
    onAlbumNameChange: (String) -> Unit,
    artistName: String,
    onArtistNameChange: (String) -> Unit,
    isErrorState: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "The name of the album and its artist",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        CustomTextField(
            value = albumName,
            onValueChange = { onAlbumNameChange(it) },
            placeholder = "Album Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isErrorState && albumName.isBlank()
        )

        CustomTextField(
            value = artistName,
            onValueChange = { onArtistNameChange(it) },
            placeholder = "Artist Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isErrorState && artistName.isBlank()
        )
    }
}

// Секция треков
@Composable
fun TracksSection(
    tracks: List<Track>,
    onTrackAdded: (Track?) -> Unit,
    isErrorState: Boolean
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val textColor = if (isErrorState && tracks.isEmpty()) Color.Red else Color.White

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Tracks",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { onTrackAdded(null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3A3A3C),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Track",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Add Audio Track",
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }


        tracks.forEach { track ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title ?: "Unknown Title",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = track.artist ?: "Unknown Artist",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(
                        text = track.duration ?: "Unknown Duration",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Track Info",
                    tint = Color.White
                )
            }
        }
    }
}

fun saveAlbum(
    context: Context,
    coverImageUri: Uri?,
    albumName: String,
    artistName: String,
    tracks: List<Track>,
) {
    Toast.makeText(context, "Album successfully saved.", Toast.LENGTH_SHORT).show()
}
