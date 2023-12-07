package com.beck.apitool.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.beck.apitool.ComposeGrid
import com.beck.apitool.InputTabBar
import com.beck.apitool.InputView
import com.beck.apitool.MainViewModel
import com.beck.apitool.ui.common.gridTextFieldColors
import com.beck.apitool.ui.theme.ApiToolTheme
import io.ktor.client.request.request
import io.ktor.http.HttpMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HttpScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = MainViewModel()
) {
    val url = viewModel.composeUrl.collectAsState()
    Box(modifier = modifier
        .padding(vertical = 8.dp)
        .fillMaxSize()
        ) {
        ConstraintLayout(
            modifier = Modifier.padding(8.dp)
        ){
            val methodDropdown = createRef()
            val (urlLabel, urlInput) = createRefs()
            val requestGrid = createRef()
            val (responseLabel, response) = createRefs()
            val sendButton = createRef()

            Box(
                modifier = Modifier
                    .constrainAs(methodDropdown) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        //end.linkTo(parent.end, margin = 8.dp)
                    }
            ) {
                var expanded by remember { mutableStateOf(false) }
                val currentMethod = viewModel.currentMethod.collectAsState()
                Button(
                    onClick = { expanded = !expanded },
                    contentPadding = PaddingValues(4.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = when(currentMethod.value) {
                        HttpMethod.Get -> "GET"
                        HttpMethod.Post -> "POST"
                        HttpMethod.Put -> "PUT"
                        HttpMethod.Delete -> "DELETE"
                        else -> "GET"
                    }, fontSize = 20.sp
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text="GET") },
                        onClick = {
                            viewModel.setHttpMethod(HttpMethod.Get)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text="POST") },
                        onClick = {
                            viewModel.setHttpMethod(HttpMethod.Post)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text="PUT") },
                        onClick = {
                            viewModel.setHttpMethod(HttpMethod.Put)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text="DELETE") },
                        onClick = {
                            viewModel.setHttpMethod(HttpMethod.Delete)
                            expanded = false
                        }
                    )
                }
            }
            Row(
                modifier.constrainAs(urlLabel) {
                    top.linkTo(methodDropdown.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    //modifier = Modifier.constrainAs(urlLabel) {
                    //    top.linkTo(methodDropdown.bottom, margin = 16.dp)
                    //    start.linkTo(parent.start, margin = 8.dp)
                    //},
                    text = "URL",
                    fontSize = 24.sp
                )
                TextField(
                    //modifier = Modifier.fillMaxWidth().constrainAs(urlInput) {
                    //    top.linkTo(urlLabel.top)
                    //    bottom.linkTo(urlLabel.bottom)
                    //    start.linkTo(urlLabel.end, margin = 8.dp)
                    //},
                    modifier = Modifier.fillMaxWidth().padding(start=8.dp, end=16.dp),
                    value = url.value,
                    onValueChange = viewModel::setUrl,
                    singleLine = true,
                    colors = gridTextFieldColors(),
                    placeholder = {
                        Text(text = "Enter your url...")
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .constrainAs(requestGrid) {
                        top.linkTo(urlLabel.bottom, margin = 8.dp)
                    }
            ) {
                val currentView = viewModel.currentView.collectAsState()
                InputTabBar(
                    modifier = Modifier.padding(bottom=4.dp),
                    currentView = currentView.value,
                    onInputViewChange = viewModel::setCurrentView
                )
                val bodyState = viewModel.bodyState.collectAsState()
                when (currentView.value) {
                    InputView.HEADERS -> ComposeGrid(
                        viewModel = viewModel,
                        content = viewModel.headerState,
                        onInput = viewModel::onHeaderChange,
                        onDelete = viewModel::removeHeader,
                        onAdd = viewModel::addHeader
                    )
                    InputView.QUERY -> ComposeGrid(
                        viewModel = viewModel,
                        content = viewModel.queryState,
                        onInput = viewModel::onQueryChange,
                        onDelete = viewModel::removeQuery,
                        onAdd = viewModel::addQuery
                    )
                    InputView.BODY -> OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = gridTextFieldColors(),
                        value = bodyState.value,
                        onValueChange = viewModel::setBody
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.httpRequest(null)
                },
                modifier = Modifier.constrainAs(sendButton) {
                    top.linkTo(requestGrid.bottom, margin = 8.dp)
                    end.linkTo(parent.end)
                }
            ) {
                Text("Send Request",
                    fontSize = 16.sp
                )
            }
            Text(
                modifier = Modifier.constrainAs(responseLabel) {
                    top.linkTo(sendButton.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
                text = "Response",
                fontSize = 24.sp,
            )
            val responseView = viewModel.composeResponseView.collectAsState()
            TextField(
                value = responseView.value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(response) {
                        top.linkTo(responseLabel.bottom, margin = 8.dp)
                    },
                colors = gridTextFieldColors()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HttpPreview() {
    ApiToolTheme {
        HttpScreen()
    }
}