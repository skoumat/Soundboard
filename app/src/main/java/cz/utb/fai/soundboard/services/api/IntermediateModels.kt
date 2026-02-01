package cz.utb.fai.soundboard.services.api

data class WikidataResponse(
    val head: Head,
    val results: Results
)

data class Head(val vars: List<String>)

data class Results(val bindings: List<Binding>)

data class Binding(
    val movieLabel: Value? = null,
    val directorLabel: Value? = null,
    val releaseDate: Value? = null,
    val actorLabel: Value? = null
)

data class Value(val type: String, val value: String)
