package com.example.albumapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlbumScreen(coverImageUri = null, onCoverImageSelected = {})
        }
    }
}

// Добавляем @Preview отдельно
@Preview(showBackground = true)
@Composable
fun AlbumScreenPreview() {
    AlbumScreen(coverImageUri = null, onCoverImageSelected = {})
}
