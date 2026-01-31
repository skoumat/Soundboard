package cz.utb.fai.soundboard.models

data class Movie(
    val id: Long,
    val name: String,
    val characters: List<String>
)
