package com.laomuji1999.compose.feature.chat.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.laomuji1999.compose.core.logic.common.dispatchers.IoCoroutineScope
import com.laomuji1999.compose.core.logic.database.dao.ContactDao
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity
import com.laomuji1999.compose.core.logic.model.entity.MessageInfoEntity
import com.laomuji1999.compose.core.logic.notification.NotificationHelper
import com.laomuji1999.compose.core.logic.repository.chat.ChatRepository
import com.laomuji1999.compose.core.ui.extension.emitGraph
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    contactDao: ContactDao,
    private val chatRepository: ChatRepository,
    private val notificationHelper: NotificationHelper,
    @IoCoroutineScope val ioCoroutineScope: CoroutineScope
) : ViewModel() {
    private val _graph = MutableSharedFlow<ChatScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    private val account = savedStateHandle.toRoute<ChatScreenRoute>().account
    private val _contactInfo = MutableStateFlow<ContactInfoEntity?>(null)
    private val _messageList = MutableStateFlow<List<MessageInfoEntity>>(emptyList())

    init {
        ioCoroutineScope.launch {
            _contactInfo.value = contactDao.getByAccount(account)
            chatRepository.getMessageList(account).collect {
                _messageList.value = it
            }
        }
    }

    private val _inputText = MutableStateFlow("")

    val uiState =
        combine(_contactInfo, _messageList, _inputText) { contactInfo, messageList, inputText ->
            ChatScreenUiState(
                receiveAvatar = contactInfo?.avatar ?: "",
                nickname = contactInfo?.nickname ?: "",
                messageList = messageList,
                inputText = inputText
            )
        }.stateInTimeout(viewModelScope, ChatScreenUiState())

    fun onAction(action: ChatScreenAction) {
        when (action) {
            is ChatScreenAction.SetInputText -> setInputText(action.text)
            ChatScreenAction.SendInputText -> sendInputText()
            ChatScreenAction.DismissNotification -> dismissNotification()
            ChatScreenAction.OnClickBack -> _graph.emitGraph(ChatScreenRoute.Graph.Back)
        }
    }

    private fun setInputText(text: String) {
        _inputText.value = text
    }

    private fun sendInputText() {
        val contactInfo = _contactInfo.value ?: return
        if (_inputText.value.isEmpty()) {
            return
        }
        ioCoroutineScope.launch {
            chatRepository.sendMessage(contactInfo.account, _inputText.value, contactInfo.nickname)
            _inputText.value = ""
        }
    }

    private fun dismissNotification() {
        val contactInfo = _contactInfo.value ?: return
        notificationHelper.dismissNotification(contactInfo)
    }
}