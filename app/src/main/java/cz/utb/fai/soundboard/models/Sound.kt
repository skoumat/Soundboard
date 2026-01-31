package cz.utb.fai.soundboard.models

data class Sound(
    val id: Long,
    val name: String,
    val character: String,
    val filePathString: String
)
