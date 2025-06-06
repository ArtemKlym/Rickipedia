package com.artemklymenko.rickipedia.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextSecondary

@Composable
fun ErrorMessageComponent(
    message: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: "Unknown exception occurred",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = RickTextSecondary
        )
    }
}