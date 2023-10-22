package com.beck.apitool

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.request
import io.ktor.util.logging.Logger
import kotlinx.coroutines.launch

enum class LoadingStatus {
    LOADING,
    DONE
}
class MainViewModel: ViewModel() {
    private val client = HttpClient(OkHttp) {
        install(Logging)
    }

    val url = MutableLiveData<String>()
    val request_body = MutableLiveData<String>()
    val responseView = MutableLiveData<String>()

    val isLoading = MutableLiveData(false)
    fun httpRequest(protocol: String) {
        val requestUrl = url.value ?: ""
        viewModelScope.launch {
            Log.d("MainViewModel", "Http Request: $requestUrl")
            isLoading.value = true
            val response = client.get(requestUrl)
            responseView.value = response.body()
        }
        isLoading.value = false
    }
}