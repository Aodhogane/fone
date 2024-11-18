package com.example.albumapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// Главный экран альбома
@Composable
fun AlbumScreen(
    coverImageUri: Uri?,
    onCoverImageSelected: (Uri?) -> Unit,
    tracks: List<Uri>,
    onTrackAdded: (Uri) -> Unit
) {
    var albumName by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AlbumCoverSection(coverImageUri = coverImageUri, onCoverImageSelected = onCoverImageSelected)

        AlbumDetailsSection(
            albumName = albumName,
            onAlbumNameChange = { albumName = it },
            artistName = artistName,
            onArtistNameChange = { artistName = it }
        )

        TracksSection(
            tracks = tracks,
            onTrackAdded = onTrackAdded
        )
    }
}

// Секция выбора обложки альбома
@Composable
fun AlbumCoverSection(coverImageUri: Uri?, onCoverImageSelected: (Uri?) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
            .clickable {
                onCoverImageSelected(null)
                activity?.let {
                    it.startActivityForResult(
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        1001
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            coverImageUri?.let {
                val painter: Painter = rememberAsyncImagePainter(it)
                Image(
                    painter = painter,
                    contentDescription = "Album Cover",
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download Cover",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "Download the album cover",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Секция деталей альбома
@Composable
fun AlbumDetailsSection(
    albumName: String,
    onAlbumNameChange: (String) -> Unit,
    artistName: String,
    onArtistNameChange: (String) -> Unit
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
            onValueChange = onAlbumNameChange,
            placeholder = "Album Name",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                .padding(16.dp)
        )

        CustomTextField(
            value = artistName,
            onValueChange = onArtistNameChange,
            placeholder = "Artist Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
    }
}

// Секция треков
@Composable
fun TracksSection(
    tracks: List<Uri>,
    onTrackAdded: (Uri) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Tracks",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = {
                activity?.let {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "audio/*"
                    }
                    it.startActivityForResult(Intent.createChooser(intent, "Select Audio File"), 1002)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Track",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Add Audio Track")
        }

        tracks.forEach { trackUri ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trackUri.lastPathSegment ?: "Unknown File",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Track Info",
                    tint = Color.White
                )
            }
        }
    }
}

// Компонент для кастомного текстового поля
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color.White
        ),
        trailingIcon = trailingIcon
    )
}
