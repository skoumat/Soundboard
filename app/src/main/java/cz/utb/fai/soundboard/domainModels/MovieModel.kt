package cz.utb.fai.soundboard.domainModels

data class MovieModel(
    val id: Long?,
    val name: String,
    val characters: List<String>
)
