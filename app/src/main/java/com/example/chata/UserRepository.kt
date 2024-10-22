package com.example.chata

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore

    ) {

    suspend fun signUp(email: String, password: String, firstName: String, lastName: String) : Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email,password).await()
            val user = User(firstName, lastName, email)
            saveUserToFireStore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error signing up", e)  // 에러 로그 출력

            Result.Error(e)
        }

    suspend fun login(email: String, password: String) : Result<Boolean> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        Result.Success(true)
    } catch (e: Exception) {
        Log.e("signin", "Error signing in", e)
        Result.Error(e)
    }

    private suspend fun saveUserToFireStore(user : User) {
        fireStore.collection("users").document(user.email).set(user).await()
    }

    suspend fun getCurrentUser() : Result<User> = try {
        val uid = auth.currentUser?.email
        if(uid != null) {
            val userDocument =
                fireStore.collection("user").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if(user != null) {
                Log.d("user2", "$uid")
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }




}