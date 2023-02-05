package com.cleverlycode.getwheels.data.local.dao

import androidx.room.*
import com.cleverlycode.getwheels.data.local.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * from profiles WHERE id = :id")
    suspend fun getProfileDetails(id: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileEntity: ProfileEntity): Long

    @Update
    suspend fun update(profileEntity: ProfileEntity)

    @Delete
    suspend fun delete(profileEntity: ProfileEntity)
}