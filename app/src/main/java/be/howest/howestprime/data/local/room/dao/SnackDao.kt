package be.howest.howestprime.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.howest.howestprime.data.local.room.entity.SnackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackDao {

    @Query("SELECT * FROM snacks")
    fun getAllSnacks(): Flow<List<SnackEntity>>

    @Query("SELECT * FROM snacks WHERE id = :id")
    suspend fun getSnackById(id: String): SnackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(snacks: List<SnackEntity>)
}