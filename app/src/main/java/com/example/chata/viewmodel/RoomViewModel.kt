package com.example.chata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chata.data.Injection
import com.example.chata.data.Repository
import com.example.chata.data.Result
import com.example.chata.data.Room
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms
    private val roomRepository: Repository = Repository(Injection.instance())

    init {
        loadRooms()
    }

    fun createRoom(name: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
            loadRooms()
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            when(val result = roomRepository.getRooms()) {
                is Result.Success -> {_rooms.postValue(result.data)
                    Log.d("RoomViewModel", "Rooms loaded: ${result.data.size}")
                }

                is Result.Error -> {
                    Log.e("RoomViewModel", "Error loading rooms", result.exception)

                }
            }
        }
    }
}