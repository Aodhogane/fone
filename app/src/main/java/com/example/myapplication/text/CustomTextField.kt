package com.example.myapplication.text

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = if (isError) {
        OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Red,
            unfocusedTextColor = Color.Red,
            focusedBorderColor = Color.Red,
            unfocusedBorderColor = Color.Red
        )
    } else {
        OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color(0xfffacd66)
        )
    }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeholder, color = if (isError) Color.Red else Color.Gray) },
        modifier = modifier,
        colors = colors
    )
}
