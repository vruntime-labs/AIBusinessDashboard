package com.aibusiness.dashboard.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aibusiness.dashboard.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: UiState,
    onApiKeyChange: (String) -> Unit,
    onUseRealAIChange: (Boolean) -> Unit,
    onToggleDarkTheme: () -> Unit,
    onBack: () -> Unit
) {
    var showApiKey by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // AI Configuration
            Text("AI Configuration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.apiKey,
                        onValueChange = onApiKeyChange,
                        label = { Text("API Key") },
                        placeholder = { Text("sk-... or xai-...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showApiKey = !showApiKey }) {
                                Icon(
                                    if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    )

                    Text(
                        "Supports OpenAI, xAI (Grok), and other OpenAI-compatible APIs.\nLeave empty to use Demo mode.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Use Real AI", fontWeight = FontWeight.Medium)
                            Text(
                                "Requires valid API key",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.useRealAI,
                            onCheckedChange = onUseRealAIChange
                        )
                    }
                }
            }

            // Appearance
            Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Dark Theme", fontWeight = FontWeight.Medium)
                        Text(
                            if (uiState.isDarkTheme) "Currently Dark" else "Currently Light / System",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.isDarkTheme,
                        onCheckedChange = { onToggleDarkTheme() }
                    )
                }
            }

            // About
            Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("AI Business Dashboard", fontWeight = FontWeight.SemiBold)
                    Text("Version 1.0.0", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Professional AI content generation platform for business users.\nSupports multiple formats, styles, themes, and file uploads.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
