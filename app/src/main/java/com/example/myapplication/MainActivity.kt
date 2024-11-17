package com.example.albumapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.AlbumCoverSection
import com.example.myapplication.AlbumDetailsSection
import com.example.myapplication.TracksSection

class MainActivity : ComponentActivity() {

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            coverImageUri = it
        }
    }

    private var coverImageUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlbumScreen(
                coverImageUri = coverImageUri,
                onCoverImageSelected = { uri ->
                    coverImageUri = uri
                }
            )
        }
    }

    fun openGallery() {
        getImageResult.launch("image/*")
    }
}

@Composable
fun AlbumScreen(coverImageUri: Uri?, onCoverImageSelected: (Uri?) -> Unit) {
    var albumName by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var tracks by remember { mutableStateOf(listOf<String>()) }
    var newTrackName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AlbumCoverSection(coverImageUri = coverImageUri, onCoverImageSelected = onCoverImageSelected)

        // Секция для ввода данных альбома и исполнителя
        AlbumDetailsSection(
            albumName = albumName,
            onAlbumNameChange = { albumName = it },
            artistName = artistName,
            onArtistNameChange = { artistName = it }
        )

        // Секция для ввода треков
        TracksSection(
            tracks = tracks,
            newTrackName = newTrackName,
            onNewTrackNameChange = { newTrackName = it },
            onAddTrack = {
                if (newTrackName.isNotBlank()) {
                    tracks = tracks + newTrackName
                    newTrackName = ""
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumScreenPreview() {
    MaterialTheme {
        AlbumScreen(coverImageUri = null, onCoverImageSelected = {})
    }
}
