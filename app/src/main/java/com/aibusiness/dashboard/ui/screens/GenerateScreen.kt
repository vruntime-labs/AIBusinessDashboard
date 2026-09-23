package com.aibusiness.dashboard.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aibusiness.dashboard.data.model.*
import com.aibusiness.dashboard.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    uiState: UiState,
    onPromptChange: (String) -> Unit,
    onFormatChange: (GenerationFormat) -> Unit,
    onStyleChange: (GenerationStyle) -> Unit,
    onThemeChange: (GenerationTheme) -> Unit,
    onTypeChange: (GenerationType) -> Unit,
    onAddFiles: (List<UploadedFile>) -> Unit,
    onRemoveFile: (String) -> Unit,
    onClearFiles: () -> Unit,
    onUseRealAIChange: (Boolean) -> Unit,
    onGenerate: () -> Unit,
    onBack: () -> Unit,
    onViewResult: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Multi file picker
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        val files = uris.map { uri ->
            val name = uri.lastPathSegment ?: "file_${System.currentTimeMillis()}"
            val mime = context.contentResolver.getType(uri) ?: "application/octet-stream"
            UploadedFile(
                name = name.substringAfterLast('/'),
                uri = uri.toString(),
                mimeType = mime
            )
        }
        onAddFiles(files)
    }

    // Navigate to result when generation finishes
    LaunchedEffect(uiState.latestResult, uiState.isLoading) {
        if (!uiState.isLoading && uiState.latestResult != null) {
            onViewResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Content", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = onGenerate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !uiState.isLoading && uiState.currentPrompt.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Generating...")
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate with AI", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
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
            // Generation Type (Text / Image)
            Text("Generation Type", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = uiState.selectedType == GenerationType.TEXT,
                    onClick = { onTypeChange(GenerationType.TEXT) },
                    label = { Text("Text") },
                    leadingIcon = if (uiState.selectedType == GenerationType.TEXT) {
                        { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                    } else null
                )
                FilterChip(
                    selected = uiState.selectedType == GenerationType.IMAGE,
                    onClick = { onTypeChange(GenerationType.IMAGE) },
                    label = { Text("Image") },
                    leadingIcon = if (uiState.selectedType == GenerationType.IMAGE) {
                        { Icon(Icons.Default.Check, null, Modifier.size(18.dp)) }
                    } else null
                )
            }

            // Prompt
            Text("Your Prompt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = uiState.currentPrompt,
                onValueChange = onPromptChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Describe what you want to generate...\nExample: Create a quarterly business performance report for Q3 2025") },
                shape = RoundedCornerShape(12.dp),
                maxLines = 6
            )

            // Format
            Text("Format", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(GenerationFormat.entries) { format ->
                    FilterChip(
                        selected = uiState.selectedFormat == format,
                        onClick = { onFormatChange(format) },
                        label = { Text(format.displayName) }
                    )
                }
            }

            // Style
            Text("Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(GenerationStyle.entries) { style ->
                    FilterChip(
                        selected = uiState.selectedStyle == style,
                        onClick = { onStyleChange(style) },
                        label = { Text(style.displayName) }
                    )
                }
            }

            // Theme / Tone
            Text("Theme / Tone", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(GenerationTheme.entries) { theme ->
                    FilterChip(
                        selected = uiState.selectedTheme == theme,
                        onClick = { onThemeChange(theme) },
                        label = { Text(theme.displayName) }
                    )
                }
            }

            // File Upload Section
            Text("Upload Files (Optional)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    filePickerLauncher.launch(arrayOf(
                        "application/pdf",
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "text/*",
                        "image/*",
                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    ))
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tap to upload files", fontWeight = FontWeight.Medium)
                    Text(
                        "PDF, Word, Excel, Images, Text",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Uploaded files list
            if (uiState.uploadedFiles.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${uiState.uploadedFiles.size} file(s) selected",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(onClick = onClearFiles) {
                            Text("Clear all")
                        }
                    }
                    uiState.uploadedFiles.forEach { file ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.InsertDriveFile, null)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    file.name,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton(onClick = { onRemoveFile(file.id) }) {
                                    Icon(Icons.Default.Close, "Remove")
                                }
                            }
                        }
                    }
                }
            }

            // Real AI Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Use Real AI", fontWeight = FontWeight.Medium)
                        Text(
                            if (uiState.useRealAI) "API mode (requires key in Settings)" 
                            else "Demo mode (instant mock responses)",
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

            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom bar
        }
    }
}
