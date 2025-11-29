package es.lasalle.videoconferencia.s04.data.remote.models

data class ApiCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String?,
    val gender: String,
    val origin: ApiCharacterLocation,
    val location: ApiCharacterLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class ApiCharacterLocation(
    val name: String,
    val url: String
)