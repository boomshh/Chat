package com.example.chata

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Repository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String) : Result<Unit> = try {
        val room = Room(name = name)
        firestore.collection("rooms").add(room).await()
        Log.e("createroom", "creating room")
        Result.Success(Unit)
    } catch (e: Exception) {
        Log.e("createroom", "error creating room", e)
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Log.e("getroom", "error getting room", e)

        Result.Error(e)
    }
}