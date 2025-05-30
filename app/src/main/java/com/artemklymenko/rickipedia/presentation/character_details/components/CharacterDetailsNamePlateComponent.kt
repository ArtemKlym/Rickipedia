package com.artemklymenko.rickipedia.presentation.character_details.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artemklymenko.network.models.domain.CharacterStatus
import com.artemklymenko.rickipedia.presentation.ui.theme.RickAction
import com.artemklymenko.rickipedia.presentation.ui.theme.RickTextPrimary


@Composable
fun CharacterDetailsNamePlateComponent(name: String, status: CharacterStatus) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CharacterStatusComponent(characterStatus = status)
        Text(
           text = name,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = RickAction
        )
    }
}

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
    Text(
        text = "Status: ${characterStatus.displayName}",
        color = RickTextPrimary,
        fontSize = 20.sp,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = when(characterStatus) {
                    is CharacterStatus.Alive -> Color.Green
                    is CharacterStatus.Unknown -> Color.Gray
                    CharacterStatus.Dead -> Color.Red
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}

@Preview
@Composable
private fun CharacterDetailsNamePlateComponentPreview() {
    MaterialTheme{
        Column {
            CharacterDetailsNamePlateComponent("Rick", CharacterStatus.Alive)
            CharacterDetailsNamePlateComponent("Rick", CharacterStatus.Unknown)
            CharacterDetailsNamePlateComponent("Rick", CharacterStatus.Dead)
        }
    }
}
