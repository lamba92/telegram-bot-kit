package me.alllex.tbot.apigen

import java.io.File

private val unionTypesWithExplicitMarker = setOf(
    "PassportElementError"
)

data class MethodVariation(
    val methodName: String,
    val requiredParams: List<String>,
    val skipParams: List<String>,
    val newMethodName: String,
    val returnType: String,
    val descriptionSubstitutions: List<Pair<Regex, String>> = emptyList(),
)

private val editedMessageDescriptionSubstitutions = listOf(
    "On success, if the( edited)? message is not an inline message, the( edited)? Message is returned, otherwise True is returned.".toRegex() to "On success the edited Message is returned."
)

private val editedInlineMessageDescriptionSubstitutions = listOf(
    "On success, if the( edited)? message is not an inline message, the( edited)? Message is returned, otherwise True is returned.".toRegex() to "On success True is returned."
)

val methodVariations = listOf(
    MethodVariation(
        "editMessageText", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageText", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageText", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageText", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageCaption", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageCaption", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageCaption", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageCaption", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageMedia", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageMedia", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageMedia", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageMedia", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageLiveLocation", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageLiveLocation", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageLiveLocation", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageLiveLocation", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageReplyMarkup", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageReplyMarkup", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageReplyMarkup", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageReplyMarkup", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "stopMessageLiveLocation", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "stopMessageLiveLocation", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "stopMessageLiveLocation", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "stopInlineMessageLiveLocation", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "setGameScore", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "setGameScore", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "setGameScore", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "setInlineGameScore", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),
)

data class ValueType(
    val name: String,
    val backingType: String,
    val docString: String? = null,
    val fieldPredicate: (type: BotApiElementName, field: BotApiElement.Field) -> Boolean
)

val valueTypes = listOf(
    ValueType("ChatId", "Long", "Chat identifier.") { type, field ->
        (type.value == "Chat" && field.serialName == "id") || field.serialName.endsWith("chat_id")
    },
    ValueType("UserId", "Long", "User identifier.") { type, field ->
        (type.value == "User" && field.serialName == "id") || field.serialName.endsWith("user_id")
    },
    ValueType("MessageId", "Long", "Opaque message identifier.") { _, field ->
        !field.serialName.endsWith("inline_message_id") && field.serialName.endsWith("message_id")
    },
    ValueType("InlineMessageId", "String", "Opaque inline message identifier.") { _, field ->
        field.serialName.endsWith("inline_message_id")
    },
    ValueType("MessageThreadId", "Long", "Opaque message thread identifier.") { _, field ->
        field.serialName.endsWith("message_thread_id")
    },
    ValueType("CallbackQueryId", "String", "Opaque [CallbackQuery] identifier.") { type, field ->
        (type.value == "CallbackQuery" && field.serialName == "id") || field.serialName.endsWith("callback_query_id")
    },
    ValueType("InlineQueryId", "String", "Opaque [InlineQuery] identifier.") { type, field ->
        (type.value == "InlineQuery" && field.serialName == "id") || field.serialName.endsWith("inline_query_id")
    },
    ValueType("InlineQueryResultId", "String", "Opaque [InlineQueryResult] identifier.") { type, field ->
        (type.value.startsWith("InlineQuery") && field.serialName == "id") ||
            (type.value == "ChosenInlineResult" && field.serialName == "result_id")
    },
    ValueType("FileId", "String", "Identifier for a file, which can be used to download or reuse the file.") { _, field ->
        field.serialName.endsWith("file_id")
    },
    ValueType(
        "FileUniqueId",
        "String",
        "Unique identifier for a file, which is supposed to be the same over time and for different bots.\n\nIt can't be used to download or reuse the file."
    ) { _, field ->
        field.serialName.endsWith("file_unique_id")
    },
    ValueType("ShippingQueryId", "String", "Opaque [ShippingQuery] identifier.") { type, field ->
        (type.value == "ShippingQuery" && field.serialName == "id") || field.serialName.endsWith("shipping_query_id")
    },
    ValueType("WebAppQueryId", "String", "Opaque web-app query identifier.") { _, field ->
        field.serialName.endsWith("web_app_query_id")
    },
    ValueType("CustomEmojiId", "String", "Opaque custom emoji identifier.") { _, field ->
        field.serialName.endsWith("custom_emoji_id")
    },
    ValueType("Seconds", "Long", "Duration in seconds.") { _, field ->
        field.serialName in setOf("cache_time", "duration", "life_period", "open_period", "timeout", "retry_after")
    },
    ValueType(
        "UnixTimestamp",
        "Long",
        "Unix time - **number of seconds** that have elapsed since 00:00:00 UTC on 1 January 1970.",
    ) { _, field ->
        field.serialName in setOf(
            "date",
            "last_error_date",
            "last_synchronization_error_date",
            "emoji_status_expiration_date",
            "forward_date",
            "edit_date",
            "close_date",
            "start_date",
            "expire_date",
            "until_date",
            "file_date",
        )
    },
)

private val unionMarkerValueInDescriptionRe = Regex("""(?:must be|always) ["“”]?([\w_]+)["“”]?""")

private val unionMarkerFieldNames = setOf("type", "status")

fun BotApiElement.Field.isUnionDiscriminator(): Boolean {
    return serialName in unionMarkerFieldNames
}

fun BotApiElement.getUnionDiscriminatorFieldName(): String? {
    return fields?.find { it.isUnionDiscriminator() }?.serialName
}

private fun BotApiElementName.asMethodNameToRequestTypeName() = "${value.toTitleCase()}Request"

data class FluentContextMethod(
    val receiver: String,
    val name: String,
    val delegateName: String,
    val args: Map<String, String>
) {
    constructor(receiver: String, name: String, args: Map<String, String>) : this(receiver, name, name, args)
}

/**
 * Fluent methods move some of the parameters from the base method signatures
 * into a direct receiver of the function, thus making it more idiomatic.
 * The ability to call a base method is retained by having `TelegramBotApiContext` as a context receiver.
 *
 * Fluent methods generation happens after the [methodVariations] are expanded.
 */
val fluentMethods = listOf(
    FluentContextMethod("Chat", "sendMessage", "sendMessage", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendMarkdown", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.MARKDOWN")),
    FluentContextMethod("Chat", "sendMarkdownV2", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.MARKDOWN_V2")),
    FluentContextMethod("Chat", "sendHtml", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.HTML")),
    FluentContextMethod("ChatId", "sendMessage", "sendMessage", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendMarkdown", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.MARKDOWN")),
    FluentContextMethod("ChatId", "sendMarkdownV2", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.MARKDOWN_V2")),
    FluentContextMethod("ChatId", "sendHtml", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.HTML")),

    FluentContextMethod("Message", "reply", "sendMessage", mapOf("chatId" to "chat.id", "replyToMessageId" to "messageId")),
    FluentContextMethod(
        "Message", "replyMarkdown",
        "sendMessage", mapOf("chatId" to "chat.id", "replyToMessageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN")
    ),
    FluentContextMethod(
        "Message", "replyMarkdownV2",
        "sendMessage", mapOf("chatId" to "chat.id", "replyToMessageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN_V2")
    ),
    FluentContextMethod(
        "Message", "replyHtml",
        "sendMessage", mapOf("chatId" to "chat.id", "replyToMessageId" to "messageId", "parseMode" to "ParseMode.HTML")
    ),

    FluentContextMethod(
        "Message", "editText",
        "editMessageText", mapOf("chatId" to "chat.id", "messageId" to "messageId")
    ),
    FluentContextMethod(
        "Message", "editTextMarkdown",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN")
    ),
    FluentContextMethod(
        "Message", "editTextMarkdownV2",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN_V2")
    ),
    FluentContextMethod(
        "Message", "editTextHtml",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.HTML")
    ),

    FluentContextMethod("Message", "delete", "deleteMessage", mapOf("chatId" to "chat.id", "messageId" to "messageId")),
    FluentContextMethod("Message", "forward", "forwardMessage", mapOf("fromChatId" to "chat.id", "messageId" to "messageId")),
    FluentContextMethod("Message", "copyMessage", "copyMessage", mapOf("fromChatId" to "chat.id", "messageId" to "messageId")),

    FluentContextMethod("Chat", "sendPhoto", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendAudio", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendDocument", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVideo", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendAnimation", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVoice", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVideoNote", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendMediaGroup", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendLocation", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVenue", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendContact", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendPoll", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendDice", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendChatAction", mapOf("chatId" to "id")),

    FluentContextMethod("Chat", "getMemberCount", "getChatMemberCount", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "getMember", "getChatMember", mapOf("chatId" to "id")),

    FluentContextMethod("ChatId", "sendPhoto", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendAudio", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendDocument", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVideo", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendAnimation", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVoice", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVideoNote", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendMediaGroup", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendLocation", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVenue", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendContact", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendPoll", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendDice", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendChatAction", mapOf("chatId" to "this")),

    FluentContextMethod("ChatId", "getMemberCount", "getChatMemberCount", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "getMember", "getChatMember", mapOf("chatId" to "this")),

    FluentContextMethod("User", "getProfilePhotos", mapOf("userId" to "id")),

    FluentContextMethod("UserId", "getProfilePhotos", mapOf("userId" to "this")),

    FluentContextMethod("CallbackQuery", "answer", "answerCallbackQuery", mapOf("callbackQueryId" to "id")),
    FluentContextMethod("CallbackQueryId", "answer", "answerCallbackQuery", mapOf("callbackQueryId" to "this")),

    FluentContextMethod("InlineQuery", "answer", "answerInlineQuery", mapOf("inlineQueryId" to "id")),
    FluentContextMethod("InlineQueryId", "answer", "answerInlineQuery", mapOf("inlineQueryId" to "this")),
)

class BotApiGenerator {

    fun run(
        html: String,
        outputDirectory: File,
        packageName: String = "",
        wrapperPackageName: String = "",
    ) {
        val parser = BotApiDefinitionParser()
        val botApi = parser.run(html)

        val allTypes = botApi.types
        println("Parsed ${allTypes.size} types")
        val allMethods = botApi.methods
        println("Parsed ${allMethods.size} methods")

        val unionTypes = collectUnionTypes(allTypes)
        val unionTypeParentByChild = unionTypes
            .flatMap { (parent, children) -> children.map { it to parent } }
            .toMap()

        val typesFileText = generateTypesFile(allTypes, unionTypeParentByChild, packageName)
        val requestTypesFileText = generateRequestTypesFile(allMethods, packageName)

        val methodsSourceCodes = generateMethodsSourceCode(allMethods, packageName, wrapperPackageName)

        val outputPackageDir = outputDirectory.resolve(packageName.replace(".", "/"))
        outputPackageDir.mkdirs()

        outputPackageDir.resolve("Types.kt").writeText(typesFileText)
        outputPackageDir.resolve("RequestTypes.kt").writeText(requestTypesFileText)
        outputPackageDir.resolve("TryRequestMethods.kt").writeText(methodsSourceCodes.tryRequestMethodSourceCode)
        outputPackageDir.resolve("TryMethods.kt").writeText(methodsSourceCodes.tryMethodSourceCode)
        outputPackageDir.resolve("TryWithContextMethods.kt").writeText(methodsSourceCodes.tryWithContextMethodSourceCode)
        outputPackageDir.resolve("Methods.kt").writeText(methodsSourceCodes.methodSourceCode)
        outputPackageDir.resolve("WithContextMethods.kt").writeText(methodsSourceCodes.withContextMethodSourceCode)
    }

    private fun collectUnionTypes(elements: List<BotApiElement>): Map<BotApiElementName, List<BotApiElementName>> {
        return buildMap {
            for (el in elements) {
                if (el.unionTypes != null) {
                    put(el.name, el.unionTypes)
                }
            }
        }
    }

    private fun generateRequestTypesFile(
        methodElements: List<BotApiMethod>,
        packageName: String
    ): String = buildString {

        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
            appendLine()
        }
        appendLine("import kotlinx.serialization.Serializable")
        appendLine("import kotlinx.serialization.encodeToString")
        appendLine()
        appendLine()

        for (el in methodElements) {
            val requestTypeSourceCodeText = generateRequestTypeSourceCode(el.name, el.parameters)
            if (requestTypeSourceCodeText.isNotEmpty()) {
                appendLine(requestTypeSourceCodeText)
            }
        }
    }

    private fun generateRequestTypeSourceCode(
        elementName: BotApiElementName,
        parameters: List<BotApiElement.Field>,
    ): String = buildString {

        if (parameters.isNotEmpty()) {
            val requestTypeName = elementName.asMethodNameToRequestTypeName()

            appendLine("/**")
            appendLine(" * Request body for [${elementName.value}].")
            appendLine(" *")
            parameters.filter { it.description.isNotEmpty() }.forEach {
                appendLine(" * @param ${it.name} ${it.description}")
            }
            appendLine(" */")
            appendLine("@Serializable")
            appendLine("data class $requestTypeName(")
            for (parameter in parameters) {
                appendFieldLine(elementName, parameter, true)
            }
            appendLine(") {")
            appendLine("    ${generateDebugToString(requestTypeName, parameters)}")

            appendLine("}")
        }
    }

    private fun StringBuilder.header(packageName: String, wrapperPackageName: String, imports: List<String> = emptyList()) {
        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
        }
        appendLine()
        for (importStatement in imports) {
            appendLine(importStatement)
        }
        if (wrapperPackageName.isNotEmpty() && wrapperPackageName != packageName) {
            appendLine("import $wrapperPackageName.*")
        }
        appendLine()
        appendLine()
    }

    private fun generateMethodsSourceCode(
        methodElements: List<BotApiMethod>,
        packageName: String,
        wrapperPackageName: String
    ): MethodOverloadsSourceCode {

        val methodsSourceCodes = methodElements.flatMap { method ->
            val requestTypeName = method.name.asMethodNameToRequestTypeName()

            val variations = methodVariations.filter { it.methodName == method.name.value }

            if (variations.isEmpty()) {
                return@flatMap listOf(generateMethodSourceCode(method, method.name.value, requestTypeName, useArgNames = false))
            }

            variations.map { variation ->
                val variationMethod = method.copy(
                    parameters = method.parameters
                        .filterNot { it.serialName in variation.skipParams }
                        .map {
                            if (it.serialName in variation.requiredParams) it.copy(isOptional = false, defaultValue = null) else it
                        },
                    returnType = KotlinType(variation.returnType),
                    description = variation.descriptionSubstitutions.fold(method.description) { acc, replacement ->
                        acc.replace(replacement.first, replacement.second)
                    }
                )

                generateMethodSourceCode(variationMethod, variation.newMethodName, requestTypeName, useArgNames = true)
            }
        }

        val tryRequestMethodsSourceCode = buildString {
            header(
                packageName, wrapperPackageName, listOf(
                    "import io.ktor.client.call.*",
                    "import io.ktor.client.request.*",
                    "import io.ktor.http.*",
                )
            )

            methodsSourceCodes.map { it.tryRequestMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { appendLine(it) }
        }

        val tryMethodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.tryMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val tryWithContextMethodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.tryWithContextMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val methodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.methodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        val withContextMethodsSourceCode = buildString {
            header(packageName, wrapperPackageName)
            methodsSourceCodes.map { it.withContextMethodSourceCode }.filter { it.isNotEmpty() }
                .forEach { append(it) }
        }

        return MethodOverloadsSourceCode(
            tryRequestMethodsSourceCode,
            tryMethodsSourceCode,
            tryWithContextMethodsSourceCode,
            methodsSourceCode,
            withContextMethodsSourceCode
        )
    }

    private fun StringBuilder.appendParameters(elementName: BotApiElementName, parameters: List<BotApiElement.Field>) {
        if (parameters.isNotEmpty()) {
            appendLine()
        }
        for (parameter in parameters) {
            appendFieldLine(elementName, parameter, isProperty = false)
        }
    }

    private fun StringBuilder.appendMethodDoc(description: String, parameters: List<BotApiElement.Field>) {
        appendLine("/**")
        appendKdocLines(description)
        if (parameters.isNotEmpty()) {
            appendLine(" *")
        }
        parameters.filter { it.description.isNotEmpty() }.forEach {
            appendLine(" * @param ${it.name} ${it.description}")
        }
        appendLine(" */")
    }

    data class MethodOverloadsSourceCode(
        val tryRequestMethodSourceCode: String,
        val tryMethodSourceCode: String,
        val tryWithContextMethodSourceCode: String,
        val methodSourceCode: String,
        val withContextMethodSourceCode: String,
    )

    private fun generateMethodSourceCode(
        method: BotApiMethod,
        methodBaseName: String,
        requestTypeName: String,
        useArgNames: Boolean
    ): MethodOverloadsSourceCode {

        val apiMethodName = method.name
        val description = method.description
        val parameters = method.parameters
        val returnType = method.returnType

        val tryMethodName = "try${methodBaseName.toTitleCase()}"

        val hasParams = parameters.isNotEmpty()

        val requestValueArg =
            if (!hasParams) ""
            else "${requestTypeName}(${parameters.joinToString { if (useArgNames) "${it.name} = ${it.name}" else it.name }})"

        // Core try-prefixed method that does the actual request
        val httpMethod = if (parameters.isEmpty()) "get" else "post"

        val tryRequestMethodSourceCode = buildString {
            appendLine("/**")
            appendKdocLines(description)
            appendLine(" */")
            append("suspend fun TelegramBotApiClient.$tryMethodName(")
            if (hasParams) {
                append("requestBody: $requestTypeName")
            }
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    httpClient.$httpMethod {")
            appendLine("        url {")
            appendLine("            protocol = apiProtocol")
            appendLine("            host = apiHost")
            appendLine("            port = apiPort")
            appendLine("            path(\"bot\$apiToken\", \"$apiMethodName\")")
            appendLine("        }")
            if (hasParams) {
                appendLine("        contentType(ContentType.Application.Json)")
                appendLine("        setBody(requestBody)")
            }
            appendLine("    }.body()")
        }

        val tryMethodSourceCode = buildString {
            if (!hasParams) return@buildString

            appendLine()
            appendMethodDoc(description, parameters)
            append("suspend fun TelegramBotApiClient.$tryMethodName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    $tryMethodName($requestValueArg)")
        }

        // Convenience try-prefixed method with context
        val tryWithContextMethodSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("context(TelegramBotApiContext)")
            append("suspend fun $tryMethodName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): TelegramResponse<${returnType.value}> =")
            appendLine("    botApiClient.$tryMethodName($requestValueArg)")
        }

        // Convenience method
        val methodSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("@Throws(TelegramBotApiException::class)")
            append("suspend fun TelegramBotApiClient.$methodBaseName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): ${returnType.value} =")
            appendLine("    $tryMethodName($requestValueArg).getResultOrThrow()")
        }

        // Convenience method with context
        val methodWithContextSourceCode = buildString {
            appendLine()
            appendMethodDoc(description, parameters)
            appendLine("context(TelegramBotApiContext)")
            appendLine("@Throws(TelegramBotApiException::class)")
            append("suspend fun $methodBaseName(")
            appendParameters(apiMethodName, parameters)
            appendLine("): ${returnType.value} =")
            appendLine("    botApiClient.$tryMethodName($requestValueArg).getResultOrThrow()")
        }

        val fluentContextMethods = fluentMethods.filter { it.delegateName == methodBaseName }.map { fluent ->
            val unexpectedArgs = fluent.args.keys - parameters.map { it.name }.toSet()
            check(unexpectedArgs.isEmpty()) { "Unexpected replacement args for fluent method ${fluent.name}/$methodBaseName: $unexpectedArgs" }

            buildString {
                appendLine("context(TelegramBotApiContext)")
                appendLine("@Throws(TelegramBotApiException::class)")
                append("suspend fun ${fluent.receiver}.${fluent.name}(")
                appendParameters(apiMethodName, parameters.filter { it.name !in fluent.args })
                appendLine("): ${returnType.value} =")
                val adjustedRequestArg = parameters.map { it.name }.joinToString { fluent.args[it] ?: it }
                appendLine("    ${fluent.delegateName}($adjustedRequestArg)")
            }
        }

        val combinedMethodWithContextSourceCode = buildString {
            append(methodWithContextSourceCode)
            fluentContextMethods.forEach {
                appendLine()
                append(it)
            }
        }

        return MethodOverloadsSourceCode(
            tryRequestMethodSourceCode,
            tryMethodSourceCode,
            tryWithContextMethodSourceCode,
            methodSourceCode,
            combinedMethodWithContextSourceCode,
        )
    }

    private fun generateTypesFile(
        typeElements: List<BotApiElement>,
        unionTypeParentByChild: Map<BotApiElementName, BotApiElementName>,
        packageName: String = "",
    ) = buildString {

        appendLine("@file:OptIn(ExperimentalSerializationApi::class)")
        appendLine()

        if (packageName.isNotEmpty()) {
            appendLine("package $packageName")
            appendLine()
        }
        appendLine("import kotlinx.serialization.json.*")
        appendLine("import kotlinx.serialization.DeserializationStrategy")
        appendLine("import kotlinx.serialization.ExperimentalSerializationApi")
        appendLine("import kotlinx.serialization.SerialName")
        appendLine("import kotlinx.serialization.Serializable")
        appendLine("import kotlinx.serialization.encodeToString")
        appendLine()

        val specialTypes: Map<String, StringBuilder.(BotApiElement) -> Unit> = mapOf(
            "Update" to { generateSourceCodeForUpdate(it) },
        )

        val processedSpecialTypes = mutableSetOf<String>()

        for (el in typeElements) {
            val specialSourceGeneration = specialTypes[el.name.value]
            if (specialSourceGeneration != null) {
                processedSpecialTypes += el.name.value
                specialSourceGeneration(el)
            } else if (el.fields != null) {
                appendLine(generateContentfulTypeSourceCode(el.name, el.description, el.fields, unionTypeParentByChild))
            } else if (el.unionTypes != null) {
                val unionTypes = el.unionTypes.map {
                    typeElements.find { typeElement -> typeElement.name == it } ?: error("Unknown union type: $it")
                }
                appendLine(generateUnionTypeSourceCode(el.name, el.description, unionTypes))
            } else {
                error("Unknown type: $el")
            }
        }

        val unprocessedSpecialTypes = specialTypes.keys - processedSpecialTypes
        check(unprocessedSpecialTypes.isEmpty()) {
            "Special types not processed: $unprocessedSpecialTypes"
        }

        for (valueType in valueTypes) {
            appendLine(generateValueTypeSourceCode(valueType))
        }
    }

    private fun generateValueTypeSourceCode(valueType: ValueType): String = buildString {
        valueType.docString?.let { appendKdoc(it) }
        appendLine("@Serializable")
        appendLine("@JvmInline")
        appendLine("value class ${valueType.name}(val value: ${valueType.backingType}) {")
        appendLine("    override fun toString(): String = \"${valueType.name}(\${quoteWhenWhitespace(value)})\"")
        appendLine("}")
    }

    private fun StringBuilder.generateSourceCodeForUpdate(updateTypeElement: BotApiElement) {
        val name = updateTypeElement.name.value
        val types = updateTypeElement.fields?.filter { it.isOptional } ?: emptyList()

        fun BotApiElement.Field.enumValue() = serialName.snakeToLoudSnakeCase()
        fun BotApiElement.Field.updateTypeName() = serialName.snakeToPascalCase() + "Update"

        appendLine("/**")
        appendLine(" * Type of updates Telegram Bot can receive.")
        appendLine(" */")
        appendLine("enum class ${name}Type {")
        for (updateField in types) {
            appendLine("    @SerialName(\"${updateField.serialName}\") ${updateField.enumValue()},")
        }
        appendLine("}")

        appendLine()
        appendLine("/**")
        updateTypeElement.description.split("\n")
            .filterNot { it.startsWith("At most one of the optional parameters") }
            .let { appendKdocLines(it) }
        appendLine(" *")
        appendLine(" * Sub-types:")
        for (updateType in types) {
            appendLine(" * - [${updateType.updateTypeName()}]")
        }
        appendLine(" */")
        appendLine("@Serializable(with = ${name}Serializer::class)")
        appendLine("sealed interface $name {")
        appendLine("    val updateId: Long")
        appendLine("    val updateType: UpdateType")
        appendLine("}")
        for (updateField in types) {
            appendLine()
            appendKdoc(updateField.description.removePrefix("Optional."))
            appendLine("@Serializable")
            appendLine("data class ${updateField.updateTypeName()}(")
            appendLine("    override val updateId: Long,")
            appendLine("    val ${updateField.name}: ${updateField.type},")
            appendLine("): $name {")
            appendLine("    override val updateType: UpdateType get() = UpdateType.${updateField.enumValue()}")
            appendLine("}")
        }

        appendLine()
        appendLine("object ${name}Serializer : JsonContentPolymorphicSerializer<$name>($name::class) {")
        appendLine("    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<$name> {")
        appendLine("        val json = element.jsonObject")
        appendLine("        return when {")
        for (updateField in types) {
            appendLine("            \"${updateField.serialName}\" in json -> ${updateField.updateTypeName()}.serializer()")
        }
        // TODO: do not fail here, as new update types may be added in the future, and we want the bot to gracefully ignore them
        appendLine("            else -> error(\"Failed to deserialize an update type: \$json\")")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
    }

    private fun generateUnionTypeSourceCode(
        name: BotApiElementName,
        description: String,
        unionTypes: List<BotApiElement>
    ): String = buildString {

        appendLine("/**")
        appendKdocLines(description)
        for (unionType in unionTypes) {
            appendLine(" * - [${unionType.name.value}]")
        }
        appendLine(" */")

        val discriminatorFieldNames = unionTypes.mapNotNull { it.getUnionDiscriminatorFieldName() }
            .toSet()

        val discriminatorFieldName = when (discriminatorFieldNames.size) {
            0 -> null
            1 -> discriminatorFieldNames.single()
            else -> error("Multiple discriminator field names found: $discriminatorFieldNames")
        }

        if (discriminatorFieldName == null) {
            appendLine("@Serializable(with = ${name}Serializer::class)")
        } else {
            appendLine("@Serializable")
            appendLine("@JsonClassDiscriminator(\"$discriminatorFieldName\")")
        }

        appendLine("sealed interface ${name.value}")

        if (discriminatorFieldName == null) {
            val avoidFields = setOf("description")
            val discriminatorFieldByType = unionTypes.associate { unionType ->
                val otherFieldNames = unionTypes
                    .filter { it != unionType }
                    .flatMap { it.fields ?: emptyList() }
                    .map { it.serialName }
                    .toSet()
                val discriminatorField = unionType.fields?.find { it.serialName !in otherFieldNames && it.serialName !in avoidFields }
                    ?: error("Failed to find discriminator field for type ${unionType.name}, with fields: ${unionType.fields?.map { it.serialName }}")
                unionType.name to discriminatorField
            }

            appendLine()
            appendLine("object ${name}Serializer : JsonContentPolymorphicSerializer<$name>($name::class) {")
            appendLine("    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<$name> {")
            appendLine("        val json = element.jsonObject")
            appendLine("        return when {")
            for (unionType in unionTypes) {
                val discriminatorField = discriminatorFieldByType[unionType.name]
                    ?: error("Failed to find discriminator field for type ${unionType.name}")
                appendLine("            \"${discriminatorField.serialName}\" in json -> ${unionType.name.value}.serializer()")
            }
            appendLine("            else -> error(\"Failed to deserialize an update type: \$json\")")
            appendLine("        }")
            appendLine("    }")
            appendLine("}")
        }
    }

    private fun generateContentfulTypeSourceCode(
        typeName: BotApiElementName,
        description: String,
        fields: List<BotApiElement.Field>,
        unionTypeParentByChild: Map<BotApiElementName, BotApiElementName>
    ): String = buildString {

        val sealedParentName = unionTypeParentByChild[typeName]

        // For sealed classes the "type" field will be automatically added by kotlinx.serialization
        // See: https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/polymorphism.md#custom-subclass-serial-name
        val trueFields = when {
            sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker -> {
                fields.filterNot { it.isUnionDiscriminator() }
            }

            else -> fields
        }

        appendLine("/**")
        appendKdocLines(description)
        trueFields.filter { it.description.isNotEmpty() }.forEach {
            appendLine(" * @param ${it.name} ${it.description}")
        }
        appendLine(" */")
        appendLine("@Serializable")
        if (sealedParentName != null && sealedParentName.value !in unionTypesWithExplicitMarker) {
            val markerField = fields.firstOrNull { it.isUnionDiscriminator() }
            if (markerField != null) {
                val fieldDescription = markerField.description
                val unionMarkerValue = unionMarkerValueInDescriptionRe.find(fieldDescription)
                    ?.groupValues?.get(1)
                    ?: error("Can't find union marker value for ${typeName.value} in description: '$fieldDescription'")
                appendLine("@SerialName(\"${unionMarkerValue}\")")
            }
        }

        if (trueFields.isEmpty()) {
            // https://kotlinlang.org/docs/object-declarations.html#data-objects
            append("data object ")
            append(typeName.value)
            if (sealedParentName != null) {
                append(" : ${sealedParentName.value}")
            }
            appendLine()
        } else {
            appendLine("data class ${typeName.value}(")

            for (field in trueFields) {
                appendFieldLine(typeName, field, true)
            }

            append(")")
            if (sealedParentName != null) {
                append(" : ${sealedParentName.value}")
            }
            appendLine(" {")
            appendLine("    ${generateDebugToString(typeName.value, trueFields)}")
            appendLine("}")
        }
    }

    private fun generateDebugToString(typeName: String, fields: List<BotApiElement.Field>): String = buildString {
        append("override fun toString() = DebugStringBuilder(\"$typeName\")")
        append(fields.map { it.name }.joinToString("") { ".prop(\"$it\", $it)" })
        append(".toString()")
    }

    private fun resolveFieldType(elementType: BotApiElementName, field: BotApiElement.Field): String {
        val specType = field.type.value

        valueTypes.find { it.backingType == specType && it.fieldPredicate(elementType, field) }?.let {
            return it.name
        }

        return specType
    }

    private fun StringBuilder.appendFieldLine(elementType: BotApiElementName, field: BotApiElement.Field, isProperty: Boolean) {
        appendLine {
            append("    ")
            if (isProperty) {
                append("val ")
            }
            append(field.name)
            append(": ")
            val type = resolveFieldType(elementType, field)
            append(type)
            if (field.isOptional) {
                append("?")
            }
            if (field.defaultValue != null) {
                append(" = ${field.defaultValue}")
            }
            append(",")
        }
    }

    companion object {

        fun generateFromSpec(
            telegramApiHtmlSpec: String,
            outputDirectory: File,
            packageName: String,
            wrapperPackageName: String,
        ) {
            BotApiGenerator().run(
                telegramApiHtmlSpec,
                outputDirectory,
                packageName,
                wrapperPackageName,
            )
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val specFile = args.firstOrNull()
                ?: error("Telegram Bot API HTML spec file is not specified as the first argument")
            val outputDirectory = args.getOrNull(1)
                ?: error("Output directory is not specified as the second argument")

            generateFromSpec(
                telegramApiHtmlSpec = File(specFile).readText(),
                outputDirectory = File(outputDirectory),
                packageName = "me.alllex.tbot.api.model",
                wrapperPackageName = "me.alllex.tbot.api.client",
            )
        }
    }

}
