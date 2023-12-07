package com.beck.apitool.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun WsScreen(
        modifier: Modifier = Modifier,
        viewModel: MainViewModel = MainViewModel()
){
    val url = viewModel.composeUrl.collectAsState()
    Box(modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxSize()
    ){
        ConstraintLayout(
                modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            val urlLabel = createRef()
            val responseLabel = createRef()
            val response = createRef()
            val connButton = createRef()

            Row(
                    modifier
                            .fillMaxWidth()
                            .constrainAs(urlLabel) {
                                top.linkTo(parent.top, margin = 8.dp)


                            },
                    verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "URL:",
                    fontSize = 24.sp
                )
                TextField(
                        modifier = Modifier

                                .padding(start = 8.dp),
                        value = url.value ,
                        onValueChange = viewModel::setUrl,
                        singleLine = true,
                        colors = gridTextFieldColors(),
                        placeholder = {
                            Text(text = "Enter your url...")
                        }
                        )


            }

            Button(modifier = Modifier.constrainAs(connButton){
                    top.linkTo(urlLabel.bottom)
                    end.linkTo(parent.end)
            },
                    onClick = {
                        viewModel.openConnection()
                    }) {
                Text(
                        "Connect",
                        fontSize = 16.sp,
                        overflow = TextOverflow.Clip
                )
            }

            Text(modifier = Modifier.constrainAs(responseLabel) {
                top.linkTo(connButton.bottom, margin = 8.dp)
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
fun WsPreview(){
    ApiToolTheme {
        WsScreen()
    }
}
























