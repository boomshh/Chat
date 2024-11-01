package com.example.chata.data
// 각 프로세스의 데이터 상태를 보유할 클래스
sealed class Result<out T> {
    data class Success<out T> (val data : T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
