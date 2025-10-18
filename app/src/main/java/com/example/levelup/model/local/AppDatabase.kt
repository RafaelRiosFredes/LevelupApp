package com.example.levelup.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.levelup.model.local.LoginEntity

@Database(entities = [LoginEntity::class], version = 1, exportSchema = false)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile private var INSTANCE: LoginDatabase? = null

        fun getDatabase(context: Context): LoginDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LoginDatabase::class.java,
                    "usuarios.db"
                ).build().also { INSTANCE = it }
            }
    }
}
