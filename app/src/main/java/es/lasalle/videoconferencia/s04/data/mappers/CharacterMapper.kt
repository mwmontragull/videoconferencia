package es.lasalle.videoconferencia.s04.data.mappers

import es.lasalle.videoconferencia.s04.data.local.CharacterEntity
import es.lasalle.videoconferencia.s04.data.remote.models.ApiCharacter
import es.lasalle.videoconferencia.s04.data.remote.models.ApiCharacterLocation
import es.lasalle.videoconferencia.s04.domain.models.Character

fun ApiCharacter.toDomainModel(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin.name,
        location = this.location.name,
        imageUrl = this.image
    )
}

fun List<ApiCharacter>.toDomainModels(): List<Character> {
    return this.map { it.toDomainModel() }
}

// Entity mappers needed for LocalDataSource
fun ApiCharacter.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type ?: "",
        gender = this.gender,
        originName = this.origin.name,
        originUrl = this.origin.url,
        locationName = this.location.name,
        locationUrl = this.location.url,
        image = this.image,
        episode = this.episode,
        url = this.url,
        created = this.created,
        isFavorite = false,
        lastAccessed = System.currentTimeMillis()
    )
}

fun List<ApiCharacter>.toEntityList(): List<CharacterEntity> {
    return this.map { it.toEntity() }
}

fun CharacterEntity.toApiCharacter(): ApiCharacter {
    return ApiCharacter(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        origin = ApiCharacterLocation(this.originName, this.originUrl),
        location = ApiCharacterLocation(this.locationName, this.locationUrl),
        image = this.image,
        episode = this.episode,
        url = this.url,
        created = this.created
    )
}

fun List<CharacterEntity>.toApiCharacterList(): List<ApiCharacter> {
    return this.map { it.toApiCharacter() }
}

fun ApiCharacter.toEntityPreservingUserData(existingEntity: CharacterEntity?): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type ?: "",
        gender = this.gender,
        originName = this.origin.name,
        originUrl = this.origin.url,
        locationName = this.location.name,
        locationUrl = this.location.url,
        image = this.image,
        episode = this.episode,
        url = this.url,
        created = this.created,
        isFavorite = existingEntity?.isFavorite ?: false,
        lastAccessed = existingEntity?.lastAccessed ?: System.currentTimeMillis()
    )
}