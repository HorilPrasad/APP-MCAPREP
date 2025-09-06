package com.mcaprep.data.local.entities

import androidx.room.*

@Entity(tableName = "projects",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userOwnerId")]
)

data class ProjectEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val paymentId: String,
    val purchaseDate: String,
    val userOwnerId: String // foreign key to UserEntity.id
)

