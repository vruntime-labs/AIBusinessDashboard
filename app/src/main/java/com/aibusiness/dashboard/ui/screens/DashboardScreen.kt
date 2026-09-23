package com.aibusiness.dashboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aibusiness.dashboard.ui.theme.*
import com.aibusiness.dashboard.viewmodel.UiState

data class DashCard(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: UiState,
    onNavigateToGenerate: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val cards = listOf(
        DashCard(
            title = "Generate Content",
            subtitle = "Text & Images with AI",
            icon = Icons.Default.AutoAwesome,
            gradient = listOf(CardBlue, CardIndigo),
            onClick = onNavigateToGenerate
        ),
        DashCard(
            title = "History",
            subtitle = "${uiState.history.size} generations",
            icon = Icons.Default.History,
            gradient = listOf(CardTeal, Color(0xFF00695C)),
            onClick = onNavigateToHistory
        ),
        DashCard(
            title = "Settings",
            subtitle = "API Key & Preferences",
            icon = Icons.Default.Settings,
            gradient = listOf(CardPurple, Color(0xFF4527A0)),
            onClick = onNavigateToSettings
        ),
        DashCard(
            title = "Upload Files",
            subtitle = "PDF, Docs, Images",
            icon = Icons.Default.UploadFile,
            gradient = listOf(Color(0xFFEF6C00), Color(0xFFE65100)),
            onClick = onNavigateToGenerate
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "AI Business Dashboard",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Welcome, ${uiState.user.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(BusinessBlue, AccentTeal)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            "Professional AI Workspace",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Generate reports, emails, proposals & more\nwith multiple formats, styles & themes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cards) { card ->
                    DashboardActionCard(card)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Generations",
                    value = "${uiState.history.size}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Files",
                    value = "${uiState.uploadedFiles.size}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Mode",
                    value = if (uiState.useRealAI) "Real AI" else "Demo",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DashboardActionCard(card: DashCard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { card.onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(card.gradient))
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        card.title,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        card.subtitle,
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
