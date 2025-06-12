package com.laomuji1999.compose.core.logic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.laomuji1999.compose.core.logic.database.entity.MessageInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageInfoEntity WHERE account = :account ORDER BY timestamp DESC")
    fun getMessageList(account:Long): Flow<List<MessageInfoEntity>>

    @Insert
    fun insert(messageInfoEntity: MessageInfoEntity)
}