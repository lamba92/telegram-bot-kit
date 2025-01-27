package me.alllex.tbot.api.client

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.utils.EmptyContent
import io.ktor.content.TextContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import me.alllex.tbot.api.model.Chat
import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.api.model.InlineKeyboardButton
import me.alllex.tbot.api.model.InlineKeyboardMarkup
import me.alllex.tbot.api.model.InlineQuery
import me.alllex.tbot.api.model.InlineQueryId
import me.alllex.tbot.api.model.InlineQueryUpdate
import me.alllex.tbot.api.model.Message
import me.alllex.tbot.api.model.MessageId
import me.alllex.tbot.api.model.MessageUpdate
import me.alllex.tbot.api.model.UnixTimestamp
import me.alllex.tbot.api.model.UpdateType
import me.alllex.tbot.api.model.User
import me.alllex.tbot.api.model.UserId
import me.alllex.tbot.api.model.tryGetMe
import me.alllex.tbot.api.model.tryGetUpdates
import me.alllex.tbot.api.model.trySendMessage
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class TelegramBotApiClientTest {

    @Test
    fun sendsGetRequestAndReceivesResponse() = runTest {
        val engine = MockEngine { request ->
            assertThat(request.url.toString()).isEqualTo("https://bot.test/botToken/getMe")
            assertThat(request.method).isEqualTo(HttpMethod.Get)
            assertThat(request.body).isEqualTo(EmptyContent)

            respond(
                content = ByteReadChannel("""{"ok": true, "result": {"id": 525, "is_bot": true, "first_name": "Test Bot", "username": "test_bot"}}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = TelegramBotApiClient(
            apiToken = "Token",
            apiHost = "bot.test",
            httpClient = TelegramBotApiClient.httpClient(engine)
        )

        val actualResponse = client.tryGetMe()

        assertEquals(
            actual = actualResponse,
            expected = TelegramResponse(
                ok = true,
                result = User(
                    id = UserId(525),
                    isBot = true,
                    firstName = "Test Bot",
                    username = "test_bot"
                )
            )
        )
    }

    @Test
    fun sendsGetRequestAndReceivesFailingResponse() = runTest {
        val engine = MockEngine { request ->
            assertThat(request.url.toString()).isEqualTo("https://bot.test/botToken/getMe")
            assertThat(request.method).isEqualTo(HttpMethod.Get)
            assertThat(request.body).isEqualTo(EmptyContent)

            respond(
                content = ByteReadChannel("""{"ok": false, "description":"wrong parameter"}"""),
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = TelegramBotApiClient(
            apiToken = "Token",
            apiHost = "bot.test",
            httpClient = TelegramBotApiClient.httpClient(engine)
        )

        val actualResponse = client.tryGetMe()

        assertEquals(
            actual = actualResponse,
            expected = TelegramResponse(
                ok = false,
                description = "wrong parameter"
            )
        )
    }

    @Test
    fun sendsPostRequestAndReceivesResponse() = runTest {
        val engine = MockEngine { request ->
            assertThat(request.url.toString()).isEqualTo("https://bot.test/botToken/getUpdates")
            assertThat(request.method).isEqualTo(HttpMethod.Post)
            assertThat(request.body).isInstanceOf<TextContent>()
                .prop(TextContent::text).isEqualTo("""{"allowed_updates":["message","inline_query"]}""")

            respond(
                content = ByteReadChannel(
                    """{
                        "ok": true,
                        "result": [
                            {"update_id": 8000, "message": {"message_id": 1000, "date": 2023, "chat": {"id": 1, "type": "private"}, "text": "Message text"}},
                            {"update_id": 8001, "inline_query": {"id": "3001", "from": {"id": 2, "is_bot": false, "first_name": "TestUser"}, "query": "Query text", "offset": "test offset"}}
                        ]
                    }""".trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = TelegramBotApiClient(
            apiToken = "Token",
            apiHost = "bot.test",
            httpClient = TelegramBotApiClient.httpClient(engine)
        )

        val actualResponse = client.tryGetUpdates(
            allowedUpdates = listOf(UpdateType.MESSAGE, UpdateType.INLINE_QUERY)
        )

        assertEquals(
            actual = actualResponse,
            expected = TelegramResponse(
                ok = true,
                result = listOf(
                    MessageUpdate(
                        updateId = 8000,
                        message = Message(
                            messageId = MessageId(1000),
                            date = UnixTimestamp(2023),
                            chat = Chat(
                                id = ChatId(1),
                                type = "private",
                            ),
                            text = "Message text"
                        )
                    ),
                    InlineQueryUpdate(
                        updateId = 8001,
                        inlineQuery = InlineQuery(
                            id = InlineQueryId("3001"),
                            from = User(
                                id = UserId(2),
                                isBot = false,
                                firstName = "TestUser"
                            ),
                            query = "Query text",
                            offset = "test offset"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun sendsPostRequestWithUnionTypeField() = runTest {
        val engine = MockEngine { request ->
            assertThat(request.url.toString()).isEqualTo("https://bot.test/botToken/sendMessage")
            assertThat(request.method).isEqualTo(HttpMethod.Post)
            assertThat(request.body).isInstanceOf<TextContent>()
                .prop(TextContent::text)
                .isEqualTo("""{"chat_id":1,"text":"Hello","reply_markup":{"inline_keyboard":[[{"text":"Button"}]]}}""")

            respond(
                content = ByteReadChannel(
                    """{
                        "ok": true,
                        "result": {
                            "message_id": 1000,
                            "date": 2023,
                            "chat": {"id": 1, "type": "private"},
                            "text": "Message text"
                        }
                    }""".trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = TelegramBotApiClient(
            apiToken = "Token",
            apiHost = "bot.test",
            httpClient = TelegramBotApiClient.httpClient(engine)
        )

        val actualResponse = client.trySendMessage(
            chatId = ChatId(1),
            text = "Hello",
            replyMarkup = InlineKeyboardMarkup(
                inlineKeyboard = listOf(
                    listOf(
                        InlineKeyboardButton(
                            text = "Button",
                        )
                    )
                )
            )
        )

        assertEquals(
            actual = actualResponse,
            expected = TelegramResponse(
                ok = true,
                result = Message(
                    messageId = MessageId(1000),
                    date = UnixTimestamp(2023),
                    chat = Chat(
                        id = ChatId(1),
                        type = "private",
                    ),
                    text = "Message text"
                )
            )
        )
    }

}
