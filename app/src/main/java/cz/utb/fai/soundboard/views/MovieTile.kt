package cz.utb.fai.soundboard.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import cz.utb.fai.soundboard.domainModels.MovieModel
import cz.utb.fai.soundboard.navigation.Routes
import cz.utb.fai.soundboard.viewModels.MoviesViewModel

@Composable
fun MovieTile(
    movie: MovieModel,
    navController: NavController,
    viewModel: MoviesViewModel,
    onClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .clickable { onClick() }
            ) {
                Text(
                    text = movie.name,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = { menuExpanded = true }
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = (-75).dp, y = (-40).dp) // -x je doleva, -y je nahoru
                ) {
                    Column(
                        modifier = Modifier
                            .width(90.dp)
                            .wrapContentHeight())
                    {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            modifier = Modifier.height(32.dp),
                            onClick = {
                                menuExpanded = false
                                Log.e("PPPPPPPPPPPP", "${movie.id}")
                                navController.navigate("edit_movie/${movie.id}")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            modifier = Modifier.height(32.dp),
                            onClick = {
                                menuExpanded = false
                                viewModel.deleteMovie(movie.id!!)
                            }
                        )
                    }
                }
            }
        }
    }
}