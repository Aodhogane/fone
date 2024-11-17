package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.ortin.ortinFyForAuthors.core.ui.components.textfield.CustomTextField

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