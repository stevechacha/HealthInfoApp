package com.chachadev.heathinfoapp.presentation.common.composables.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester

@Composable fun rememberFocusRequester(): FocusRequester = remember { FocusRequester() }

@Composable fun rememberFocusRequester(count: Int): List<FocusRequester> =
    remember { List(count) { FocusRequester() } }