package com.example.forage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.forage.model.Forageable
import kotlinx.coroutines.flow.Flow

@Dao
interface ForageableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(forageable: Forageable)

    @Update
    suspend fun update(forageable: Forageable)

    @Delete
    suspend fun delete(forageable: Forageable)

    @Query("SELECT * FROM forageable")
    fun getForageables(): Flow<List<Forageable>>

    @Query("SELECT * FROM forageable WHERE id = :id")
    fun getForageable(id: Long): Flow<Forageable>
}
