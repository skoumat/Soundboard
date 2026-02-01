package cz.utb.fai.soundboard.mappers

import cz.utb.fai.soundboard.database.entities.SoundEntity
import cz.utb.fai.soundboard.domainModels.SoundModel


fun SoundEntity.toDomainModel(): SoundModel {
    return SoundModel(
        id = this.id,
        name = this.name,
        characters = parse(this.charactersJson),
        movieId = this.movieId,
        filePathString = this.filePathString
    )
}

fun SoundModel.toEntityModel(): SoundEntity {
    return SoundEntity(
        id = this.id ?: 0L,
        name = this.name,
        charactersJson = serialize(this.characters),
        movieId = this.movieId,
        filePathString = this.filePathString
    )
}