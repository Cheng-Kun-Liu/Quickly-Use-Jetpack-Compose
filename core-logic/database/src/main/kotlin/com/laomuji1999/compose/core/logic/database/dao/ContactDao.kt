package com.laomuji1999.compose.core.logic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity

@Dao
interface ContactDao {
    @Query("SELECT * FROM ContactInfoEntity ORDER BY category DESC")
    fun getAll(): List<ContactInfoEntity>

    @Query("SELECT * FROM ContactInfoEntity WHERE account = :account")
    fun getByAccount(account: Long): ContactInfoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<ContactInfoEntity>)
}