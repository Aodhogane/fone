package com.example.myapplication

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

@Composable
fun AlbumCoverSection(coverImageUri: Uri?, onCoverImageSelected: (Uri?) -> Unit) {
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
                // Если URI существует, показываем изображение
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
                    onCoverImageSelected(null)  // Сбросить обложку
                    activity?.let {
                        // Открываем галерею для выбора изображения
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