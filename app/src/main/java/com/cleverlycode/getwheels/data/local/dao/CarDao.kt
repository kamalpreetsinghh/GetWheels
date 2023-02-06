package com.cleverlycode.getwheels.data.local.dao

import androidx.room.*
import com.cleverlycode.getwheels.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun observeCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars WHERE isFavorite")
    fun observeFavCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars")
    suspend fun getCars(): List<CarEntity>

    @Query("SELECT * FROM cars WHERE isFavorite")
    suspend fun getFavoriteCars(): List<CarEntity>

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCar(id: String): CarEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(carEntity: CarEntity): Long

    @Update
    suspend fun update(carEntity: CarEntity)

    @Query("UPDATE cars SET isFavorite = :isFavorite WHERE id = :id ")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}