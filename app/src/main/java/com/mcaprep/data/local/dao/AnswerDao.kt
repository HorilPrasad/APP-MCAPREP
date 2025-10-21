package com.mcaprep.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mcaprep.data.local.entities.AnswerEntity

@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAnswer(answer: AnswerEntity)

    @Query("SELECT * FROM selected_answers WHERE testId = :testId")
    fun getAnswersForTest(testId: String): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM selected_answers WHERE testId = :testId AND questionId = :questionId")
    suspend fun getAnswerForQuestion(testId: String, questionId: String): AnswerEntity?

    @Query("DELETE FROM selected_answers WHERE testId = :testId")
    suspend fun clearAnswersForTest(testId: String) // Added to clear answers when a test is reset or submitted
}
