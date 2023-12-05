package com.beck.apitool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.beck.apitool.databinding.ComposeGridFragmentBinding
import com.beck.apitool.ui.common.tabButtonColors
import com.beck.apitool.ui.common.gridTextFieldColors
import com.beck.apitool.ui.theme.ApiToolTheme
import com.beck.apitool.ui.theme.red
import com.beck.apitool.ui.theme.text

public class GridFragment: Fragment() {
    private var _binding: ComposeGridFragmentBinding? = null

    // Only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!
    @OptIn(ExperimentalMaterial3Api::class)
    @Override
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val viewModel: MainViewModel by activityViewModels()
        _binding = ComposeGridFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeGrid.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ApiToolTheme {
                    val currentView = viewModel.currentView.collectAsState()
                    Column(
                        modifier = Modifier.padding(vertical=4.dp)
                    ) {
                        InputTabBar(modifier = Modifier.padding(bottom=4.dp), currentView = currentView.value, onInputViewChange = viewModel::setCurrentView)
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
                }
            }
        }
        //return super.onCreateView(inflater, container, savedInstanceState)
        return view
    }
}

@Composable
fun ComposeGrid(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    content: SnapshotStateList<GridRowState>,
    onInput: (Int, String?, String?) -> Unit,
    onDelete: (Int) -> Unit,
    onAdd: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LazyColumn(
            //modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(content) { index, item ->
                GridRow(
                    key = item.key,
                    value = item.value,
                    onKeyChange = { onInput(index, it, null) },
                    onValueChange = { onInput(index, null, it) },
                    onDelete = { onDelete(index) }
                )
            }
        }

        IconButton(
            onClick = onAdd,
        ) {
            Icon(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ),
                imageVector = Icons.Default.Add,
                contentDescription = "Add Row",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        /*Button(
            onClick = onAdd,
            colors = tabButtonColors(highlighted = true)
        ) {
            Text("Add")
        }*/
    }
}

@Composable
fun InputTabBar(
    modifier: Modifier = Modifier,
    currentView: InputView,
    onInputViewChange: (InputView) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        //horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onInputViewChange(InputView.HEADERS) },
            shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
            colors = tabButtonColors(highlighted = currentView == InputView.HEADERS),
            contentPadding = PaddingValues(1.dp)
        ) {
            Text(
                "Headers",
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onInputViewChange(InputView.QUERY) },
            shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
            colors = tabButtonColors(highlighted = currentView == InputView.QUERY)
        ) {
            Text("Query")
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onInputViewChange(InputView.BODY) },
            shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
            colors = tabButtonColors(highlighted = currentView == InputView.BODY)
        ) {
            Text("Body")
        }
        Box(
            modifier = Modifier.weight(0.6f),
            contentAlignment = Alignment.CenterEnd
        ){
            if(currentView == InputView.BODY) {
                IconButton(
                    //modifier = Modifier.weight(0.5f),
                    onClick = {}
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Body Options")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridRow(
    modifier: Modifier = Modifier,
    key: String = "foo",
    value: String = "bar",
    onKeyChange: (String) -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onDelete: () -> Unit,
) {
    Row(
        //modifier = modifier
    ) {
        Row(
            modifier = Modifier.weight(0.9f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                //.padding(horizontal = 2.dp),
                colors = gridTextFieldColors(),
                //shape = RectangleShape,
                value = key,
                onValueChange = onKeyChange
            )
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                //.padding(horizontal = 2.dp),
                colors = gridTextFieldColors(),
                //shape = RectangleShape,
                value = value,
                onValueChange = onValueChange
            )
        }

        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = onDelete
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Row",
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

/*@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GridFragmentPreview() {
   ApiToolTheme {
       Column() {
           val mod = Modifier.padding(top=4.dp)
           GridRow()
           GridRow()
           GridRow()
       }
   }
}*/
