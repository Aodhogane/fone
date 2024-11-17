package com.example.albumapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ortin.ortinFyForAuthors.core.ui.components.textfield.CustomTextField

class MainActivity : ComponentActivity() {

    private val getImageResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Сохраняем URI изображения
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
fun ShowToastMessage(message: String) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
        CoverUploadSection(coverImageUri = coverImageUri, onCoverImageSelected = onCoverImageSelected)

        AlbumDetailsSection(
            albumName = albumName,
            onAlbumNameChange = { albumName = it },
            artistName = artistName,
            onArtistNameChange = { artistName = it }
        )

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

@Composable
fun CoverUploadSection(coverImageUri: Uri?, onCoverImageSelected: (Uri?) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            coverImageUri?.let {
                // Если URI изображения существует, показываем его
                val painter: Painter = rememberAsyncImagePainter(it)
                Image(painter = painter, contentDescription = "Album Cover", modifier = Modifier.fillMaxSize())
            } ?: run {
                // Если изображение не выбрано, показываем иконку загрузки
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
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    onCoverImageSelected(null)  // Сбрасываем текущую обложку
                    activity?.let {
                        // Открываем галерею из активности
                        it.startActivityForResult(
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            1001
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun AlbumDetailsSection(
    albumName: String,
    onAlbumNameChange: (String) -> Unit,
    artistName: String,
    onArtistNameChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                .padding(12.dp)
        )
        CustomTextField(
            value = artistName,
            onValueChange = onArtistNameChange,
            placeholder = "Artist Name",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )
    }
}

@Composable
fun TracksSection(
    tracks: List<String>,
    newTrackName: String,
    onNewTrackNameChange: (String) -> Unit,
    onAddTrack: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Tracks",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        CustomTextField(
            value = newTrackName,
            onValueChange = onNewTrackNameChange,
            placeholder = "Add a new track",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                .padding(12.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Track",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onAddTrack() }
                )
            }
        )
        tracks.forEach { track ->
            Text(
                text = track,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onButtonClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        enabled = isEnabled,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = Color.Gray,
            containerColor = if (isEnabled) Color(0xFF6200EE) else Color.Gray
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
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
