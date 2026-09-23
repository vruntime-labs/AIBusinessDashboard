package com.aibusiness.dashboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aibusiness.dashboard.data.model.*
import com.aibusiness.dashboard.data.repository.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val isLoggedIn: Boolean = false,
    val user: User = User(),
    val isLoading: Boolean = false,
    val currentPrompt: String = "",
    val selectedFormat: GenerationFormat = GenerationFormat.REPORT,
    val selectedStyle: GenerationStyle = GenerationStyle.PROFESSIONAL,
    val selectedTheme: GenerationTheme = GenerationTheme.BUSINESS,
    val selectedType: GenerationType = GenerationType.TEXT,
    val uploadedFiles: List<UploadedFile> = emptyList(),
    val useRealAI: Boolean = false,
    val apiKey: String = "",
    val latestResult: GenerationResult? = null,
    val history: List<GenerationResult> = emptyList(),
    val errorMessage: String? = null,
    val isDarkTheme: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AIRepository(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // Observe history
        viewModelScope.launch {
            repository.getHistory().collect { historyList ->
                _uiState.update { it.copy(history = historyList) }
            }
        }
    }

    fun login(name: String = "Business User", email: String = "user@company.com") {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                user = User(name = name, email = email, isLoggedIn = true)
            )
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                user = User()
            )
        }
    }

    fun updatePrompt(prompt: String) {
        _uiState.update { it.copy(currentPrompt = prompt) }
    }

    fun updateFormat(format: GenerationFormat) {
        _uiState.update { it.copy(selectedFormat = format) }
    }

    fun updateStyle(style: GenerationStyle) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun updateTheme(theme: GenerationTheme) {
        _uiState.update { it.copy(selectedTheme = theme) }
    }

    fun updateType(type: GenerationType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun addFiles(files: List<UploadedFile>) {
        _uiState.update {
            it.copy(uploadedFiles = it.uploadedFiles + files)
        }
    }

    fun removeFile(fileId: String) {
        _uiState.update {
            it.copy(uploadedFiles = it.uploadedFiles.filter { f -> f.id != fileId })
        }
    }

    fun clearFiles() {
        _uiState.update { it.copy(uploadedFiles = emptyList()) }
    }

    fun setUseRealAI(enabled: Boolean) {
        _uiState.update { it.copy(useRealAI = enabled) }
    }

    fun setApiKey(key: String) {
        _uiState.update { it.copy(apiKey = key) }
    }

    fun toggleDarkTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun generate() {
        val state = _uiState.value
        if (state.currentPrompt.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a prompt") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, latestResult = null) }

            try {
                val request = GenerationRequest(
                    prompt = state.currentPrompt,
                    format = state.selectedFormat,
                    style = state.selectedStyle,
                    theme = state.selectedTheme,
                    type = state.selectedType,
                    files = state.uploadedFiles,
                    useRealAI = state.useRealAI
                )

                val result = repository.generate(
                    request = request,
                    apiKey = state.apiKey.takeIf { it.isNotBlank() }
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        latestResult = result,
                        errorMessage = if (!result.isSuccess) result.errorMessage else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Generation failed"
                    )
                }
            }
        }
    }

    fun clearLatestResult() {
        _uiState.update { it.copy(latestResult = null) }
    }

    fun deleteHistoryItem(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
