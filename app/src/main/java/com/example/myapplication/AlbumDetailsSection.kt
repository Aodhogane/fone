package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ortin.ortinFyForAuthors.core.ui.components.textfield.CustomTextField

@Composable
fun AlbumDetailsSection(
    albumName: String,
    onAlbumNameChange: (String) -> Unit,
    artistName: String,
    onArtistNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Группировка полей для ввода названия альбома и имени исполнителя
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "The name of the album and its artist",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Поле для ввода названия альбома
            CustomTextField(
                value = albumName,
                onValueChange = onAlbumNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp)) // Добавление серого фона
                    .padding(16.dp),  // Добавление отступов
                placeholder = "Album Name"
            )

            // Поле для ввода имени исполнителя
            CustomTextField(
                value = artistName,
                onValueChange = onArtistNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))  // Добавление серого фона
                    .padding(16.dp),  // Добавление отступов
                placeholder = "Artist Name"
            )
        }
    }
}
