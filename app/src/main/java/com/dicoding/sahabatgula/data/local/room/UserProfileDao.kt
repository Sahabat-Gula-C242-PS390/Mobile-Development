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
    @Query("SELECT * FROM UserProfile WHERE id = :id LIMIT 1")
    suspend fun getUserProfile(id: String?): UserProfile

    // get user profile limit 1
    @Query("SELECT * FROM UserProfile LIMIT 1")
    suspend fun getUserProfileLimit_1(): UserProfile

    // get data userProfile by email
    @Query("SELECT * FROM UserProfile WHERE email = :email LIMIT 1")
    suspend fun getUserProfilebyEmail_limit1(email: String): UserProfile

    // delete semua data user
    @Query("DELETE FROM UserProfile")
    suspend fun deleteAllUserProfile()

    // update data user
    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)

    // check email apakah ada yang sama
    @Query("Select * FROM UserProfile WHERE email = :email")
    suspend fun getUserProfileByEmail(email: String): UserProfile

    // get user id by email
    @Query("SELECT id FROM UserProfile WHERE email = :email")
    suspend fun getUserId(email: String?): String
}