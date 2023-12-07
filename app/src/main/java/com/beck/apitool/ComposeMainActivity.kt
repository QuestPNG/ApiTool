package com.beck.apitool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.beck.apitool.Databases_Room.Database
import com.beck.apitool.Databases_Room.Session
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
                    var showWS by remember { mutableStateOf(false) }
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
                                        navController.navigate("db")
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = null)
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        showWS = !showWS
                                        navController.navigate("http")
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                    }
                                }
                            )
                        },
                        contentWindowInsets = WindowInsets(0.dp)
                    ) { scaffoldPadding ->
                        //WsScreen(viewModel = viewModel)
                        NavHost(navController = navController, startDestination = "http") {
                            composable("http") {
                                if(showWS) {
                                    WsScreen(modifier = Modifier.padding(scaffoldPadding), viewModel = viewModel)
                                } else {
                                    HttpScreen(
                                        modifier = Modifier.padding(scaffoldPadding),
                                        viewModel = viewModel
                                    )
                                }
                            }
                            composable("webSocket") {
                                WsScreen(modifier = Modifier.padding(scaffoldPadding), viewModel = viewModel)
                            }
                            composable("db") {
                                viewModel.getSavedSessions()
                                SessionListScreen(modifier = Modifier.padding(scaffoldPadding), viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
