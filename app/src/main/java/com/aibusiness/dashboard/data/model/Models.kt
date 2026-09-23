package com.aibusiness.dashboard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Generation Formats
enum class GenerationFormat(val displayName: String) {
    REPORT("Professional Report"),
    EMAIL("Business Email"),
    SUMMARY("Executive Summary"),
    PRESENTATION("Presentation Outline"),
    BLOG("Blog / Article"),
    LETTER("Formal Letter"),
    PROPOSAL("Business Proposal"),
    ANALYSIS("Data Analysis"),
    SOCIAL("Social Media Post"),
    CUSTOM("Custom Format")
}

// Styles
enum class GenerationStyle(val displayName: String) {
    PROFESSIONAL("Professional"),
    CONCISE("Concise & Direct"),
    DETAILED("Detailed & Thorough"),
    EXECUTIVE("Executive Level"),
    CASUAL("Casual Professional"),
    TECHNICAL("Technical"),
    PERSUASIVE("Persuasive"),
    ANALYTICAL("Analytical")
}

// Themes / Tones
enum class GenerationTheme(val displayName: String) {
    BUSINESS("Business"),
    FORMAL("Formal"),
    CREATIVE("Creative"),
    ANALYTICAL("Analytical"),
    INSPIRATIONAL("Inspirational"),
    NEUTRAL("Neutral"),
    AUTHORITATIVE("Authoritative")
}

// Generation Type
enum class GenerationType {
    TEXT,
    IMAGE
}

// Uploaded File
data class UploadedFile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val uri: String,
    val mimeType: String,
    val size: Long = 0L
)

// Generation Request
data class GenerationRequest(
    val prompt: String,
    val format: GenerationFormat = GenerationFormat.REPORT,
    val style: GenerationStyle = GenerationStyle.PROFESSIONAL,
    val theme: GenerationTheme = GenerationTheme.BUSINESS,
    val type: GenerationType = GenerationType.TEXT,
    val files: List<UploadedFile> = emptyList(),
    val useRealAI: Boolean = false
)

// Generation Result
data class GenerationResult(
    val id: String = UUID.randomUUID().toString(),
    val request: GenerationRequest,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)

// Room Entity for History
@Entity(tableName = "generation_history")
data class GenerationHistoryEntity(
    @PrimaryKey val id: String,
    val prompt: String,
    val format: String,
    val style: String,
    val theme: String,
    val type: String,
    val content: String,
    val imageUrl: String? = null,
    val timestamp: Long,
    val isSuccess: Boolean = true
)

// User
data class User(
    val id: String = "demo_user",
    val name: String = "Business User",
    val email: String = "user@company.com",
    val isLoggedIn: Boolean = false
)

// Dashboard Card
data class DashboardCard(
    val title: String,
    val subtitle: String,
    val icon: String,
    val route: String,
    val color: Long
)
