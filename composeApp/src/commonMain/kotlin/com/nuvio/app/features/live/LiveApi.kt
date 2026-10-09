package com.nuvio.app.features.live

import com.nuvio.app.core.network.createApiHttpClient
import com.nuvio.app.core.network.readBoundedResponseBody
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal const val SPORTSRC_API_BASE = "https://api.sportsrc.org"

@Serializable
internal data class LiveMatch(
    val id: String,
    val title: String,
    val category: String,
    val poster: String = "",
    val popular: Boolean = false,
)

@Serializable
internal data class LiveStreamSource(
    val id: String,
    val streamNo: Int,
    val language: String = "",
    val hd: Boolean = false,
    val embedUrl: String = "",
    val viewers: Int = 0,
)

@Serializable
private data class MatchesResponse(
    val success: Boolean = false,
    val data: List<LiveMatchDto> = emptyList(),
)

@Serializable
private data class LiveMatchDto(
    val id: String,
    val title: String,
    val category: String,
    val poster: String = "",
    val popular: Boolean = false,
)

@Serializable
private data class DetailResponse(
    val success: Boolean = false,
    val data: LiveDetailDto? = null,
)

@Serializable
private data class LiveDetailDto(
    val id: String,
    val title: String,
    val category: String,
    val sources: List<LiveSourceDto> = emptyList(),
)

@Serializable
private data class LiveSourceDto(
    val id: String,
    val streamNo: Int,
    val language: String = "",
    val hd: Boolean = false,
    val embedUrl: String = "",
    val viewers: Int = 0,
)

internal object LiveApi {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    val categories = listOf("cricket", "football", "tennis", "basketball", "hockey", "baseball", "rugby", "combat")

    suspend fun getMatches(category: String): List<LiveMatch> = withContext(Dispatchers.Default) {
        try {
            val client = createApiHttpClient()
            val response = client.get("$SPORTSRC_API_BASE/?data=matches&category=$category")
            val body = readBoundedResponseBody(response.bodyAsChannel(), response.contentLength())
            val parsed = json.decodeFromString<MatchesResponse>(body)
            parsed.data.map { LiveMatch(it.id, it.title, it.category, it.poster, it.popular) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getSources(category: String, id: String): List<LiveStreamSource> = withContext(Dispatchers.Default) {
        try {
            val client = createApiHttpClient()
            val response = client.get("$SPORTSRC_API_BASE/?data=detail&category=$category&id=$id")
            val body = readBoundedResponseBody(response.bodyAsChannel(), response.contentLength())
            val parsed = json.decodeFromString<DetailResponse>(body)
            parsed.data?.sources?.map {
                LiveStreamSource(it.id, it.streamNo, it.language, it.hd, it.embedUrl, it.viewers)
            } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }
}
