package com.laomuji1999.compose.core.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.laomuji1999.compose.core.logic.database.dao.ContactDao
import com.laomuji1999.compose.core.logic.database.dao.MessageDao
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity
import com.laomuji1999.compose.core.logic.model.entity.MessageInfoEntity

@Database(
    entities = [
        ContactInfoEntity::class,
        MessageInfoEntity::class,
    ],
    version = 4
)
abstract class Database : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    abstract fun messageDao(): MessageDao
}