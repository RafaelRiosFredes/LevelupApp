package com.example.levelup.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ProductosEntity::class], version = 1)
abstract class AppDatabase  : RoomDatabase() {

    abstract fun productosDao(): ProductosDao


}

