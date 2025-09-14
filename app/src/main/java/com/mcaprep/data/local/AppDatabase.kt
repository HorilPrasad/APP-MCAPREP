package com.mcaprep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcaprep.data.local.dao.AnswerDao // Added import for AnswerDao
import com.mcaprep.data.local.dao.UserDao
import com.mcaprep.data.local.entities.AnswerEntity // Added import for AnswerEntity
import com.mcaprep.data.local.entities.ProjectEntity
import com.mcaprep.data.local.entities.UserEntity

@Database(entities = [UserEntity::class, ProjectEntity::class, AnswerEntity::class], version = 1, exportSchema = false) // Added AnswerEntity and incremented version
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun answerDao(): AnswerDao
}
