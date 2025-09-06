package com.mcaprep.data.local.dao
import androidx.room.*
import com.mcaprep.data.local.entities.ProjectEntity
import com.mcaprep.data.local.entities.UserEntity

@Dao
interface UserDao {

    // Insert User and replace if already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Insert all projects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    // Get user with specific ID
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    // Get all projects for a user
    @Query("SELECT * FROM projects WHERE userOwnerId = :userId")
    suspend fun getProjectsByUserId(userId: String): List<ProjectEntity>

    // Delete a user
    @Delete
    suspend fun deleteUser(user: UserEntity)

    // Delete projects by user ID
    @Query("DELETE FROM projects WHERE userOwnerId = :userId")
    suspend fun deleteProjectsByUserId(userId: String)

    // Clear everything
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects()
}
