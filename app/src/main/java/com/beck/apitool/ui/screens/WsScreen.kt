package com.beck.apitool.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.beck.apitool.wsMessage
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import com.beck.apitool.ui.theme.text
import com.beck.apitool.wsMessageType
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.beck.apitool.R

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
                modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
        ) {
            val urlLabel = createRef()
            val responseLabel = createRef()
            val response = createRef()
            val connButton = createRef()
            val message = createRef()
            val messageLabel = createRef()
            val sendButton = createRef()

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

                                .padding(start = 8.dp, end = 16.dp),
                        value = url.value ,
                        onValueChange = viewModel::setUrl,
                        singleLine = true,
                        colors = gridTextFieldColors(),
                        placeholder = {
                            Text(text = "Enter your url...")
                        }
                        )


            }

            Button(
                    modifier = Modifier.constrainAs(connButton){
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

            Row(modifier = Modifier.constrainAs(messageLabel){
                top.linkTo(connButton.bottom)
            }) {
                Text(
                        text = "Message",
                        fontSize = 24.sp
                )
                Spacer(Modifier.weight(1f))
                Button(
                        onClick = viewModel::sendWsMessage) {
                    Text(
                            "Send",
                            fontSize = 16.sp,
                    )
                }
            }

            val body = viewModel.bodyState.collectAsState()
            TextField(
                    modifier = Modifier
                            .constrainAs(message) {
                                top.linkTo(messageLabel.bottom)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth(),
                    value = body.value,
                    onValueChange = viewModel::setBody,
                    colors = gridTextFieldColors(),
                    placeholder = {
                        Text(text="Compose message here...")
                    }
            )

            Text(
                    modifier = Modifier.constrainAs(responseLabel) {
                    top.linkTo(message.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
            },
                    text = "Response",
                    fontSize = 24.sp,
            )

            LazyColumn(
                    modifier = Modifier
                            .constrainAs(response) {
                                top.linkTo(responseLabel.bottom)
                            }
                            .fillMaxWidth()){

                items(viewModel.webSocketResponseView){
                    if(it.type == wsMessageType.INCOMING){
                        Column(modifier = Modifier.fillMaxWidth().padding(end = 10.dp) , horizontalAlignment = Alignment.Start){
                            Card(){
                                Box(
                                    contentAlignment = Alignment.Center){
                                    Text(   modifier = Modifier.padding(start = 24.dp),
                                            text = it.content)
                                    Icon(
                                            painter = painterResource(R.drawable.incoming),
                                            contentDescription = stringResource(id = R.string.incoming_desc),
                                            tint = Color(0xFFca9ee6),
                                            modifier = Modifier.align(Alignment.TopStart)
                                    )
                                }
                            }
                        }
                    }else if(it.type == wsMessageType.OUTGOING){
                        Column(modifier = Modifier.fillMaxWidth().padding(start = 10.dp) , horizontalAlignment = Alignment.End){
                            Card(){
                                Box(
                                        contentAlignment = Alignment.Center){
                                    Text(   modifier = Modifier.padding(end = 24.dp),
                                            text = it.content)
                                    Icon(
                                            painter = painterResource(R.drawable.outgoing),
                                            contentDescription = stringResource(id = R.string.outgoing_desc),
                                            tint = Color(0xFF8caaee),
                                            modifier = Modifier.padding(start = 3.dp).align(Alignment.TopEnd)
                                    )
                                }

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
fun WsPreview(){
    ApiToolTheme {
        WsScreen()
    }
}
























