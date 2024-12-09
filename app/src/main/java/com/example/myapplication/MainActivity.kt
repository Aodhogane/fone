package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import java.io.IOException


class MainActivity : ComponentActivity() {
    private var coverImageUri by mutableStateOf<Uri?>(null)
    private var tracks by mutableStateOf<List<Track>>(emptyList())
    private var albumName by mutableStateOf("")
    private var artistName by mutableStateOf("")

    private val selectCoverImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val (width, height) = validateImageSize(it)
                if (width != 512 || height != 512) {
                    Toast.makeText(this, "Image must be 512x512. Current size: ${width}x${height}.", Toast.LENGTH_SHORT).show()
                    coverImageUri = null
                } else {
                    coverImageUri = it
                }
            }
        }

    private fun validateImageSize(uri: Uri): Pair<Int, Int> {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, options)
            return options.outWidth to options.outHeight
        }
        throw IOException("Failed to load image dimensions")
    }

    private val selectTrack =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(this, it)
                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                val durationMillis =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                val duration = durationMillis?.let { ms ->
                    val minutes = ms / 1000 / 60
                    val seconds = ms / 1000 % 60
                    String.format("%d:%02d", minutes, seconds)
                }
                retriever.release()

                val track = Track(it, title, artist, duration)
                tracks = tracks + track
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlbumScreen(
                coverImageUri = coverImageUri,
                onCoverImageSelected = { selectCoverImage.launch("image/*") },
                albumName = albumName,
                onAlbumNameChange = { albumName = it },
                artistName = artistName,
                onArtistNameChange = { artistName = it },
                tracks = tracks,
                onTrackAdded = { selectTrack.launch("audio/*") },
                onSaveAlbum = {
                    saveAlbum(
                        context = this,
                        coverImageUri = coverImageUri,
                        albumName = albumName,
                        artistName = artistName,
                        tracks = tracks
                    )
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(
        coverImageUri = null,
        onCoverImageSelected = {},
        albumName = "Sample Album",
        onAlbumNameChange = {},
        artistName = "Sample Artist",
        onArtistNameChange = {},
        tracks = listOf(

        ),
        onTrackAdded = { },
        onSaveAlbum = { }
    )
}
