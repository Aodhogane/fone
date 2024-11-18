package com.example.albumapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    private var coverImageUri: Uri? by mutableStateOf(null)
    private var tracks: List<Uri> by mutableStateOf(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Передаем состояние в экран
            AlbumScreen(
                coverImageUri = coverImageUri,
                onCoverImageSelected = { uri -> coverImageUri = uri },
                tracks = tracks,
                onTrackAdded = { trackUri -> tracks = tracks + trackUri }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                val coverUri = data?.data
                coverUri?.let {
                    coverImageUri = it
                }
            } else if (requestCode == 1002) {
                val trackUri = data?.data
                trackUri?.let {
                    tracks = tracks + it
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(coverImageUri = null, onCoverImageSelected = {}, tracks = listOf(), onTrackAdded = {})
}
