package com.example.chata.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Repository(private val firestore: FirebaseFirestore) {

    init {

        Log.d("MessageRepository", "Firestore instance created: $firestore")
    }

    suspend fun createRoom(name: String) : Result<Unit> = try {
        val roomRef = firestore.collection("rooms").document()
        val room = Room(roomId = roomRef.id, name = name)
        roomRef.set(room).await()
        Result.Success(Unit)
//        val room = Room(name = name)
//        firestore.collection("rooms").add(room).await()
//        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(roomId = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Log.e("getroom", "error getting room", e)

        Result.Error(e)
    }
}

//suspend fun createRoom(name: String) : kotlin.Result<Unit> = try {
//    val room = Room(name = name)
//    firestore.collection("rooms").add(room).await()
//    kotlin.Result.Success(Unit)
//} catch(e: Exception) {
//    kotlin.Result.Error(e)
//}
//
//suspend fun getRooms() : kotlin.Result<List<Room>> = try {
//    val querySnapshot =
//        firestore.collection("rooms").get().await()
//    val rooms = querySnapshot.documents.map { document ->
//        document.toObject(Room::class.java)!!.copy(id = document.id)
//    }
//    kotlin.Result.Success(rooms)
//} catch (e: Exception) {
//    kotlin.Result.Error(e)
//}