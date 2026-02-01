package cz.utb.fai.soundboard.services.api

data class MovieDetailsModel(
    val title: String,
    val director: String? = null,
    val releaseDate: String? = null,
    val cast: List<String> = emptyList()
)
