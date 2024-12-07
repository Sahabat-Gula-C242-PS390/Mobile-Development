package com.dicoding.sahabatgula.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.sahabatgula.data.local.entity.UserProfile

@Dao
interface UserProfileDao {

    // insert data user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    // get data userProfile by id
    @Query("SELECT * FROM UserProfile WHERE id = :id")
    suspend fun getUserProfile(id: Long): UserProfile

    // get semua data user
    @Query("SELECT * FROM UserProfile")
    suspend fun getAllUserProfile(): List<UserProfile>

    // delete semua data user
    @Query("DELETE FROM UserProfile")
    suspend fun deleteAllUserProfile()

    // update data user
    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)

    // check email apakah ada yang sama
    @Query("Select * FROM UserProfile WHERE email = :email")
    suspend fun getUserProfileByEmail(email: String): UserProfile
}