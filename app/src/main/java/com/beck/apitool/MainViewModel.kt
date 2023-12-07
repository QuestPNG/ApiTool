package com.beck.apitool

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.accept
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

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

enum class wsMessageType {
    INCOMING,
    OUTGOING
}

data class wsMessage(
    val type: wsMessageType,
    val content: String
)

/*enum class HTTPMethod {
    GET,
    POST,
    PUT,
    DELETE
}*/

data class GridRowState (
    val key: String,
    val value: String,
)
class MainViewModel: ViewModel() {
    private val client = HttpClient(OkHttp) {
        install(Logging)
        install(ContentNegotiation) {
            json()
        }
        //TODO: Install ContentNegotiation plugin
    }

    private val wsClient = HttpClient(OkHttp) {
        install(Logging)
        install(WebSockets)
    }

    val url = MutableLiveData<String>()

    private val _composeUrl = MutableStateFlow("")
    val composeUrl = _composeUrl.asStateFlow()

    val requestBody = MutableLiveData<String>()
    val responseView = MutableLiveData<String>()

    val webSocketResponseView = mutableStateListOf<wsMessage>()

    private val _composeResponseView = MutableStateFlow("")
    val composeResponseView = _composeResponseView.asStateFlow()

    private val _currentView = MutableStateFlow(InputView.BODY)
    val currentView = _currentView.asStateFlow()

    val isLoading = MutableLiveData(false)

    private val _currentMethod = MutableStateFlow(HttpMethod.Get)
    val currentMethod = _currentMethod.asStateFlow()

    val headerState = mutableStateListOf<GridRowState>(GridRowState("Content-Type", "application/json"))
    val queryState = mutableStateListOf<GridRowState>(GridRowState("", ""))
    val gridState = mutableStateListOf<GridRowState>(GridRowState("", ""))

    private val _bodyState = MutableStateFlow("")
    val bodyState = _bodyState.asStateFlow()

    val spinnerState = MutableLiveData<Int>()

    fun httpRequest(protocol: String?) {
        //val requestUrl = url.value ?: ""
        val requestUrl = composeUrl.value ?: ""
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = client.request(requestUrl) {
                    /*method = when(spinnerState.value) {
                        0 -> HttpMethod.Get
                        1 -> HttpMethod.Post
                        2 -> HttpMethod.Put
                        3 -> HttpMethod.Delete
                        else -> HttpMethod.Get
                    }*/
                    method = _currentMethod.value
                    accept(io.ktor.http.ContentType.Any)
                    url {
                        for (header in headerState) {
                            if(header.key != "" && header.value != "") {
                                headers.append(header.key, header.value)
                            }
                        }
                        for (param in queryState) {
                            if(param.key != "" && param.value != "") {
                                parameters.append(param.key, param.value)
                            }
                        }
                    }
                    setBody(bodyState.value)
                }
                _composeResponseView.value = response.body()
                //responseView.value = response.body()
            } catch (e: Exception) {
               // TODO: Make toast
                //responseView.value = e.stackTraceToString()
                _composeResponseView.value = e.stackTraceToString()
            }
        }
        isLoading.value = false
    }

    fun openConnection(){
        viewModelScope.launch{
            val wsRequest = _composeUrl.value // get viewmodel variable
            try {
                val session = wsClient.webSocketSession(wsRequest) {
                }
                session.incoming
                        .consumeAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { (it.readText()) }
                        .onEach {
                            Log.d("incoming", it)
                            _composeResponseView.value = it
                        }
                        .catch {
                            Log.e("viewModel.openConnection", it.stackTraceToString())
                            session.close()
                            //TODO make toast or response codes
                        }
                        .collect{
                            _composeResponseView.value = it
                            val message = wsMessage(wsMessageType.INCOMING, it)
                            webSocketResponseView.add(message)
                        }
                session.incoming.receive()
            } catch(e: Exception){
                Log.e("viewModel.openConnection", e.stackTraceToString())
            }
        }
    }

    fun setHttpMethod(method: HttpMethod) {
        _currentMethod.value = method
    }
    fun setUrl(url: String) {
        _composeUrl.value = url
    }

    fun setResponse(response: String) {
        _composeResponseView.value = response
    }
    fun setBody(body: String) {
       _bodyState.value = body
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