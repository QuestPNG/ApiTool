package com.beck.apitool.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun tabButtonColors(highlighted: Boolean = false) = if(highlighted) {
    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
} else {
    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gridTextFieldColors(
    focusedTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    unfocusedTextColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    disabledTextColor: Color = Color.White,
    focusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    cursorColor: Color = MaterialTheme.colorScheme.secondary,
    errorCursorColor: Color = Color.White,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Transparent,
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = focusedTextColor,
    unfocusedTextColor = unfocusedTextColor,
    disabledTextColor = disabledTextColor,
    focusedContainerColor = focusedContainerColor,
    unfocusedContainerColor = unfocusedContainerColor,
    cursorColor = cursorColor,
    errorCursorColor = errorCursorColor,
    focusedBorderColor = focusedBorderColor,
    unfocusedBorderColor = unfocusedBorderColor,
)


@Composable
fun sessionListCardColors(

) = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer
)