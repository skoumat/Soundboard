package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.DpOffset
import cz.utb.fai.soundboard.domainModels.SoundModel

@Composable
fun SoundTile(
    sound: SoundModel,
    onClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp)
                    .clickable { onClick() }
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = sound.name, textAlign = TextAlign.Center)
                    Text(text = sound.character, style = MaterialTheme.typography.bodySmall)
                }
            }

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = (-8).dp, y = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(140.dp)
                            .wrapContentHeight()
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = { menuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = { menuExpanded = false }
                        )
                    }
                }
            }
        }
    }
}