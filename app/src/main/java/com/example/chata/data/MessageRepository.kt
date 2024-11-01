    package com.example.chata.data

    import android.util.Log
    import com.google.firebase.firestore.FirebaseFirestore
    import kotlinx.coroutines.channels.awaitClose
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.callbackFlow
    import kotlinx.coroutines.tasks.await

    class MessageRepository(private val firestore: FirebaseFirestore) {


        init {
            Log.d("MessageRepository", "Firestore instance: $firestore")
        }





        suspend fun sendMessage(roomId: String, message: Message) : Result<Unit> = try {
            Log.d("MessageRepository", "roomId: $roomId, message: $message")
            Log.d("MessageRepository", "Attempting to send message: $message")
            firestore.collection("rooms")
                .document(roomId).collection("messages").add(message).await()
            Log.d("MessageRepository", "Message sent successfully")
            Result.Success(Unit)
        } catch (e:Exception) {
            Log.e("MessageRepository", "Error sending message: ${e.localizedMessage}", e)
            Result.Error(e)
        }

        fun getChatMessages(roomId: String) : Flow<List<Message>> = callbackFlow {
            val subscription = firestore.collection("rooms").document(roomId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, _ ->
                    querySnapshot?.let {
                        trySend(it.documents.map { doc ->
                            doc.toObject(Message::class.java)!!.copy()
                        }).isSuccess
                    }
                }
            awaitClose { subscription.remove()}
        }
    }