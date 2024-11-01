package com.example.chata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chata.data.Injection
import com.example.chata.data.Message
import com.example.chata.data.MessageRepository
import com.example.chata.data.Result
import com.example.chata.data.User
import com.example.chata.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MessageViewModel: ViewModel() {

    private val messageRepository : MessageRepository
    private val userRepository : UserRepository
    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(),
            Injection.instance())

        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser : LiveData<User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when(val result = userRepository.getCurrentUser()) {
                is Result.Success -> {_currentUser.value = result.data
                    Log.d("MessageViewModel", "Current user loaded: ${_currentUser.value}")
                }
                is Result.Error -> {
                    Log.e("MessageViewModel", "Error loading current user")
                }
            }
        }
    }

//    fun loadMessages() {
//        viewModelScope.launch {
//            if(_roomId != null) {
//                messageRepository.getChatMessages(_roomId.value.toString()).collect{_messages.value = it}
//            }
//        }
//    }

    fun loadMessages() {
        viewModelScope.launch {
            Log.d("MessageViewModel", "Loading messages for roomId: ${_roomId.value}")
            _roomId.value?.let {
                messageRepository.getChatMessages(it).collect { messages ->
                    _messages.value = messages
                    Log.d("MessageViewModel", "Messages loaded: $messages")
                }
            }
        }
    }


    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }

    fun sendMessage(text: String) {
        if(_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            Log.d("MessageViewModel", "sendMessage called with text: $text, ${_currentUser.value!!.firstName}")

            viewModelScope.launch {
                Log.d("MessageViewModel", "Launching coroutine to send message")
                when (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is Result.Success -> {
                        Log.d("MessageViewModel", "Message sent successfully")

                    }
                    is Result.Error -> {
                        Log.e("MessageViewModel", "Error sending message")

                    }


                }
            }
        } else {
            Log.e("MessageViewModel", "sendMessage failed: _currentUser or _roomId is null")

        }
    }



}