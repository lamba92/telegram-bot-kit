package me.alllex.tbot.api.client

import kotlinx.serialization.Serializable
import me.alllex.tbot.api.model.ResponseParameters

@Serializable
data class TelegramResponse<T>(
    val result: T? = null,
    val ok: Boolean,
    val description: String? = null,
    val errorCode: Int? = null,
    val parameters: ResponseParameters? = null
)

@Throws(TelegramBotApiException::class)
fun <T> TelegramResponse<T>.getResultOrThrow(): T {
    if (ok && result != null) {
        return result
    }

    throw TelegramBotApiException(this)
}
