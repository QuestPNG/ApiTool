package com.beck.apitool.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.beck.apitool.ui.theme.base
import com.beck.apitool.ui.theme.lavender
import com.beck.apitool.ui.theme.mantle
import com.beck.apitool.ui.theme.peach
import com.beck.apitool.ui.theme.text

@Composable
fun tabButtonColors(highlighted: Boolean = false) = if(highlighted) {
    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
} else {
    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gridTextFieldColors(
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledTextColor: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    cursorColor: Color = MaterialTheme.colorScheme.secondary,
    errorCursorColor: Color = Color.White,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Transparent,
) = if(isSystemInDarkTheme()) {
    TextFieldDefaults.outlinedTextFieldColors(
        textColor = textColor,
        disabledTextColor = disabledTextColor,
        containerColor = backgroundColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
    )
} else {
    TextFieldDefaults.textFieldColors()
}