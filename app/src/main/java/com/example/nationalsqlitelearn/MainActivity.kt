package com.example.nationalsqlitelearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.nationalsqlitelearn.ui.theme.NationalsqlitelearnTheme
import java.util.UUID
import kotlin.math.acos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NationalsqlitelearnTheme {
                val todo = ViewModelProvider(this)[TodoData::class.java]
                TodoList(todo)
            }
        }
    }
}

@Composable
fun TodoList(db: TodoData) {
    var textInput by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.padding(horizontal = 15.dp)) {
            item {
                OutlinedTextField(
                    textInput,
                    onValueChange = { textInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = {
                    db.addTodo(Todo(UUID.randomUUID().toString(), textInput, false))
                    textInput = ""
                }, modifier = Modifier.fillMaxWidth()) { Text("add") }
                Spacer(Modifier.height(10.dp))
            }
            items(db.todo) {
                Card(modifier = Modifier.padding(vertical = 10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it.name, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                        Row {
                            Button(onClick = {
                                db.delete(it.id)
                            }) {
                                Text("Delete")
                            }
                            Spacer(Modifier.width(10.dp))
                            Button(onClick = {
                                db.update(it.copy(done = !it.done), it.id)
                            }) {
                                Text(if (!it.done) "Not Done" else "Done")
                            }
                        }
                    }
                }
            }
        }
    }
}