package com.beck.apitool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.request
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val client = HttpClient(OkHttp)

    val url = MutableLiveData<String>()
    val request_body = MutableLiveData<String>()
    val responseView = MutableLiveData<String>("{ \"type\": \"success\" }")
    fun httpRequest(protocol: String, url: String) {
        viewModelScope.launch {
            if(protocol == "get") {
                val response = client.get(url)
            }
        }
    }
}