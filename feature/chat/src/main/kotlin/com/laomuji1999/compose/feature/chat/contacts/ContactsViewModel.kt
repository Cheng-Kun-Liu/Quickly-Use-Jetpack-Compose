package com.laomuji1999.compose.feature.chat.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.common.dispatchers.IoCoroutineScope
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity
import com.laomuji1999.compose.core.logic.repository.contacts.ContactRepository
import com.laomuji1999.compose.core.ui.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    @IoCoroutineScope val ioCoroutineScope: CoroutineScope
) : ViewModel() {
    private val _contactMap: MutableStateFlow<Map<String, List<ContactInfoEntity>>> =
        MutableStateFlow(emptyMap())

    val uiState = _contactMap.asStateFlow().map { it ->
        ContactsScreenUiState(contactInfoList = it.map {
            ContactsScreenUiState.ContactInfo(
                category = it.key, contactList = it.value
            )
        })
    }.stateInTimeout(viewModelScope, ContactsScreenUiState())

    fun onAction(action: ContactsScreenAction) {
        when (action) {
            is ContactsScreenAction.UpdateContactList -> updateContactList()
        }
    }

    private fun updateContactList() {
        ioCoroutineScope.launch {
            contactsRepository.contactsList().collect { it ->
                _contactMap.value =
                    it.sortedBy { it.nickname }.groupBy { it.category }.toSortedMap()
            }
        }
    }
}