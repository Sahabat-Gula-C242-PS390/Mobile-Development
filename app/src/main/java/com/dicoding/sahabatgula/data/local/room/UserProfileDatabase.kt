package com.dicoding.sahabatgula.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.sahabatgula.data.local.entity.UserProfile

@Database(
    entities = [UserProfile::class],
    version = 2,
    exportSchema = false
)

abstract class UserProfileDatabase: RoomDatabase() {

    // dao yang digunakan
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        // ini instance database yang akan dipakai untuk operasi global
        private var INSTANCE: UserProfileDatabase? = null

        // pakai singleton untuk instance database
        fun getInstance(context: Context): UserProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserProfileDatabase::class.java, // kelas databasenya
                    "user_profile_database"
                ).addMigrations(DatabaseMigration.MIGRATION_1_2)
                    .build() // build database
                INSTANCE = instance
                instance
            }
        }
    }

}