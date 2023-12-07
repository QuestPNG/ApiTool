package com.beck.apitool.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.beck.apitool.Databases_Room.Session
import com.beck.apitool.ui.theme.ApiToolTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

// val mockSessionList = listOf("mySession", "theSeissoin", "alsdkfj")
val mockSession = Session(
    isWebsocketSession = true,
    title = "Test 1",
    url = "this.url.might.work",
    headers = "",
    method = null,
    queryParams = "",
    body = null
)
val mockSessionList = listOf(
    mockSession,
    mockSession,
    mockSession,
    mockSession
)
@Immutable
data class BorderStroke(val width: Dp, val brush: Brush)

@Composable
fun SessionListScreen(
    modifier: Modifier = Modifier,
    sessions: List<Session>,
) {
    var showWebSockets by remember { mutableStateOf(true) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Card(
                    modifier = Modifier.weight(1f)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text("Saved Sessions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary))
                        .clickable {
                            showWebSockets = false
                        },
                    colors = if(showWebSockets == false) {
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    }
                    else {
                        CardDefaults.cardColors()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("HTTP Requests")
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary))
                        .clickable {
                            showWebSockets = true
                        },
                    colors = if(showWebSockets == true) {
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    }
                    else {
                        CardDefaults.cardColors()
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("WebSockets")
                    }
                }

            }
            LazyColumn() {
                items(sessions) {
                    if (showWebSockets)
                        if (it.isWebsocketSession) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier.weight(0.9f)
                                ){
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)){
                                        Text(it.title)
                                    }
                                }
                                IconButton(
                                    onClick = {},
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        modifier = Modifier.weight(0.1f),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                }
            }
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun SessionListScreenPreview() {
    ApiToolTheme {
        SessionListScreen(sessions = mockSessionList)
    }
}