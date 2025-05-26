package com.artemklymenko.rickipedia.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artemklymenko.rickipedia.core.domain.DataPoint
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextPrimary

@Composable
fun DataPointComponent(dataPoint: DataPoint) {
    Column {
        Text(
            text = dataPoint.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RickAction
        )
        Text(
            text = dataPoint.description,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = RickTextPrimary
        )
    }
}

@Preview
@Composable
private fun DataPointComponentDarkPreview() {
    MaterialTheme{
        DataPointComponent(
            DataPoint("Last known place", "Citadel of Ricks")
        )
    }
}