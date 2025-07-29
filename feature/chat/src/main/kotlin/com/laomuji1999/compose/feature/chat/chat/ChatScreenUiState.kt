package com.laomuji1999.compose.feature.chat.chat

import com.laomuji1999.compose.core.logic.model.entity.MessageInfoEntity

data class ChatScreenUiState(
    val sendAvatar: String = "",
    val receiveAvatar: String = "",
    val nickname: String = "",
    val messageList: List<MessageInfoEntity> = emptyList(),
    val inputText: String = "",
)
