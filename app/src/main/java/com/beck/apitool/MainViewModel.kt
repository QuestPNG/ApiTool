package com.beck.apitool

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.asStateFlow

enum class LoadingStatus {
    LOADING,
    DONE
}
/*
data class GridRow(
    url: String,
    key: String,
    Value: String

)


val gridItems: List<MutableStateFlows<GridRows>>

onGridRowChange(index: Int) {
    gridItems[index].value =
}
 */

enum class InputView {
    HEADERS,
    QUERY,
    BODY
}

enum class HTTPMethod {
    GET,
    POST,
}

data class GridRowState (
    val key: String,
    val value: String,
)
class MainViewModel: ViewModel() {
    private val client = HttpClient(OkHttp) {
        install(Logging)
    }

    val url = MutableLiveData<String>()
    val requestBody = MutableLiveData<String>()
    val responseView = MutableLiveData<String>()

    private val _currentView = MutableStateFlow(InputView.BODY)
    val currentView = _currentView.asStateFlow()

    val isLoading = MutableLiveData(false)

    val headerState = mutableStateListOf<GridRowState>(GridRowState("", ""))
    val queryState = mutableStateListOf<GridRowState>(GridRowState("", ""))
    val gridState = mutableStateListOf<GridRowState>(GridRowState("", ""))

    fun httpRequest(protocol: String) {
        val requestUrl = url.value ?: ""
        viewModelScope.launch {
            Log.d("MainViewModel", "Http Request: $requestUrl")
            isLoading.value = true
            try {
                val response = client.get(requestUrl)
                responseView.value = response.body()
            } catch (e: Exception) {
               // TODO: Make toast
                responseView.value = ""
            }
        }
        isLoading.value = false
    }

    fun setCurrentView(view: InputView) {
        _currentView.value = view
    }
    fun addGridRow() {
        gridState.add(GridRowState("", ""))
    }
    fun onHeaderChange(index: Int, key: String?, value: String?) {
        val content = headerState[index]
        val newContent = GridRowState(key ?: content.key, value?: content.value)
        headerState[index] = newContent
    }
    fun addHeader() {
        headerState.add(GridRowState("",""))
    }
    fun removeHeader(index: Int) {
        headerState.removeAt(index)
    }
    fun onQueryChange(index: Int, key: String?, value: String?) {
        val content = queryState[index]
        val newContent = GridRowState(key ?: content.key, value?: content.value)
        queryState[index] = newContent
    }
    fun addQuery() {
        queryState.add(GridRowState("",""))
    }
    fun removeQuery(index: Int) {
        queryState.removeAt(index)
    }
    fun onGridInput(index: Int, key: String?, value: String?) {
        val content = gridState[index]
        val newContent = GridRowState(key ?: content.key, value?: content.value)
        gridState[index] = newContent
        //state = GridRowState(key = key ?: state.value.key, value ?: state.value.value)
    }

}