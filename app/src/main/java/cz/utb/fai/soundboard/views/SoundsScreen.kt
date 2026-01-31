package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SoundsScreen() {

    var fabExpanded by remember { mutableStateOf(false) }
    var characterDropdownExpanded by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = { // TODO: separatni soubor s MovieScreen?
            Box (
                contentAlignment = Alignment.BottomEnd
            ){
                if (fabExpanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(bottom = 69.dp)
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                fabExpanded = false
                            }
                        ) {
                            Text("Add movie")
                        }

                        SmallFloatingActionButton(
                            onClick = {
                                fabExpanded = false
                            }
                        ) {
                            Text("Add sound")
                        }
                    }
                }

                FloatingActionButton(
                    onClick = {
                        fabExpanded = !fabExpanded
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {


            // vyhledavaci okno
            OutlinedTextField(
                value = "",
                onValueChange = { e -> {}},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search sounds") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // sorting row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly // .spacedBy(12.dp)
            ) {
                Button(onClick = {
                    // TODO: ordering
                }) {
                    Text("A→Z")
                }

                Box {
                    Button(onClick = { characterDropdownExpanded = true }) {
                        Text( "Filter by character")
                    }

                    DropdownMenu(
                        expanded = characterDropdownExpanded,
                        onDismissRequest = { characterDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {}
                        )

                        // TODO: jednotlive postavy nejak dynamicky
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // obsah
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    SoundTile(
                        name = "aaaaa",
                        onClick = {}
                    )
                }

                item {
                    SoundTile(
                        name = "bbb",
                        onClick = {}
                    )
                }

                item {
                    SoundTile(
                        name = "ccc",
                        onClick = {}
                    )
                }

            }
        }
    }
}