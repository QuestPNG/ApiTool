package com.beck.apitool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.beck.apitool.Databases_Room.Database
import com.beck.apitool.Databases_Room.Session
import com.beck.apitool.ui.common.gridTextFieldColors
import com.beck.apitool.ui.screens.HttpScreen
import com.beck.apitool.ui.screens.SessionListScreen
import com.beck.apitool.ui.screens.WsScreen
import com.beck.apitool.ui.theme.ApiToolTheme

class ComposeMainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiToolTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var openSaveDialog by remember { mutableStateOf(false) }
                    var showCreateDialog by remember { mutableStateOf(false) }
                    var showingDB by remember { mutableStateOf(false) }
                    val navController = rememberNavController()
                    val db = Room.databaseBuilder(
                        applicationContext,
                        Database::class.java, "session-db"
                    ).build()
                    //val viewModel by remember { mutableStateOf(viewModel(factory = MainViewModelFactory(db)) }
                    val viewModel: MainViewModel by viewModels { MainViewModelFactory(db) }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("ApiTool") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        if (showingDB) {
                                            navController.popBackStack()
                                            showingDB = false
                                        } else {
                                            navController.navigate("db")
                                            showingDB = true
                                        }

                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = null)
                                    }
                                },
                                actions = {
                                    if(!showingDB) {
                                        IconButton(
                                            onClick = {
                                                showCreateDialog = true
                                            }
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = null)
                                        }
                                        IconButton(
                                            onClick = {
                                                openSaveDialog = true
                                            }
                                        ) {
                                            Icon(Icons.Default.Create, contentDescription = null)
                                        }
                                    }
                                }
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp)
                    ) { scaffoldPadding ->
                        //WsScreen(viewModel = viewModel)
                        val sessionIsWebsocket = viewModel.sessionIsWebsocket.collectAsState()
                        when {
                            openSaveDialog -> {
                                SaveDialog(
                                    onDismissRequest = { openSaveDialog = false },
                                    onSaveClick = viewModel::saveSession,
                                    sessionIsWebsocket.value
                                )
                            }
                            showCreateDialog -> {
                                CreateSessionDialog(
                                    onDismissRequest = { showCreateDialog = false },
                                    createHTTP = {
                                        viewModel.newSession(false)
                                        navController.navigate("http")
                                        showCreateDialog = false
                                    },
                                    createWebsocket = {
                                        viewModel.newSession(true)
                                        navController.navigate("ws")
                                        showCreateDialog = false
                                    }
                                )
                            }
                        }
                        NavHost(navController = navController, startDestination = "http") {
                            composable("http") {
                                HttpScreen(
                                    modifier = Modifier.padding(scaffoldPadding),
                                    viewModel = viewModel
                                )
                            }
                            composable("ws") {
                                WsScreen(modifier = Modifier.padding(scaffoldPadding), viewModel = viewModel)
                            }
                            composable("db") {
                                viewModel.getSavedSessions()
                                SessionListScreen(
                                    modifier = Modifier.padding(scaffoldPadding),
                                    viewModel = viewModel,
                                    openHttpSession = {
                                        showingDB = false
                                        navController.navigate("http")
                                    },
                                    openWsSession = {
                                        showingDB = false
                                        navController.navigate("ws")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateSessionDialog(
    onDismissRequest: () -> Unit,
    createHTTP: () -> Unit,
    createWebsocket: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Create New Session",
                        fontSize = 20.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = createHTTP
                        ) {
                            Text("HTTP")
                        }
                        //Text("Or")
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = createWebsocket
                        ) {
                            Text("WebSocket")
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun SaveDialog(
    onDismissRequest: () -> Unit,
    onSaveClick: (String, Boolean) -> Unit,
    isWebSocketSession: Boolean
) {
    var title by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            /*Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center
            )*/
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    OutlinedTextField(
                        modifier = Modifier,
                        value = title,
                        onValueChange = { title = it },
                        placeholder = {
                            Text("Please enter a title")
                        },
                        colors = gridTextFieldColors()
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            onSaveClick(title,  isWebSocketSession)
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = "Save"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SaveDialogPreview() {
    ApiToolTheme {
        SaveDialog({}, {_, _ -> }, false)
    }
}

@Composable
@Preview
fun CreateDialogPreview() {
    ApiToolTheme {
        CreateSessionDialog(
            {}, {}, {}
        )
    }
}