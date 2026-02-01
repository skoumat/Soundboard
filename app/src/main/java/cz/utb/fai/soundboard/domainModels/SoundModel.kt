package cz.utb.fai.soundboard.domainModels

data class SoundModel(
    val id: Long,
    val name: String,
    val character: String,
    val movieId: Long,
    val filePathString: String
)
