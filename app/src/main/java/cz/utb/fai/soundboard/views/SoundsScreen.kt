package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.core.net.toUri

import cz.utb.fai.soundboard.R
import cz.utb.fai.soundboard.SoundboardApp
import cz.utb.fai.soundboard.services.ExoPlayerObj
import cz.utb.fai.soundboard.viewModels.SortingOrder
import cz.utb.fai.soundboard.viewModels.SoundsViewModel
import cz.utb.fai.soundboard.viewModels.SoundsViewModelFatory

@Composable
fun SoundsScreen(
    navController: NavController,
    onAddSound: (movieId: Long) -> Unit
) {
    val viewModel: SoundsViewModel = viewModel(
        factory = SoundsViewModelFatory(
            (LocalContext.current.applicationContext as SoundboardApp).repository
        )
    )

    val context = LocalContext.current

    val sounds by viewModel.filteredSounds.collectAsState()
    val characters by viewModel.characters.collectAsState()


    var fabExpanded by remember { mutableStateOf(false) }
    var characterDropdownExpanded by remember { mutableStateOf(false) }

    val paddingMiddle = 6.dp

    // TODO: back button?

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                ExoPlayerObj.stop()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        floatingActionButton = { // TODO: separatni soubor s MovieScreen?
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                if (fabExpanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(bottom = 69.dp)
                    ) {
//                        SmallFloatingActionButton(
//                            onClick = {
//                                fabExpanded = false
//                                // TODO: novy film?
//                            }
//                        ) {
//                            Text("Add movie")
//                        }

                        SmallFloatingActionButton(
                            onClick = {
                                fabExpanded = false
                                onAddSound(viewModel.movieId)
                            }
                        ) {
                            Text(
                                "Add sound",
                                modifier = Modifier.padding(10.dp))
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
                value = viewModel.searchQuery,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search sounds") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(paddingMiddle))

            // sorting row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly // .spacedBy(12.dp)
            ) {
                Button(onClick = {
                    val next = if (viewModel.sortOrder == SortingOrder.ASC) SortingOrder.DESC else SortingOrder.ASC
                    viewModel.onSortChange(next)
                }) {
                    Text(if (viewModel.sortOrder == SortingOrder.ASC) "A→Z" else "Z→A")
                }

                Box {
                    Button(onClick = { characterDropdownExpanded = true }) {
                        Text(viewModel.characterFilter ?: "Filter by character")
                    }

                    DropdownMenu(
                        expanded = characterDropdownExpanded,
                        onDismissRequest = { characterDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {
                                viewModel.onCharacterFilter(null)
                                characterDropdownExpanded = false
                            }
                        )

                        characters.forEach { character ->
                            DropdownMenuItem(
                                text = { Text(character) },
                                onClick = {
                                    viewModel.onCharacterFilter(character)
                                    characterDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(paddingMiddle))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    viewModel.movieName,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorResource(id = R.color.green))
            }

            Spacer(modifier = Modifier.height(paddingMiddle))

            // obsah
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sounds) { sound ->
                    SoundTile(
                        sound = sound,
                        viewModel = viewModel,
                        navController = navController,
                        onClick = {
                            val uri = sound.filePathString.toUri()
                            if(ExoPlayerObj.lastPlayedUri == uri && ExoPlayerObj.isNowPlaying){
                                ExoPlayerObj.stop()
                            }
                            else{
                                ExoPlayerObj.play(context, uri)
                            }

                        }
                    )
                }
            }
        }
    }
}