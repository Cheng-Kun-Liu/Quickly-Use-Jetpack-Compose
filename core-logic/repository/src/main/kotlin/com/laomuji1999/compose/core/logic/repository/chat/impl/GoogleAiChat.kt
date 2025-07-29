package com.laomuji1999.compose.core.logic.repository.chat.impl

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.laomuji1999.compose.core.logic.common.BuildConfig
import com.laomuji1999.compose.core.logic.database.dao.ContactDao
import com.laomuji1999.compose.core.logic.database.dao.MessageDao
import com.laomuji1999.compose.core.logic.model.entity.MessageInfoEntity
import com.laomuji1999.compose.core.logic.notification.NotificationHelper
import com.laomuji1999.compose.core.logic.repository.chat.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

internal class GoogleAiChat(
    private val contactDao: ContactDao,
    private val messageDao: MessageDao,
    private val notificationHelper: NotificationHelper
) : ChatRepository {

    private val coroutineScope =
        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

    private suspend fun getHistory(account: Long) =
        messageDao.getMessageList(account = account).first().map { message ->
            content(
                role = if (message.isSend) {
                    "user"
                } else {
                    "model"
                }
            ) {
                text(message.text)
            }
        }

    override fun sendMessage(
        account: Long,
        text: String,
        nickname: String
    ) {
        messageDao.insert(
            MessageInfoEntity(
                account = account,
                text = text,
                isSend = true
            )
        )
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-pro-latest",
            apiKey = BuildConfig.GEMINI_API_KEY,
            systemInstruction = content {
                text("请像友好的${nickname}一样回复此聊天对话")
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
            )
        )
        coroutineScope.launch {
            try {
                generativeModel.startChat(history = getHistory(account = account))
                    .sendMessage(text).text
            } catch (e: Exception) {
                e.message
            }?.let {
                val filterText = if (it.endsWith("\n")) {
                    it.substring(0, it.length - 1)
                } else {
                    it
                }
                val messageInfoEntity = MessageInfoEntity(
                    account = account,
                    text = filterText,
                    isSend = false
                )
                messageDao.insert(messageInfoEntity)
                notificationHelper.showNotification(
                    contactInfoEntity = contactDao.getByAccount(account),
                    messageInfoEntity = messageInfoEntity
                )
            }
        }
    }

    override fun getMessageList(account: Long): Flow<List<MessageInfoEntity>> =
        messageDao.getMessageList(account)
}