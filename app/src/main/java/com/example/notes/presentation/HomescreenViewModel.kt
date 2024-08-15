package com.example.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.repository.Repository
import com.example.notes.data.room.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    val isLoading: Boolean = false,
    val items: List<Note> = emptyList<Note>()
)
@HiltViewModel
class HomescreenViewModel @Inject constructor(

    private val repository: Repository

): ViewModel() {

    private val _uiState = MutableStateFlow(UIState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllNotes().collectLatest { notes ->
                _uiState.value = _uiState.value.copy(isLoading = false, items = notes)
            }
        }
    }

    fun insertData(note: Note) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.insert(note)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun deleteData(note: Note) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.delete(note)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun updateData(note: Note) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.update(note)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
