package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cz.utb.fai.soundboard.SoundboardApp

import androidx.compose.ui.platform.LocalContext

import cz.utb.fai.soundboard.viewModels.MoviesViewModel
import cz.utb.fai.soundboard.viewModels.MoviesViewModelFactory

@Composable
fun MoviesScreen(
    navController: NavController,
    onMovieClick: (Long) -> Unit,
    onAddMovie: () -> Unit
) {
    val savedStateHandle = remember { SavedStateHandle() }
    val viewModel: MoviesViewModel = viewModel(
        factory = MoviesViewModelFactory((LocalContext.current.applicationContext as SoundboardApp).repository, savedStateHandle)
    )

    val movies by viewModel.filteredMovies.collectAsState()

    var fabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                if (fabExpanded) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(bottom = 69.dp)
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                fabExpanded = false
                                onAddMovie()
                            }
                        ) {
                            Text("Add movie")
                        }

//                        SmallFloatingActionButton(
//                            onClick = {
//                                fabExpanded = false
//                            }
//                        ) {
//                            Text("Add sound")
//                        }
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

            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search movies") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies) { movie ->
                    MovieTile(
                        movie = movie,
                        navController = navController,
                        viewModel = viewModel,
                        onClick = { onMovieClick(movie.id!!) }
                    )
                }
            }
        }
    }
}