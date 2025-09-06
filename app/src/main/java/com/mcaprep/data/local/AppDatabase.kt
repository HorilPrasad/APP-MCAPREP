package com.mcaprep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcaprep.data.local.dao.UserDao
import com.mcaprep.data.local.entities.ProjectEntity
import com.mcaprep.data.local.entities.UserEntity

@Database(entities = [UserEntity::class, ProjectEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}