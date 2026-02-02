package cz.utb.fai.soundboard.mappers

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

import cz.utb.fai.soundboard.database.entities.MovieEntity
import cz.utb.fai.soundboard.domainModels.MovieModel

val json = Json { encodeDefaults = true }

fun serialize(characters: List<String>): String =
    json.encodeToString(characters)

fun parse(charactersJson: String?): List<String>{
    return if (charactersJson != null) json.decodeFromString(charactersJson) else emptyList()
}



fun MovieEntity.toDomainModel(): MovieModel {
    return MovieModel(
        id = this.id,
        name = this.name,
        characters = parse(charactersJson)
    )
}

fun MovieModel.toEntityModel(): MovieEntity {
    return MovieEntity(
        id = this.id ?: 0L,
        name = this.name,
        charactersJson = serialize(this.characters)
    )
}