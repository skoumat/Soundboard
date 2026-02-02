package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import cz.utb.fai.soundboard.SoundboardApp
import cz.utb.fai.soundboard.viewModels.EditMovieViewModel
import cz.utb.fai.soundboard.viewModels.EditMovieViewModelFactory

@Composable
fun EditMovieScreen(
    movieId: Long?,
    navController: NavController,
) {
    val viewModel: EditMovieViewModel = viewModel(
        factory = EditMovieViewModelFactory((LocalContext.current.applicationContext as SoundboardApp).repository)
    )

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = if (movieId == null || movieId < 0) "New Movie" else "Edit Movie",
                    style = MaterialTheme.typography.headlineSmall
                )
            }



            HorizontalDivider()



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = viewModel.movieName,
                        onValueChange = viewModel::onMovieNameChange,
                        label = { Text("Movie name") },
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {
                            navController.navigate("movie_details?movieName=${viewModel.movieName}")
                        }
                    ) {
                        Text("Get details")
                    }
                }



                Column(
                    modifier = Modifier.fillMaxHeight(0.5f)
                ){
                    Text("Characters", style = MaterialTheme.typography.titleMedium)

                    LazyColumn {
                        items(viewModel.movieCharacters) { character ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(character, modifier = Modifier.weight(1f))
                                IconButton(onClick = { viewModel.removeCharacter(character) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = viewModel.newCharacter,
                        onValueChange = viewModel::onNewCharacterChange,
                        label = { Text("New character") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = viewModel::addCharacter) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = viewModel::saveMovie,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

                if (movieId != null) {
                    OutlinedButton(
                        onClick = viewModel::deleteMovie,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Movie")
                    }
                }
            }
        }
    }
}