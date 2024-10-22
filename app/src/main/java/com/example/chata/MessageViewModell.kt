package com.example.chata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {

    private val messageRepository: MessageRepository = MessageRepository(Injection.instance())
    private val userRepository: UserRepository = UserRepository(FirebaseAuth.getInstance(),
        Injection.instance())
    init {
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser : LiveData<User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {

                }
            }
        }
    }

    // 채팅룸 안의 메시지를 가져오기 위한 함수
    fun loadMessage() {
        viewModelScope.launch {
            if(_roomId != null) {
                messageRepository.getChatMessages(_roomId.value.toString())
                    .collect{ _messages.value = it}
            }
        }
    }

    // 사용자가 속한 채팅룸에 메시지를 보내기 위한 함수
    fun sendMessage(text: String) {
        if(_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            viewModelScope.launch {
                when(messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is Result.Success -> Unit
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    // 현재 채팅룸 정보에 대한 id를 제공하고 메시지를 표시하기 위한 함수
    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessage()
    }
}