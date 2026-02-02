package cz.utb.fai.soundboard.views

import android.util.Log
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import cz.utb.fai.soundboard.SoundboardApp
import cz.utb.fai.soundboard.viewModels.EditSoundViewModel
import cz.utb.fai.soundboard.viewModels.EditSoundViewModelFactory


@Composable
fun EditSoundScreen(
    soundId: Long?,
    navController: NavController,
) {
    val viewModel: EditSoundViewModel = viewModel(
        factory = EditSoundViewModelFactory((LocalContext.current.applicationContext as SoundboardApp).repository)
    )

    val characters by viewModel.characters.collectAsState()

    val context = LocalContext.current

    val soundPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                viewModel.soundSelected(it.toString())
            }
        }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (soundId == null) "New Sound" else "Edit Sound",
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


                OutlinedTextField(
                    value = viewModel.soundName,
                    onValueChange = viewModel::onSoundNameChange,
                    label = { Text("Sound Name") },
                    modifier = Modifier.fillMaxWidth()
                )


                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = viewModel.selectedCharacter,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Character") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Character")
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(200.dp) // Optional: control width
                    ) {
                        characters.forEach { character ->
                            DropdownMenuItem(
                                text = { Text(character) },
                                onClick = {
                                    viewModel.onCharacterSelected(character)
                                    expanded = false
                                }
                            )
                        }
                    }
                }


                Button(
                    onClick = { soundPickerLauncher.launch(arrayOf("audio/*")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(viewModel.soundFileUriString ?: "Select Sound File")
                }


                Button(
                    onClick = viewModel::saveSound,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }

                if (soundId != null) {
                    OutlinedButton(
                        onClick = viewModel::deleteSound,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Sound")
                    }
                }
            }
        }
    }
}