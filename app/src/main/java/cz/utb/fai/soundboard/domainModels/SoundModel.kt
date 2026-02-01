package cz.utb.fai.soundboard.domainModels

data class SoundModel(
    val id: Long?,
    val name: String,
    val characters: List<String>,
    val movieId: Long,
    val filePathString: String
)
