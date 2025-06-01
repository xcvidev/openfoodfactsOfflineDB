package com.xcvi.openfoodfactsapi.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FoodViewModel,
) {
    val foods = viewModel.foods.collectAsStateWithLifecycle().value
    var text by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = {
                    navController.navigate("camera_screen")
                }
            ) {
                Text("Scan")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item{
                Row {
                    TextField(
                        singleLine = true,
                        maxLines = 1,
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, "")
                        },
                        placeholder = {
                            Text("Search")
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Transparent,
                            unfocusedContainerColor = Transparent,
                            disabledContainerColor = Transparent,
                            focusedIndicatorColor = Transparent,
                            unfocusedIndicatorColor = Transparent,
                            disabledIndicatorColor = Transparent,
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.search(text)
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Search
                        ),
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        value = text,
                        onValueChange = {
                            text = it
                        }
                    )
                    Button(
                        modifier = Modifier.padding(8.dp).weight(1f),
                        onClick = {
                        viewModel.search(text)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Outlined.Search, "")
                    }
                }
            }
            foods.forEach { food ->
                item {
                    Card(
                        modifier = Modifier.padding(12.dp).fillMaxWidth()
                    ) {
                        Text("Name: ${food.name} (${food.brand})", fontSize = 20.sp)
                        Text("Barcode: ${food.barcode}")
                        Text("Calories: ${food.calories} kcal")
                    }
                }
            }

        }
    }
}