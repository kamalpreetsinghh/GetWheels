package com.cleverlycode.getwheels.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * from cars")
    fun observeCars(): Flow<List<CarEntity>>

    @Query("SELECT * from cars WHERE id = :id")
    suspend fun getCar(id: String): CarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(carEntity: CarEntity): Long

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}