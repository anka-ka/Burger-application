package com.example.burgerapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.burgerapplication.dao.CartDao
import com.example.burgerapplication.entity.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}