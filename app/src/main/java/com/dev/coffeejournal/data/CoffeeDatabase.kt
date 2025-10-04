package com.dev.coffeejournal.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "coffees")
data class Coffee (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val name: String,
    val origin: String,
    val process: String,
    val roastProfile: String,
    val flavourProfile: String
)

@Dao
interface CoffeeDao {
    @Insert
    suspend fun insert(coffee: Coffee)

    @Query("SELECT * FROM coffees ORDER BY name ASC")
    fun getAllCoffees(): Flow<List<Coffee>>

}

@Database(entities = [Coffee::class], version = 1, exportSchema = false)
abstract class CoffeeDatabase : RoomDatabase() {
    abstract fun coffeeDao(): CoffeeDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeDatabase? = null

        fun getDatabase(context: Context): CoffeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeDatabase::class.java,
                    "coffee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}