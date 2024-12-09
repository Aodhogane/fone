package com.example.myapplication.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    onButtonClick: () -> Unit,  // Обычная функция, без @Composable
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
) {
    Button(
        modifier = modifier.height(ButtonDefaults.MinHeight),
        enabled = isEnabled,
        onClick = onButtonClick,  // Передаем обычную функцию
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(size = 16.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.aspectRatio(1f),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            }
            Row(
                modifier = Modifier.graphicsLayer {
                    alpha = if (isLoading) 0f else 1f
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leftIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = text,
                )
                rightIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}