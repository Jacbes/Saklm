package dev.jacbes.saklm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import dev.jacbes.saklm.database.AppDatabase
import dev.jacbes.saklm.database.User
import dev.jacbes.saklm.database.UserDao
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "users"
        ).build()
        val userDao = db.userDao()

        val buttonAdd = findViewById<Button>(R.id.add)
        val editFirstName = findViewById<EditText>(R.id.edit_first_name)
        val editLastName = findViewById<EditText>(R.id.edit_last_name)
        buttonAdd.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = User(editFirstName.text.toString(), editLastName.text.toString())
                insertUser(userDao, user)
            }
        }

        val buttonShow = findViewById<Button>(R.id.show)
        val showText = findViewById<TextView>(R.id.show_id)
        buttonShow.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val users = getAllUsers(userDao).joinToString(separator = "\n")
                runOnUiThread {
                    showText.text = users
                }
            }
        }

        val buttonClean = findViewById<Button>(R.id.clean)
        buttonClean.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                userDao.deleteAllUsers()
            }
        }
    }

    private fun insertUser(userDao: UserDao, user: User) {
        userDao.insertAll(user)
    }

    private fun getAllUsers(userDao: UserDao) = userDao.getAll()
}