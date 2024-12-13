package com.dicoding.sahabatgula.data.local.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Buat tabel baru dengan definisi terbaru
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS UserProfile_new (
                id TEXT DEFAULT '' NOT NULL PRIMARY KEY,
                name TEXT DEFAULT '',
                email TEXT DEFAULT '',
                password TEXT DEFAULT '',
                umur INTEGER DEFAULT 0,
                berat INTEGER DEFAULT 0,
                tinggi INTEGER DEFAULT 0,
                gender TEXT DEFAULT '',
                lingkarPinggang TEXT DEFAULT '',
                riwayatDiabetes INTEGER DEFAULT 0 NOT NULL,
                gulaDarahTinggi INTEGER DEFAULT 0 NOT NULL,
                tekananDarahTinggi INTEGER DEFAULT 0 NOT NULL,
                tingkatAktivitas TEXT DEFAULT '',
                konsumsiBuah INTEGER DEFAULT 0 NOT NULL,
                kaloriHarian INTEGER DEFAULT 0 NOT NULL,
                karbohidratHarian INTEGER DEFAULT 0 NOT NULL,
                proteinHarian INTEGER DEFAULT 0 NOT NULL,
                lemakHarian INTEGER DEFAULT 0 NOT NULL,
                gulaHarian INTEGER DEFAULT 0 NOT NULL
            )
            """
            )

            // Salin data dari tabel lama ke tabel baru
            db.execSQL(
                """
            INSERT INTO UserProfile_new (
                id, name, email, password, umur, berat, tinggi, gender, lingkarPinggang,
                riwayatDiabetes, gulaDarahTinggi, tekananDarahTinggi, tingkatAktivitas,
                konsumsiBuah, kaloriHarian, karbohidratHarian, proteinHarian, lemakHarian, gulaHarian
            )
            SELECT
                id, name, email, password, umur, berat, tinggi, gender, lingkarPinggang,
                riwayatDiabetes, gulaDarahTinggi, tekananDarahTinggi, tingkatAktivitas,
                konsumsiBuah, kaloriHarian, karbohidratHarian, proteinHarian, lemakHarian, gulaHarian
            FROM UserProfile
            """
            )

            // Hapus tabel lama
            db.execSQL("DROP TABLE UserProfile")

            // Ganti nama tabel baru menjadi tabel lama
            db.execSQL("ALTER TABLE UserProfile_new RENAME TO UserProfile")
        }
    }

}