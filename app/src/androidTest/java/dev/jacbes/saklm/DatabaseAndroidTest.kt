package dev.jacbes.saklm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.jacbes.saklm.database.AppDatabase
import dev.jacbes.saklm.database.User
import dev.jacbes.saklm.database.UserDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DatabaseAndroidTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user = User("First", "Last")
        userDao.insertAll(user)
        val byName = userDao.findByName("First", "Last")
        assertThat(byName, equalTo(user))
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        val user = User("First", "Last")
        userDao.delete(user)
        val getAll = userDao.getAll()
        assertThat(getAll, equalTo(emptyList()))
    }

    @Test
    @Throws(Exception::class)
    fun writeUsersAndReadUsersByIds() {
        val user1 = User("First", "Last")
        val user2 = User("Last", "First")
        userDao.insertAll(user1, user2)

        val ids = intArrayOf(1, 2)
        val users = userDao.loadAllByIds(ids)
        assertThat(users, equalTo(listOf(user1, user2)))
    }

    @Test
    @Throws(Exception::class)
    fun writeUsersAndDeleteAll() {
        val user1 = User("First", "Last")
        val user2 = User("Last", "First")
        userDao.insertAll(user1, user2)

        userDao.deleteAllUsers()
        val users = userDao.getAll()
        assertThat(users, equalTo(emptyList()))
    }
}