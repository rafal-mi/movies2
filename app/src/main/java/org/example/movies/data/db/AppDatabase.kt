package org.example.movies.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Movie::class,
        RemoteKeys::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance

                instance
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE remote_keys (
                        movie_id INTEGER NOT NULL PRIMARY KEY,
                        prev_key INTEGER,
                        next_key INTEGER
                );""")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE movie;")
                database.execSQL("""
                    CREATE TABLE movie (
                        id INTEGER NOT NULL PRIMARY KEY,
                        imdb_id TEXT,
                        original_title TEXT NOT NULL,
                        release_date TEXT NOT NULL,
                        backdrop_path TEXT,
                        poster_path TEXT,
                        favorite INTEGER NOT NULL DEFAULT 0
                );""")
            }
        }


    }
}