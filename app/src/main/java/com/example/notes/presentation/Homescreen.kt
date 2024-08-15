package com.example.notes.presentation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notes.data.room.Note
import com.example.notes.presentation.components.NoteInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homescreen() {

    val vm = hiltViewModel<HomescreenViewModel>()

    // Collect the UI state as a state object
    val uiState by vm.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var updation = remember{ mutableStateOf(false) }
    var updateThis:Note =Note(id= -1,note="")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Notes", fontSize = 32.sp) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Note",color = Color.White) },
                icon = { Icon(Icons.Filled.Add, contentDescription = "",tint = Color.White) },
                onClick = {
                    showBottomSheet = true
                },
                containerColor = Color(0xFF388E3C)
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()

        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = Color.Green)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {

                    items(uiState.items) { note ->
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 6.dp)
                                .border(BorderStroke(1.dp, Color(0xA92E2C2C))),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        updation.value = true
                                        updateThis = note
                                        showBottomSheet = true
                                    }
                                    .padding(8.dp)
                                    .weight(3f),
                                text = note.note,
                                maxLines = 2
                            )
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                tint = Color(0xFFA20303),
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .clickable {
                                        vm.deleteData(note)
                                    }
                                    .padding(end = 13.dp)
                            )
                        }
                        
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    updation.value = false
                    updateThis = Note(-1,"")
                },
                sheetState = sheetState
            ) {
                NoteInput(
                    updation = updation,
                    note = if (updation.value) updateThis.note else ""
                ) { newNote ->
                    // Handle the new note submission
                    scope.launch{
                        if (updation.value){
                            updateThis.note = newNote
                            vm.updateData(updateThis)
                        }

                        else{
                            vm.insertData(Note(note = newNote))
                        }

                        sheetState.hide()
                        showBottomSheet = false

                        updation.value = false
                        updateThis = Note(-1,"")
                    }
                }
            }
        }
    }
}

