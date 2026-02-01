package cz.utb.fai.soundboard.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import cz.utb.fai.soundboard.SoundboardApp
import cz.utb.fai.soundboard.viewModels.MovieDetailsViewModel
import cz.utb.fai.soundboard.viewModels.MovieDetailsViewModelFactory

@Composable
fun MovieDetailsScreen(
    navController: NavController
) {
    val viewModel: MovieDetailsViewModel = viewModel(
        factory = MovieDetailsViewModelFactory((LocalContext.current.applicationContext as SoundboardApp).repository)
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
                    text = viewModel.movieNameMut,
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

                Text("Director: " + viewModel.director, style = MaterialTheme.typography.titleMedium)

                Text("Released: " + viewModel.released, style = MaterialTheme.typography.titleMedium)
                Text("Cast: ", style = MaterialTheme.typography.titleMedium)

                LazyColumn {
                    items(viewModel.cast) { person ->
                        Text(
                            text = person,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}