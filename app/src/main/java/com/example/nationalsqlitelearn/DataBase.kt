package com.example.nationalsqlitelearn

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel

data class Todo(
    val id: String,
    val name: String,
    val done: Boolean
)

class TodoData(private val context: Application) : AndroidViewModel(context) {
    var todo = mutableStateListOf<Todo>()
    val db = TodoDataBase(context)

    init {
        get()
    }

    fun get() {
        todo.clear()
        todo.addAll(db.getTodo())
    }

    fun delete(id: String) {
        db.delete(id)
        get()
    }

    fun addTodo(todo: Todo) {
        db.insertTodo(todo)
        get()
    }

    fun update(todo: Todo, id: String) {
        db.update(id, todo.name, todo.done)
        get()
    }
}

class TodoDataBase(context: Context) : SQLiteOpenHelper(context, "app.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE todos (
                id TEXT PRIMARY KEY,
                name TEXT,
                done INTGER 
            )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(
            """
            DROP TABLE IF EXISTS todos
        """.trimIndent()
        )
    }

    fun insertTodo(todo: Todo) {
        writableDatabase.execSQL(
            """
            INSERT INTO todos VALUES(?,?,?)
        """.trimIndent(), arrayOf(todo.id, todo.name, if (todo.done) 1 else 0)
        )
    }

    fun getTodo(): List<Todo> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM todos", null)
        val todos = mutableStateListOf<Todo>()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow("done"))
            todos.add(Todo(id, name, done == 1))
        }
        return todos
    }

    fun delete(id: String) {
        writableDatabase.execSQL(
            """
            DELETE FROM todos WHERE id = ?
        """.trimIndent(), arrayOf(id)
        )
    }

    fun update(id: String, name: String, done: Boolean) {
        writableDatabase.execSQL(
            """
            UPDATE todos SET name = ?, done = ? WHERE id = ?
        """.trimIndent(), arrayOf(name, if (done) 1 else 0, id)
        )
    }
}